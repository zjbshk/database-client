package org.jasonsui.views;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.HostServices;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasonsui.commons.AppEnum;
import org.jasonsui.commons.ImagesConstants;
import org.jasonsui.commons.util.CodeProWindow;
import org.jasonsui.commons.util.EventBus;
import org.jasonsui.commons.util.IL8N;
import org.jasonsui.conf.AppConf;
import org.jasonsui.modal.DBConfigModal;
import org.jasonsui.modal.LiteConfigModal;
import org.jasonsui.service.SqliteService;
import org.jasonsui.views.main.MysqlConfigView;
import org.jasonsui.views.main.PGConfigView;
import org.jasonsui.views.main.SelectDbTypeView;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.jasonsui.commons.FxmlConstants.MAIN;

@Slf4j
@RequiredArgsConstructor
@FXMLView(value = MAIN, bundle = "il8n.messages", stageStyle = "TRANSPARENT")
public class MainView extends AbstractFxmlView implements Initializable {
    private static double xOffset = 0;
    private static double yOffset = 0;
    private final HostServices hostServices;
    private final AppConf.AppInfo appInfo;
    private final SelectDbTypeView selectDbTypeView;
    private final MysqlConfigView mysqlConfigView;
    private final PGConfigView pgConfigView;
    private final SqliteService sqliteService;
    @FXML
    public AnchorPane mainPane;
    public TableColumn<DBConfigModal, StringProperty> columnName;
    public TableColumn<DBConfigModal, ImageView> columnType;
    public TableColumn<DBConfigModal, StringProperty> columnHost;
    public TableColumn<DBConfigModal, StringProperty> columnDatabase;
    @FXML
    private MaterialDesignIconView closeBtn;
    @FXML
    private TableView<DBConfigModal> connectionTable;
    @FXML
    private AnchorPane windowBar;
    @FXML
    private ImageView giteeBtn;
    @FXML
    private ImageView githubBtn;
    @FXML
    private ImageView jianshuBtn;
    @FXML
    private JFXButton addConnectionBtn;
    @FXML
    private AnchorPane leftPane;
    private Stage selectDbTypeDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftPane.setBackground(new Background(new BackgroundImage(new Image("images/screen.png", 32, 32, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
        initCloseBtn();
        initConnectionTable();
        initWindowBarMove();
        initGiteeBtnClick();
        initGithubBtnClick();
        initJianshuBtnClick();
        initAddConnectionBtnClick();
        selectDbType();
        initMainPaneKeyPressed();
        initLoadTable();
        initConnectionTableContextMenu();
        EventBus.getInstance().register(AppEnum.Events.RELOAD_DB_CONFIG, e -> {
            initLoadTable();
        });
    }

    private void initConnectionTableContextMenu() {
        JavaFxObservable.eventsOf(connectionTable, ContextMenuEvent.CONTEXT_MENU_REQUESTED).subscribe(contextMenuEvent -> {
            log.info("MainView_initConnectionTableContextMenu_contextMenuEvent={}", contextMenuEvent);
            ContextMenu contextMenu = new ContextMenu();
            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
            editIcon.setStyle("-fx-text-fill: #fff;-fx-fill: #fff");
            editIcon.setSize("1.2em");
            MenuItem openMenu = new MenuItem("修改");
            openMenu.setGraphic(editIcon);
            openMenu.setOnAction(event -> {

            });
            MenuItem renameMenu = new MenuItem("删除");
            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.RECYCLE);
            deleteIcon.setStyle("-fx-text-fill: #fff;-fx-fill: #fff");
            deleteIcon.setSize("1.2em");
            renameMenu.setGraphic(deleteIcon);
            renameMenu.setOnAction(event -> {

            });
            contextMenu.getItems().addAll(openMenu, renameMenu);
            ((TableView) contextMenuEvent.getSource()).setContextMenu(contextMenu);
        });
    }

    private void initLoadTable() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnHost.setCellValueFactory(new PropertyValueFactory<>("host"));
        columnDatabase.setCellValueFactory(new PropertyValueFactory<>("database"));
        columnType.setCellValueFactory(param -> {
            DBConfigModal value = param.getValue();
            String imageUrl = "";
            switch (value.getTypeEnum()) {
                case MYSQL:
                    imageUrl = ImagesConstants.MYSQL;
                    break;
                case ORACLE:
                    imageUrl = ImagesConstants.ORACLE;
                    break;
                case PG:
                    imageUrl = ImagesConstants.PG;
                    break;
            }
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            return new SimpleObjectProperty<ImageView>(imageView);
        });
        List<LiteConfigModal> liteConfigModals = sqliteService.selectALl();
        if (CollectionUtil.isNotEmpty(liteConfigModals)) {
            List<DBConfigModal> dbConfigModals = liteConfigModals.stream().map(x -> JSONObject.parseObject(x.getDbmodal(),
                    DBConfigModal.class)).collect(Collectors.toList());
            log.error("MainView_initLoadTable_dbConfigModals={}", JSON.toJSONString(dbConfigModals));
            this.connectionTable.getItems().clear();
            this.connectionTable.getItems().addAll(dbConfigModals);
        }

    }

    private void initMainPaneKeyPressed() {
        JavaFxObservable.eventsOf(mainPane, KeyEvent.KEY_PRESSED)
                .filter(keyEvent -> keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.N))
                .subscribe(this::openSelectDbTypeDialog);
    }

