package com.example.dsap1.Controllers.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TransitiionButton {

    private final SpringFXMLLoader springFXMLLoader;

    @Autowired
    public TransitiionButton(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    public void transition(Button button, String fxmlPath, String windowTitle) {
        try {
            // Получаем текущее окно
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close(); // Закрываем текущее окно

            // Загружаем FXML через Spring
            FXMLLoader fxmlLoader = springFXMLLoader.load(fxmlPath);
            Parent root = fxmlLoader.load();

            // Создаём новое окно
            Stage newStage = new Stage();
            newStage.setTitle(windowTitle);
            newStage.setScene(new Scene(root, 1200, 800));
            newStage.setResizable(false);
            newStage.show();

            // Обработчик закрытия окна
            newStage.setOnCloseRequest(event -> event.consume());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
