package com.example.dsap1.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.dsap1.Controllers.Utils.TransitiionButton;
import com.example.dsap1.Controllers.Utils.VisualEffect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class AdminConsoleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exit;

    @FXML
    private Button fileExtension;

    @FXML
    private Button regularExepssion;

    @FXML
    private Button role;

    @FXML
    private Button stopWords;

    @FXML
    private Button users;

    @FXML private AnchorPane anchorPane;

    @Autowired
    TransitiionButton transitiionButton;
    @Autowired
    VisualEffect visualEffect;

    @FXML
    void exitGo(ActionEvent event) {
    }

    @FXML
    void fileExtensionGo(ActionEvent event) {
        transitiionButton.transition(fileExtension, "/com/example/dsap1/StopFileExtension.fxml", "РАСШИРЕНИЯ");
    }

    @FXML
    void regularExepssionGo(ActionEvent event) {
        transitiionButton.transition(fileExtension, "/com/example/dsap1/RegularExpression.fxml", "РЕГУЛЯРНЫЕ ВЫРАЖЕНИЯ");
    }

    @FXML
    void roleGo(ActionEvent event) {
        transitiionButton.transition(fileExtension, "/com/example/dsap1/Role.fxml", "РОЛИ");
    }

    @FXML
    void stopWordsGo(ActionEvent event) {
        transitiionButton.transition(stopWords, "/com/example/dsap1/StopWord.fxml", "СТОП-СЛОВА");
    }

    @FXML
    void usersGo(ActionEvent event) {
        transitiionButton.transition(stopWords, "/com/example/dsap1/Users.fxml", "ПОЛЬЗОВАТЕЛИ");
    }

    @FXML
    void initialize() {
        visualEffect.addButtonEffects(stopWords);
        visualEffect.addButtonEffects(exit);
        visualEffect.addButtonEffects(fileExtension);
        visualEffect.addButtonEffects(role);
        visualEffect.addButtonEffects(users);
        visualEffect.addButtonEffects(regularExepssion);
        anchorPane.setStyle("-fx-background-image: url('" + getClass().getResource("/image/dlp1.png") + "');");

    }

}
