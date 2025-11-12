package com.example.dsap1.Controllers;

import com.example.dsap1.Controllers.Utils.TextCheckService;
import com.example.dsap1.Controllers.Utils.TikaTextExtractor;
import com.example.dsap1.Controllers.Utils.TransitiionButton;
import com.example.dsap1.Controllers.model.TextCheckRequest;
import com.example.dsap1.Controllers.model.UserInfoDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


@Component
public class ChoiceFileController {

    @FXML
    private Button addFilebutton;

    @FXML
    private Button addUSBbuuton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button exitButton;

    @FXML
    private TextField pathToUSBfield;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button sendFileButton;
    @Autowired
    private TextCheckService textCheckService;
    @Autowired
    UserInfoDTO userInfoDTO;
    @Autowired
    private VBox fileListContainer;
    @Autowired
    TransitiionButton transitiionButton;
    @Autowired
    TikaTextExtractor tikaTextExtractor;

    private final List<String> selectedFilePaths = new ArrayList<>();
    private String usbPath;

    @FXML
    void initialize() {
        anchorPane.setStyle("-fx-background-image: url('" + getClass().getResource("/image/dlp1.png") + "');");
        scrollPane.setContent(fileListContainer);
        fileListContainer.setPadding(new Insets(10));
        fileListContainer.setSpacing(10);
    }

    @FXML
    void addUSBgo(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите USB-накопитель");
        File selectedDir = directoryChooser.showDialog(anchorPane.getScene().getWindow());
        if (selectedDir != null) {
            usbPath = selectedDir.getAbsolutePath();
            pathToUSBfield.setText(usbPath);
        }
    }

    @FXML
    void addFilego(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файлы");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(anchorPane.getScene().getWindow());

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                String filePath = file.getAbsolutePath();
                if (!selectedFilePaths.contains(filePath)) {
                    selectedFilePaths.add(filePath);
                    addFileToUI(filePath);
                }
            }
        }
    }

    private void addFileToUI(String filePath) {
        HBox fileRow = new HBox(10);
        fileRow.setAlignment(Pos.CENTER_LEFT);

        TextField pathField = new TextField(filePath);
        pathField.setFont(Font.font(20));
        pathField.setEditable(false);
        pathField.setStyle("-fx-background-radius: 21px; -fx-font-size: 20px;");

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #167DFF; -fx-background-radius: 21px;");
        deleteButton.setTextFill(Color.WHITE);
        deleteButton.setFont(Font.font("System Bold", 14));

        deleteButton.setOnAction(e -> {
            selectedFilePaths.remove(filePath);
            fileListContainer.getChildren().remove(fileRow);
        });

        fileRow.getChildren().addAll(pathField, deleteButton);
        fileListContainer.getChildren().add(fileRow);
    }

    @FXML
    void exitGo(ActionEvent event) {
        selectedFilePaths.clear();
        pathToUSBfield.clear();
        fileListContainer.getChildren().clear();
        transitiionButton.transition(exitButton, "/com/example/dsap1/AdminConsole.fxml", "Админ");
    }

    @FXML
    void sendFilego(ActionEvent event) {
        if (usbPath == null || usbPath.isEmpty()) {
            pathToUSBfield.setPromptText("Выберите путь");
        }

        for (javafx.scene.Node node : fileListContainer.getChildren()) {
            if (node instanceof HBox hbox) {
                TextField pathField = (TextField) hbox.getChildren().get(0);
                String filePath = pathField.getText();
                File file = new File(filePath);

                try {
                    String text = tikaTextExtractor.extractTextWithOCR(file);
                    String extension = getFileExtension(filePath);

                    TextCheckRequest request = new TextCheckRequest();
                    request.setText(text);
                    request.setExtension("." + extension);
                    request.setRoleId(userInfoDTO.getRoleId());

                    boolean passed = textCheckService.checkText(request);

                    if (passed) {
                        pathField.setStyle("-fx-control-inner-background: #b8ffb8; -fx-background-radius: 21px; -fx-font-size: 20px;");
                        copyFileToUSB(file);
                    } else {
                        pathField.setStyle("-fx-control-inner-background: #ffb8b8; -fx-background-radius: 21px; -fx-font-size: 20px;");
                    }
                    System.out.println("Extension: " + extension);
                    System.out.println("Text content:\n" + text);


                } catch (Exception e) {
                    e.printStackTrace();
                    pathField.setStyle("-fx-control-inner-background: #ffb8b8;");
                }

            }
        }
    }
    private String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filePath.substring(dotIndex + 1);
    }

    private void copyFileToUSB(File file) throws IOException {
        File dest = new File(usbPath, file.getName());
        Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }



}