package com.example.dsap1.Controllers;

import com.example.dsap1.Controllers.Utils.TransitiionButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Контроллер для управления списком ролей (аналогично стоп-словам, только без ComboBox).
 */
@Component
@Lazy
public class RolesController {

    @FXML
    private TextField roleNameField;   // Поле ввода новой роли

    @FXML
    private Button addRoleButton;      // Кнопка «Добавить»

    @FXML
    private ScrollPane scrollPane;     // ScrollPane для прокрутки списка ролей

    @FXML
    private Button exitButton;         // Кнопка «Выход» (если нужна)

    @FXML private AnchorPane anchorPane;

    @Autowired
    private VBox container;            // VBox, куда будем динамически добавлять роли
    @Autowired
    TransitiionButton transitiionButton;

    @Autowired
    private RestTemplate restTemplate; // Для запросов к бэкенду
    @Value("${url.from.bd}")
    private String baseUrl;


    @FXML
    void initialize() {
        anchorPane.setStyle("-fx-background-image: url('" + getClass().getResource("/image/dlp1.png") + "');");
        // Настройка VBox внутри ScrollPane
        scrollPane.setContent(container);
        container.setSpacing(10); // небольшой отступ между «строками»
        container.setPadding(new Insets(10, 10, 10, 10));

        roleNameField.setPromptText("Введите новую роль");

        // Загрузим уже существующие роли с бэкенда и отобразим
        loadRolesFromBackend();
    }

    /**
     * Подтянуть все роли (GET /roles) и отобразить в контейнере.
     */
    private void loadRolesFromBackend() {
        try {
            Role[] rolesArray = restTemplate.getForObject(baseUrl + "/roles", Role[].class);
            if (rolesArray == null) return;

            // Если хотите — можно сразу отсортировать по алфавиту
            List<Role> roles = new ArrayList<>(Arrays.asList(rolesArray));
            // roles.sort(Comparator.comparing(Role::getName));

            // Добавляем каждую роль на форму
            for (Role r : roles) {
                addRoleToUI(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавить роль в интерфейс (HBox с TextField + кнопка «Удалить»).
     */
    private void addRoleToUI(Role role) {
        // Создаем HBox, чтобы расположить поле и кнопку в одной строке
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        // Текстовое поле с именем роли (аналог стиля у стоп-слов)
        TextField roleField = new TextField(role.getName());
        roleField.setFont(Font.font(20));
        roleField.setEditable(false);
        roleField.setStyle("-fx-background-radius: 21px; -fx-font-size: 20px;");

        // Кнопка «Удалить»
        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #167DFF; -fx-background-radius: 21px;");
        deleteButton.setTextFill(Color.WHITE);
        deleteButton.setFont(Font.font("System Bold", 14));

        // Обработчик удаления: DELETE /roles/{id}
        deleteButton.setOnAction(ev -> {
            deleteRole(role.getId());
            // Удаляем всю "строку" (HBox) из VBox
            container.getChildren().remove(row);
        });

        // Добавляем в HBox
        row.getChildren().addAll(roleField, deleteButton);

        // А сам HBox в VBox
        container.getChildren().add(row);
    }

    /**
     * Кнопка «Добавить» — POST /roles
     */
    @FXML
    void addGo(ActionEvent event) {
        // Считать введённое имя
        String newName = roleNameField.getText();
        if (newName == null || newName.isBlank()) {
            return;
        }
        // Создаём DTO
        Role newRole = new Role();
        newRole.setName(newName);

        try {
            // POST запрос для создания роли
            Role created = restTemplate.postForObject(baseUrl +"/roles", newRole, Role.class);

            // Если всё ок, добавляем в интерфейс
            if (created != null) {
                addRoleToUI(created);
            }
            // Очищаем поле
            roleNameField.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаление роли по id
     */
    private void deleteRole(Long id) {
        try {
            restTemplate.delete(baseUrl + "/roles" + "/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Кнопка «Выход» (или переход на другую сцену).
     */
    @FXML
    void exitGo(ActionEvent event) {
        container.getChildren().clear();
        transitiionButton.transition(exitButton, "/com/example/dsap1/AdminConsole.fxml", "Админ");
    }

    // DTO Role в клиенте (тот же класс, что и на бэкенде, но без аннотаций JPA)
    public static class Role {
        private Long id;
        private String name;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
