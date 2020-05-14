package org.jasonsui.support;

import com.jfoenix.controls.JFXProgressBar;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MySplashScreen extends SplashScreen {

    public Parent getParent() {
        final ImageView imageView = new ImageView(MySplashScreen.class.getResource("/images/screen.png").toExternalForm());
        final JFXProgressBar splashProgressBar = new JFXProgressBar();
        splashProgressBar.setPrefWidth(600);
        splashProgressBar.setPrefHeight(5);
        final VBox vbox = new VBox();
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);
        vbox.getChildren().addAll(imageView, splashProgressBar);
        return vbox;
    }
}
