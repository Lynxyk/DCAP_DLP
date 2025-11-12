package com.example.dsap1;

import com.example.dsap1.Controllers.Utils.SpringFXMLLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;


public class HelloApplication extends Application {
    private ConfigurableApplicationContext context;


    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        SpringFXMLLoader loader = context.getBean(SpringFXMLLoader.class);
        FXMLLoader fxmlLoader = loader.load("/com/example/dsap1/Authorization.fxml");
        stage.setOnCloseRequest(event -> {
            //Platform.exit();
            //System.exit(0);
            event.consume();
        });
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Авторизация");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