    private void selectDbType() {
        EventBus.getInstance().register(AppEnum.Events.SELECT_DB_TYPE, e -> {
            AppEnum.TypeEnum val = (AppEnum.TypeEnum) e;
            switch (val) {
                case MYSQL:
                    CodeProWindow codeProWindow = new CodeProWindow(mainPane, "新建Mysql连接", "images/db/mysql.png",
                            mysqlConfigView.getView());
                    codeProWindow.show();
                    break;
                case PG:
                    codeProWindow = new CodeProWindow(mainPane, "新建Postgresql连接", "images/db/pg.png",
                            pgConfigView.getView());
                    codeProWindow.show();
                    break;
                case ORACLE:
                    codeProWindow = new CodeProWindow(mainPane, "新建Oracle连接", "images/db/oracle.png",
                            mysqlConfigView.getView());
                    codeProWindow.show();
                    break;
            }

        });
    }

    private void initAddConnectionBtnClick() {
        Tooltip tooltip = new Tooltip("Ctrl+N");
        addConnectionBtn.setTooltip(tooltip);
        JavaFxObservable.eventsOf(addConnectionBtn, MouseEvent.MOUSE_CLICKED)
                .subscribe(this::openSelectDbTypeDialog);
    }

    private void openSelectDbTypeDialog(Object event) {
        if (Objects.isNull(selectDbTypeDialog)) {
            Window window = windowBar.getScene().getWindow();
            selectDbTypeDialog = new Stage(StageStyle.TRANSPARENT);
            selectDbTypeDialog.initModality(Modality.WINDOW_MODAL);
            selectDbTypeDialog.initStyle(StageStyle.TRANSPARENT);
            selectDbTypeDialog.initOwner(window);
            Scene scene = new Scene(selectDbTypeView.getView());
            scene.setFill(Color.TRANSPARENT);
            selectDbTypeDialog.setScene(scene);
        }
        selectDbTypeDialog.show();
        selectDbTypeDialog.setOnShown(e -> {
            selectDbTypeView.initDropShadow();
        });
    }

    private void initJianshuBtnClick() {
        JavaFxObservable.eventsOf(jianshuBtn, MouseEvent.MOUSE_CLICKED)
                .subscribe(actionEvent -> hostServices.showDocument(appInfo.getRecommend().getJianshuUrl()));
    }

    private void initGithubBtnClick() {
        JavaFxObservable.eventsOf(githubBtn, MouseEvent.MOUSE_CLICKED)
                .subscribe(actionEvent -> hostServices.showDocument(appInfo.getRecommend().getGithubUrl()));
    }

    private void initGiteeBtnClick() {
        JavaFxObservable.eventsOf(giteeBtn, MouseEvent.MOUSE_CLICKED)
                .subscribe(actionEvent -> hostServices.showDocument(appInfo.getRecommend().getGiteeUrl()));
    }

    private void initWindowBarMove() {
        windowBar.setOnMouseDragged(event -> {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            window.setX(event.getScreenX() - xOffset);
            window.setY(event.getScreenY() - yOffset);
        });
        windowBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
    }

    private void initConnectionTable() {
        this.connectionTable.setPlaceholder(new Label(IL8N.get("connection.table.placeholder")));
    }

    private void initCloseBtn() {
        closeBtn.setOnMouseEntered(event -> {
            closeBtn.getStyleClass().add("");
            closeBtn.setIcon(MaterialDesignIcon.CLOSE_CIRCLE_OUTLINE);
        });
        closeBtn.setOnMouseExited(event -> closeBtn.setIcon(MaterialDesignIcon.CHECKBOX_BLANK_CIRCLE));
        closeBtn.setOnMouseClicked(event -> {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            window.hide();
        });
    }
}
