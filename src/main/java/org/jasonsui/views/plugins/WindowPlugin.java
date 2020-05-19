package org.jasonsui.views.plugins;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.Duration;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class WindowPlugin implements Initializable {
    public AnchorPane dialog;
    @FXML
    private AnchorPane windowTitle;

    @FXML
    private ImageView icon;

    @FXML
    private Text title;

    @FXML
    private MaterialDesignIconView closeBtn;

    @FXML
    private AnchorPane content;

    private static double xOffset = 0;

    private static double yOffset = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCloseBtn();
        initEscapeHandler();
        windowDragged();
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
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()))
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(shadow.widthProperty(), 35),
                        new KeyValue(shadow.heightProperty(), 35),
                        new KeyValue(shadow.radiusProperty(), 10),
                        new KeyValue(shadow.spreadProperty(), 0.38),
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()))
                ),
                new KeyFrame(Duration.ZERO,
                        new KeyValue(shadow.radiusProperty(), shadow.getRadius()),
                        new KeyValue(shadow.spreadProperty(), shadow.getSpread()),
                        new KeyValue(shadow.offsetXProperty(), shadow.getOffsetX()),
                        new KeyValue(shadow.offsetYProperty(), shadow.getOffsetY()),
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()))
                ),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(shadow.widthProperty(), 35),
                        new KeyValue(shadow.heightProperty(), 35),
                        new KeyValue(shadow.radiusProperty(), 10),
                        new KeyValue(shadow.spreadProperty(), 0.38),
                        new KeyValue(shadow.colorProperty(), Color.color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()))
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    public void setProperty(String title, String iconFile, Node node){
        this.content.getChildren().add(node);
        this.title.setText(title);
        this.icon.setImage(new Image(iconFile));
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
