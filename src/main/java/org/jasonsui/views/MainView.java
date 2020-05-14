package org.jasonsui.views;

import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import org.jasonsui.commons.util.IL8N;
import org.jasonsui.conf.AppConf;
import org.jasonsui.views.main.SelectDbTypeView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.jasonsui.commons.FxmlConstants.MAIN;

@RequiredArgsConstructor
@FXMLView(value = MAIN, bundle = "il8n.messages", stageStyle = "TRANSPARENT")
public class MainView extends AbstractFxmlView implements Initializable {
    @FXML
    private MaterialDesignIconView closeBtn;
    @FXML
    private TableView<?> connectionTable;
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

    private final HostServices hostServices;

    private final AppConf.AppInfo appInfo;

    private final SelectDbTypeView selectDbTypeView;

    private static double xOffset = 0;
    private static double yOffset = 0;

    private Stage selectDbTypeDialog;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCloseBtn();
        initConnectionTable();
        initWindowBarMove();
        initGiteeBtnClick();
        initGithubBtnClick();
        initJianshuBtnClick();
        initAddConnectionBtnClick();
        selectDbType();
    }

    private void selectDbType() {
        selectDbTypeView.getType().addListener((observable, oldValue, newValue) -> System.out.println(newValue));
    }

    private void initAddConnectionBtnClick() {
        JavaFxObservable.eventsOf(addConnectionBtn, MouseEvent.MOUSE_CLICKED).subscribe(actionEvent -> {
            if(Objects.isNull(selectDbTypeDialog)){
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
        });
    }

    private void initJianshuBtnClick() {
        JavaFxObservable.eventsOf(jianshuBtn, MouseEvent.MOUSE_CLICKED).subscribe(actionEvent -> hostServices.showDocument(appInfo.getRecommend().getJianshuUrl()));
    }

    private void initGithubBtnClick() {
        JavaFxObservable.eventsOf(githubBtn, MouseEvent.MOUSE_CLICKED).subscribe(actionEvent -> hostServices.showDocument(appInfo.getRecommend().getGithubUrl()));
    }

    private void initGiteeBtnClick() {
        JavaFxObservable.eventsOf(giteeBtn, MouseEvent.MOUSE_CLICKED).subscribe(actionEvent -> hostServices.showDocument(appInfo.getRecommend().getGiteeUrl()));
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
