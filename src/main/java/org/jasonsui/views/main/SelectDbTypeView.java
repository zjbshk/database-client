package org.jasonsui.views.main;

import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import lombok.Getter;
import org.jasonsui.commons.AppEnum;

import java.net.URL;
import java.util.ResourceBundle;

import static org.jasonsui.commons.FxmlConstants.SELECT_DB_TYPE;

@FXMLView(value = SELECT_DB_TYPE)
public class SelectDbTypeView extends AbstractFxmlView implements Initializable {


    @FXML
    private AnchorPane dialog;

    @FXML
    private HBox windowTitle;

    @FXML
    private MaterialDesignIconView closeBtn;

    @FXML
    private JFXButton mysqlBtn;

    @FXML
    private JFXButton pgBtn;

    @FXML
    private JFXButton oracleBtn;

    private static double xOffset = 0;

    private static double yOffset = 0;

    @Getter
    private ObjectProperty<AppEnum.TypeEnum> type = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEscapeHandler();
        windowDragged();
        initCloseBtn();
        initSelectClick();
    }

    private void initSelectClick() {
        JavaFxObservable.eventsOf(mysqlBtn, MouseEvent.MOUSE_CLICKED).subscribe(event -> setValue(AppEnum.TypeEnum.MYSQL));
        JavaFxObservable.eventsOf(oracleBtn, MouseEvent.MOUSE_CLICKED).subscribe(event -> setValue(AppEnum.TypeEnum.ORACLE));
        JavaFxObservable.eventsOf(pgBtn, MouseEvent.MOUSE_CLICKED).subscribe(event -> setValue(AppEnum.TypeEnum.PG));
    }

    private void setValue(AppEnum.TypeEnum typeEnum) {
        type.set(typeEnum);
        Window window = mysqlBtn.getScene().getWindow();
        window.hide();
    }

    private void initCloseBtn() {
        JavaFxObservable.eventsOf(closeBtn, MouseEvent.MOUSE_ENTERED).subscribe(event ->
                closeBtn.setIcon(MaterialDesignIcon.CLOSE_CIRCLE_OUTLINE));
        JavaFxObservable.eventsOf(closeBtn, MouseEvent.MOUSE_EXITED).subscribe(event ->
                closeBtn.setIcon(MaterialDesignIcon.CHECKBOX_BLANK_CIRCLE));
        JavaFxObservable.eventsOf(closeBtn, MouseEvent.MOUSE_CLICKED).subscribe(event -> {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            window.hide();
        });
    }

    private void initEscapeHandler() {
        dialog.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Window window = ((Node) event.getSource()).getScene().getWindow();
                window.hide();
            }
        });
    }

    private void windowDragged() {
        JavaFxObservable.eventsOf(windowTitle, MouseDragEvent.MOUSE_DRAGGED).subscribe(event -> {
            Window window = ((Node) event.getSource()).getScene().getWindow();
            window.setX(event.getScreenX() - xOffset);
            window.setY(event.getScreenY() - yOffset);
        });

        JavaFxObservable.eventsOf(windowTitle, MouseEvent.MOUSE_PRESSED).subscribe(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
    }
}
