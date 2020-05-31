package org.jasonsui.views.main;

import com.alibaba.fastjson.JSON;
import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.jasonsui.commons.AppEnum;
import org.jasonsui.commons.group.Group;
import org.jasonsui.commons.util.CodeProAlert;
import org.jasonsui.commons.util.EventBus;
import org.jasonsui.commons.util.ValidatorUtil;
import org.jasonsui.modal.MySqlDBConfigModal;
import org.jasonsui.service.DBFactory;
import org.jasonsui.service.SqliteService;

import java.net.URL;
import java.util.ResourceBundle;

import static org.jasonsui.commons.FxmlConstants.MYSQL_CONFIG;

@Slf4j
@FXMLView(value = MYSQL_CONFIG)
public class MysqlConfigView extends AbstractFxmlView implements Initializable {
    private final MySqlDBConfigModal dbConfigModal = new MySqlDBConfigModal(AppEnum.TypeEnum.MYSQL);
    private final DBFactory dbFactory;
    private final SqliteService sqliteService;
    @FXML
    private TextField nameTextF;
    @FXML
    private TextField hostTextF;
    @FXML
    private TextField portTextF;
    @FXML
    private TextField userTextF;
    @FXML
    private PasswordField passwordTextF;
    @FXML
    private TextField databaseTextF;
    @FXML
    private TextField urlTextF;
    @FXML
    private JFXButton testBtn;
    @FXML
    private JFXButton confirmBtn;
    @FXML
    private AnchorPane root;

    public MysqlConfigView(DBFactory dbFactory, SqliteService sqliteService) {
        this.dbFactory = dbFactory;
        this.sqliteService = sqliteService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVal();
        initTestBtnClick();
        initConfirmBtnClick();
    }

    private void initConfirmBtnClick() {
        JavaFxObservable.eventsOf(root, KeyEvent.KEY_PRESSED)
                .filter(keyEvent -> keyEvent.getCode().equals(KeyCode.ENTER))
                .subscribe(keyEvent -> confirmConnection());
        JavaFxObservable.eventsOf(confirmBtn, MouseEvent.MOUSE_CLICKED)
                .subscribe(event -> confirmConnection());
        log.info("MysqlConfigView_initTestBtnClick_dbConfigModal={}", JSON.toJSONString(dbConfigModal));
    }

    private void confirmConnection() {
        try {
            ValidatorUtil.validator(dbConfigModal, Group.Name.class, Group.Host.class, Group.Port.class,
                    Group.User.class, Group.Password.class, Group.Database.class, Group.Url.class);
            dbFactory.getConn(dbConfigModal);
            sqliteService.save(JSON.toJSONString(dbConfigModal));
            EventBus.getInstance().post(AppEnum.Events.RELOAD_DB_CONFIG, true);
            close();
        } catch (RuntimeException e) {
            log.info("MysqlConfigView_confirmConnection_e={}", e.getMessage());
            CodeProAlert.openAlert((Stage) root.getScene().getWindow(), e.getMessage());
        }
    }


    private void initTestBtnClick() {
        JavaFxObservable.eventsOf(testBtn, MouseEvent.MOUSE_CLICKED)
                .subscribe(event -> {
                    try {
                        ValidatorUtil.validator(dbConfigModal, Group.Name.class, Group.Host.class, Group.Port.class,
                                Group.User.class, Group.Password.class, Group.Database.class, Group.Url.class);
                        dbFactory.getConn(dbConfigModal);
                        CodeProAlert.openAlert((Stage) root.getScene().getWindow(), "连接成功");
                    } catch (RuntimeException e) {
                        log.info("MysqlConfigView_confirmConnection_e={}", e.getMessage());
                        CodeProAlert.openAlert((Stage) root.getScene().getWindow(), e.getMessage());
                    }
                });
    }

    private void close() {
        root.getScene().getWindow().hide();
    }

    private void initVal() {
        dbConfigModal
                .setName(nameTextF.getText())
                .setHost(hostTextF.getText())
                .setPort(portTextF.getText())
                .setUser(userTextF.getText())
                .setUrl(urlTextF.getText())
                .setPassword(passwordTextF.getText())
                .setDatabase(databaseTextF.getText());
        JavaFxObservable.changesOf(hostTextF.textProperty()).subscribe(this::setUrlTextF);
        JavaFxObservable.changesOf(portTextF.textProperty()).subscribe(this::setUrlTextF);
        JavaFxObservable.changesOf(databaseTextF.textProperty()).subscribe(this::setUrlTextF);
        JavaFxObservable.changesOf(nameTextF.textProperty()).subscribe(x -> dbConfigModal.setName(x.getNewVal()));
        JavaFxObservable.changesOf(hostTextF.textProperty()).subscribe(x -> dbConfigModal.setHost(x.getNewVal()));
        JavaFxObservable.changesOf(portTextF.textProperty()).subscribe(x -> dbConfigModal.setPort(x.getNewVal()));
        JavaFxObservable.changesOf(userTextF.textProperty()).subscribe(x -> dbConfigModal.setUser(x.getNewVal()));
        JavaFxObservable.changesOf(passwordTextF.textProperty()).subscribe(x -> dbConfigModal.setPassword(x.getNewVal()));
        JavaFxObservable.changesOf(databaseTextF.textProperty()).subscribe(x -> dbConfigModal.setDatabase(x.getNewVal()));
        JavaFxObservable.changesOf(urlTextF.textProperty()).subscribe(x -> dbConfigModal.setUrl(x.getNewVal()));
        hostTextF.setText("localhost");
        portTextF.setText("3306");
        urlTextF.setText("jdbc:mysql://localhost:3306");
    }

    /**
     * Change<String> stringChange
     *
     * @param change change
     */
    public void setUrlTextF(Change<String> change) {
        String urlVal = urlTextF.getText();
        String[] split = urlVal.split("\\?");
        if (split.length > 1) {
            urlTextF.setText(String.format("jdbc:mysql://%s:%s/%s?%s", hostTextF.getText(), portTextF.getText(),
                    databaseTextF.getText(), split[1]));
        } else {
            urlTextF.setText(String.format("jdbc:mysql://%s:%s/%s", hostTextF.getText(), portTextF.getText(),
                    databaseTextF.getText()));
        }

    }
}
