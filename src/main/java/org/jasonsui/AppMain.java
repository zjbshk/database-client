package org.jasonsui;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.HostServices;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jasonsui.support.MySplashScreen;
import org.jasonsui.views.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author zjb
 */
@SpringBootApplication
public class AppMain extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(AppMain.class, MainView.class, new MySplashScreen(), args);
    }

    @Bean
    public HostServices hostServices() {
        return getHostServices();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);
        super.start(stage);
    }

}
