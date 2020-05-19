package org.jasonsui.commons.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lombok.Getter;
import org.jasonsui.views.plugins.WindowPlugin;

import java.io.IOException;

import static org.jasonsui.commons.FxmlConstants.PLUGINS_WINDOW;

public class CodeProWindow {

    @Getter
    private Stage stage;

    public CodeProWindow(Node source, String title, String iconFile, Node node) {
        FXMLLoader load = FXMLLoadUtil.load(PLUGINS_WINDOW);
        try {
            AnchorPane anchorPane = load.load();
            WindowPlugin windowPlugin = load.getController();
            windowPlugin.setProperty(title, iconFile, node);
            Window window = source.getScene().getWindow();
            stage = new Stage(StageStyle.TRANSPARENT);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initOwner(window);
            Scene scene = new Scene(anchorPane);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        stage.showAndWait();
    }

    public void close() {
        stage.close();
    }

}
