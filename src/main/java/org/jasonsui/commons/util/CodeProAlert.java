package org.jasonsui.commons.util;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.jasonsui.views.main.MysqlConfigView;

public class CodeProAlert {
    public static void openAlert(Stage stage, String e) {
        Platform.runLater(() -> {
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.getStylesheets().add(MysqlConfigView.class.getResource("/css/app.css").toExternalForm());
            layout.getStyleClass().add("confirm-dialog");
            Label label = new Label(e);
            label.getStyleClass().add("confirm-dialog-text-fill");
            layout.setBody(label);
            JFXAlert<Void> alert = new JFXAlert<>(stage);
            layout.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    alert.close();
                }
            });
            JFXButton ok = new JFXButton("");
            ok.setMaxHeight(0);
            ok.setPrefHeight(0);
            layout.setActions(ok);
            alert.setAnimation(JFXAlertAnimation.BOTTOM_ANIMATION);
            alert.setContent(layout);
            alert.setTitle("信息");
            alert.setOverlayClose(true);
            alert.showAndWait();
        });
    }
}
