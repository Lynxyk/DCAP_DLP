package com.example.dsap1.Controllers;

import com.example.dsap1.Controllers.Utils.TransitiionButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
@Lazy
public class RegularExpressionController {

    @FXML
    private TextField inpCoiceFile;
    @FXML
    private Button add;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button exit;
    @FXML private AnchorPane anchorPane;


    // Теперь ComboBox параметризуем Role
    @FXML
    private ComboBox<Role> comboBoxRole;

    @Autowired
    private VBox container;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransitiionButton transitiionButton;

    @Value("${url.from.bd}")
    private String baseUrl;


    @FXML
    void initialize() {
        anchorPane.setStyle("-fx-background-image: url('" + getClass().getResource("/image/dlp1.png") + "');");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(container);
        container.setSpacing(10);

        inpCoiceFile.setPromptText("Введите новое слово");

        // Подтягиваем существующие стоп-слова
        loadRegularFromBackend();
        // Подтягиваем список ролей и заполняем ComboBox
        loadRolesFromBackend();

        // При желании зададим стили прямо в коде (можно вынести в .css)
        comboBoxRole.setStyle(
                "-fx-font-size: 20px;"
                        + "-fx-background-color: white;"
                        + "-fx-text-fill: black;"
                        + "-fx-background-radius: 21px;"
        );

        // Настройка выпадающего списка (ячеек)
        comboBoxRole.setCellFactory(listView -> {
            ListCell<Role> cell = new ListCell<>() {
                @Override
                protected void updateItem(Role role, boolean empty) {
                    super.updateItem(role, empty);
                    if (empty || role == null) {
                        setText(null);
                        setStyle(""); // сбрасываем стиль, когда пусто
                    } else {
                        setText(role.getName());
                        // Можно здесь же добавить стили для каждой ячейки
                        setStyle("-fx-background-color: white; "
                                + "-fx-text-fill: black;"
                                + "-fx-background-radius: 21px;");
                    }
                }
            };
            return cell;
        });
    }

    /**
     * Получаем список ролей и заполняем comboBoxRole
     */
    private void loadRolesFromBackend() {
        try {
            // Получаем массив ролей с бэкенда
            Role[] rolesArray = restTemplate.getForObject(baseUrl + "/roles", Role[].class);
            if (rolesArray != null) {
                // Преобразуем в List и добавляем в ComboBox
                List<Role> roles = Arrays.asList(rolesArray);
                comboBoxRole.getItems().addAll(roles);
            }
            // Чтобы в «поле» ComboBox отображалось только имя роли
            comboBoxRole.setConverter(new StringConverter<Role>() {
                @Override
                public String toString(Role role) {
                    return (role == null) ? "" : role.getName();
                }
                @Override
                public Role fromString(String string) {
                    // По умолчанию можно вернуть null или попытаться найти среди items
                    return comboBoxRole.getItems()
                            .stream()
                            .filter(r -> r.getName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Загрузка стоп-слов и отрисовка в интерфейсе
     */
    private void loadRegularFromBackend() {
        try {
            RegularExpression[] regularExpressions = restTemplate.getForObject(baseUrl + "/regexps", RegularExpression[].class);
            if (regularExpressions == null) return;

            // Преобразуем в список
            List<RegularExpression> list = new ArrayList<>(Arrays.asList(regularExpressions));

            // Сортируем: если role=null, считаем её как пустую строку
            list.sort(Comparator.comparing(re ->
                    re.getRole() != null ? re.getRole().getName() : ""
            ));

            // Теперь добавляем в интерфейс в нужном порядке
            for (RegularExpression re : list) {
                addRegularToUI(re);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавление виджетов для конкретного стоп-слова
     */
    private void addRegularToUI(RegularExpression re) {
        // Горизонтальный контейнер
        HBox row = new HBox(10); // 10 – отступы между элементами
        row.setAlignment(Pos.CENTER_LEFT);

        // Текстовое поле со словом
        TextField textField = new TextField(re.getPattern());
        textField.setStyle("-fx-background-radius: 21px;"
                + "-fx-font-size: 20px;");
        textField.setEditable(false);

        // Текстовое поле для роли (либо Label, но TextField выглядит как на скриншоте)
        TextField roleField = new TextField(
                re.getRole() != null ? re.getRole().getName() : "Без роли"
        );
        roleField.setStyle("-fx-background-radius: 21px;"
                + "-fx-font-size: 20px;");
        roleField.setEditable(false);

        // Кнопка «Удалить»
        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #167DFF; "
                + "-fx-background-radius: 21px;");
        deleteButton.setTextFill(Color.WHITE);
        deleteButton.setFont(Font.font("System Bold", 14));
        deleteButton.setOnAction(event -> {
            deleteRegular(re.getId());
            container.getChildren().remove(row);
        });

        // Добавляем все в один HBox
        row.getChildren().addAll(textField, roleField, deleteButton);

        // Теперь добавляем строку (HBox) в VBox-контейнер
        container.getChildren().add(row);
    }

    private void deleteRegular(Long id) {
        try {
            restTemplate.delete(baseUrl + "/regexps" + "/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Кнопка «Добавить»
     */
    @FXML
    void addGo(ActionEvent event) {
        String value = inpCoiceFile.getText();
        if (value == null || value.isBlank()) {
            return;
        }

        // Создаём DTO для отправки
        RegularExpression regularExpression = new RegularExpression();
        regularExpression.setPattern(value);

        // Берём выбранную роль из ComboBox, если есть
        Role selectedRole = comboBoxRole.getValue();
        if (selectedRole != null) {
            regularExpression.setRole(selectedRole);
        }

        try {
            if (comboBoxRole.getValue() != null){
                // Отправляем POST на бэкенд
                RegularExpression created = restTemplate.postForObject(baseUrl + "/regexps", regularExpression, RegularExpression.class);
                if (created != null) {
                    addRegularToUI(created);
                }
                // Очищаем поле
                inpCoiceFile.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void exitGo(ActionEvent event) {
        container.getChildren().clear();
        transitiionButton.transition(exit, "/com/example/dsap1/AdminConsole.fxml", "Админ");
    }

    @FXML
    void inpCoiceFileGo(ActionEvent event) {
        // Доп. обработчик, если нужно
    }

    // DTO в клиенте
    public static class RegularExpression {
        private Long id;
        private Role role;
        private String pattern;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
    }
    public static class Role {
        private Long id;
        private String name;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
