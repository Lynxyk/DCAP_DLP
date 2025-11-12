package com.example.dsap1.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.dsap1.Controllers.Utils.PasswordResetService;
import com.example.dsap1.Controllers.Utils.TransitiionButton;
import com.example.dsap1.Controllers.Utils.VisualEffect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class RestorePassController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exit;

    @FXML
    private Button keyBut;

    @FXML
    private TextField keyInp;

    @FXML
    private TextField login;

    @FXML
    private Button next;

    @FXML
    private PasswordField password;

    @FXML private AnchorPane anchorPane;

    @Autowired
    private VisualEffect visualEffect;
    @Autowired
    TransitiionButton transitiionButton;
    @Autowired
    private PasswordResetService passwordResetService;


    @FXML
    void exitGo(ActionEvent event) {
        transitiionButton.transition(exit, "/com/example/dsap1/Authorization.fxml", "Восстановление");
    }

    @FXML
    void keyGo(ActionEvent event) {

        if (!login.getText().isEmpty() && !password.getText().isEmpty()){
            String result = passwordResetService.sendCode(login.getText(), password.getText());
            showStyledAlert(result);

        } else {
            login.setText("");
            login.setPromptText("Введите логин и пароль");
        }
    }

    @FXML
    void nextGo(ActionEvent event) {
        if (!login.getText().isEmpty() && !password.getText().isEmpty()){
            String result = passwordResetService.confirmCode(login.getText(), keyInp.getText());
            showStyledAlert(result);
        } else {
            login.setText("");
            login.setPromptText("Введите логин и пароль");
            keyInp.setText("");
            keyInp.setPromptText("Введите ключ");
        }
    }

    @FXML
    void initialize() {
        anchorPane.setStyle("-fx-background-image: url('" + getClass().getResource("/image/dlp1.png") + "');");
        visualEffect.addButtonEffects(next);
        visualEffect.addButtonEffects(exit);
        visualEffect.addButtonEffects(keyBut);
    }

    private void showStyledAlert(String message) {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.initStyle(StageStyle.TRANSPARENT); // Убираем окно, рамки, крестик

        Label label = new Label(message);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");

        Button okButton = new Button("ОК");
        okButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #2196F3, #0D47A1);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 20 6 20;"
        );
        okButton.setOnAction(e -> alertStage.close());

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: #2e2e2e;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 30;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 0);"
        );
        layout.getChildren().addAll(label, okButton);

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT); // Прозрачный фон у сцены

        alertStage.setScene(scene);
        alertStage.setResizable(false);
        alertStage.showAndWait();
    }



}