package org.jasonsui.views.main;

import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.jasonsui.commons.AppEnum;
import org.jasonsui.commons.util.EventBus;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static org.jasonsui.commons.FxmlConstants.SELECT_DB_TYPE;

@Slf4j
@FXMLView(value = SELECT_DB_TYPE)
public class SelectDbTypeView extends AbstractFxmlView implements Initializable {


    private static double xOffset = 0;
    private static double yOffset = 0;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEscapeHandler();
        windowDragged();
        initCloseBtn();
        initSelectClick();
        initKeyEnterHandler();
        initDropShadow();
    }

    public void initDropShadow() {
        DropShadow shadow = (DropShadow) dialog.getEffect();
        Timeline timeline = new Timeline();
        Random rand = new Random();
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(shadow.radiusProperty(), shadow.getRadius()),
                        new KeyValue(shadow.spreadProperty(), shadow.getSpread()),
                        new KeyValue(shadow.offsetXProperty(), shadow.getOffsetX()),
                        new KeyValue(shadow.offsetYProperty(), shadow.getOffsetY()),
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(shadow.widthProperty(), 35),
                        new KeyValue(shadow.heightProperty(), 35),
                        new KeyValue(shadow.radiusProperty(), 10),
                        new KeyValue(shadow.spreadProperty(), 0.38),
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))
                ),
                new KeyFrame(Duration.ZERO,
                        new KeyValue(shadow.radiusProperty(), shadow.getRadius()),
                        new KeyValue(shadow.spreadProperty(), shadow.getSpread()),
                        new KeyValue(shadow.offsetXProperty(), shadow.getOffsetX()),
                        new KeyValue(shadow.offsetYProperty(), shadow.getOffsetY()),
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(shadow.widthProperty(), 35),
                        new KeyValue(shadow.heightProperty(), 35),
                        new KeyValue(shadow.radiusProperty(), 10),
                        new KeyValue(shadow.spreadProperty(), 0.38),
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()))
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    private void initKeyEnterHandler() {
        JavaFxObservable.eventsOf(dialog, KeyEvent.KEY_PRESSED)
                .filter(keyEvent -> keyEvent.getCode() == KeyCode.ENTER)
                .subscribe(keyEvent -> {
                    if (mysqlBtn.isFocused()) {
                        setValue(AppEnum.TypeEnum.MYSQL);
                        return;
                    }
                    if (oracleBtn.isFocused()) {
                        setValue(AppEnum.TypeEnum.ORACLE);
                        return;
                    }
                    if (pgBtn.isFocused()) {
                        setValue(AppEnum.TypeEnum.PG);
                    }
                });
    }

    private void initSelectClick() {
        JavaFxObservable.eventsOf(mysqlBtn, MouseEvent.MOUSE_CLICKED).subscribe(event -> setValue(AppEnum.TypeEnum.MYSQL));
        JavaFxObservable.eventsOf(oracleBtn, MouseEvent.MOUSE_CLICKED).subscribe(event -> setValue(AppEnum.TypeEnum.ORACLE));
        JavaFxObservable.eventsOf(pgBtn, MouseEvent.MOUSE_CLICKED).subscribe(event -> setValue(AppEnum.TypeEnum.PG));
    }

    private void setValue(AppEnum.TypeEnum typeEnum) {
        Window window = mysqlBtn.getScene().getWindow();
        window.hide();
        EventBus.getInstance().post(AppEnum.Events.SELECT_DB_TYPE, typeEnum);
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
