package com.example.dsap1.Controllers;

import com.example.dsap1.Controllers.Utils.AuthService;
import com.example.dsap1.Controllers.Utils.TransitiionButton;
import com.example.dsap1.Controllers.Utils.VisualEffect;
import com.example.dsap1.Controllers.model.UserInfoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;


@Component
public class AuthorizationController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button next;
    @FXML private Button exit;
    @FXML private Button restore;
    @FXML private AnchorPane anchorPane;


    @Autowired
    private VisualEffect visualEffect;
    @Autowired
    TransitiionButton transitiionButton;
    @Autowired
    AuthService authService;
    @Autowired
    UserInfoDTO userInfoDTO;

    @FXML
    public void initialize() {
        visualEffect.addButtonEffects(next);
        visualEffect.addButtonEffects(exit);
        visualEffect.addButtonEffects(restore);
        anchorPane.setStyle("-fx-background-image: url('" + getClass().getResource("/image/ChoiceDir.png") + "');");
    }

    @FXML
    private void nextGo() {
        if (!login.getText().isEmpty() && !password.getText().isEmpty()){
            userInfoDTO = authService.login(login.getText(), password.getText());
            if (userInfoDTO != null) {
                System.out.println("Успешный вход: " + userInfoDTO.getFullName() + " / " + userInfoDTO.getRoleName() + " логин: " + login.getText() + " id_role=" + userInfoDTO.getRoleId());
                transitiionButton.transition(next, "/com/example/dsap1/ChoiceFile.fxml", "Админ");
            } else {
                System.out.println("Неверный логин или пароль.");
            }
        }else {
            login.setText("");
            password.setText("");
            login.setPromptText("Введите логин");
            password.setPromptText("Введите пароль");
        }

    }

    @FXML
    private void exitGo() {

    }

    @FXML
    private void restroreGo() {
        transitiionButton.transition(restore, "/com/example/dsap1/RestorePass.fxml", "Восстановление");
    }
}
