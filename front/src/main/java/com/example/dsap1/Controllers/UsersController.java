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

/**
 * Контроллер для управления пользователями
 * (Добавить / Отобразить / Редактировать / Удалить).
 *  - В каждой "строке" есть: login, password, fullName (TextField), role (ComboBox),
 *    и 2 кнопки: [Редактировать], [Удалить].
 */
@Component
@Lazy
public class UsersController {

    @FXML
    private TextField loginfield;       // ввод нового логина
    @FXML
    private TextField passwordfield;    // ввод нового пароля
    @FXML
    private TextField fullnamefield;    // ввод нового ФИО
    @FXML
    private ComboBox<Role> comboBoxRole;// выбор роли при добавлении

    @FXML
    private Button addUserbutton;
    @FXML
    private Button exitbutton;
    @FXML
    private ScrollPane scrollPane;

    @FXML private AnchorPane anchorPane;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private VBox container;
    @Autowired
    TransitiionButton transitiionButton;
    @Value("${url.from.bd}")
    private String baseUrl;

    // Список доступных ролей (подгружаем один раз)
    private List<Role> allRoles = new ArrayList<>();

    @FXML
    void initialize() {
        anchorPane.setStyle("-fx-background-image: url('" + getClass().getResource("/image/dlp1.png") + "');");
        container.setPadding(new Insets(4));
        scrollPane.setContent(container);
        container.setSpacing(4);

        // Загрузить роли в allRoles, заполнить comboBoxRole
        loadRolesFromBackend();

        // Загрузить и отрисовать пользователей
        loadUsersFromBackend();
        comboBoxRole.setStyle(
                "-fx-font-size: 20px;"
                        + "-fx-background-color: white;"
                        + "-fx-text-fill: black;"
                        + "-fx-background-radius: 21px;"
        );

    }

    /**
     * Загрузка всех ролей (GET /roles), сохраняем в allRoles, заполняем comboBoxRole.
     */
    private void loadRolesFromBackend() {
        try {
            Role[] rolesArr = restTemplate.getForObject(baseUrl + "/roles", Role[].class);
            if (rolesArr != null) {
                allRoles = new ArrayList<>(Arrays.asList(rolesArr));
                allRoles.sort(Comparator.comparing(Role::getName));
                comboBoxRole.getItems().addAll(allRoles);
            }

            // Чтобы в "поле" ComboBox показывалось имя роли
            comboBoxRole.setConverter(new StringConverter<>() {
                @Override
                public String toString(Role role) {
                    return (role == null) ? "" : role.getName();
                }

                @Override
                public Role fromString(String string) {
                    return comboBoxRole.getItems()
                            .stream()
                            .filter(r -> r.getName().equalsIgnoreCase(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Загрузка всех пользователей (GET /users), сортируем по названию роли
     * (или по логину — на ваш выбор), и добавляем строки в container.
     */
    private void loadUsersFromBackend() {
        try {
            User[] usersArr = restTemplate.getForObject(baseUrl + "/users", User[].class);
            if (usersArr == null) return;
            List<User> userList = new ArrayList<>(Arrays.asList(usersArr));
            // Сортируем по роли (null-роль пойдёт первой)
            userList.sort(Comparator.comparing(u ->
                    (u.getRole() != null) ? u.getRole().getName() : ""
            ));

            for (User u : userList) {
                addUserRow(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет одну "строку" (HBox) для пользователя:
     *  - TextField login,
     *  - TextField password,
     *  - TextField fullname,
     *  - ComboBox<Role> (для изменения роли),
     *  - 2 кнопки: "Редактировать" и "Удалить".
     */
    private void addUserRow(User user) {
        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER_LEFT);

        // Поля
        TextField loginField   = createTextField(user.getLogin());
        TextField passwordField= createTextField(user.getPassword());
        TextField fullNameField= createTextField(user.getFullName());

        // ComboBox для роли — чтобы можно было поменять
        ComboBox<Role> roleCombo = new ComboBox<>();
        roleCombo.setPrefWidth(120);
        roleCombo.setStyle("-fx-font-size: 16px;"
                + "-fx-background-color: white;"
                + "-fx-text-fill: black;"
                + "-fx-background-radius: 21px;");
        // Заполним тем же списком ролей
        roleCombo.getItems().addAll(allRoles);

        // Настроим converter
        roleCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Role r) {
                return (r == null) ? "" : r.getName();
            }

            @Override
            public Role fromString(String string) {
                return roleCombo.getItems()
                        .stream()
                        .filter(r -> r.getName().equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Если у пользователя есть роль, выставляем
        if (user.getRole() != null) {
            // ищем совпадение по id или по имени
            Role found = allRoles.stream()
                    .filter(r -> r.getId().equals(user.getRole().getId()))
                    .findFirst()
                    .orElse(null);
            roleCombo.setValue(found);
        }

        // Кнопка "Редактировать" (сохраняет все изменения разом)
        Button editBtn = new Button("Редактировать");
        editBtn.setStyle("-fx-background-color: #167DFF; -fx-background-radius: 12px;");
        editBtn.setTextFill(Color.WHITE);
        editBtn.setFont(Font.font("System Bold", 12));

        editBtn.setOnAction(e -> {
            // Считываем новые значения
            String newLogin = loginField.getText();
            String newPass  = passwordField.getText();
            String newFio   = fullNameField.getText();
            Role newRole    = roleCombo.getValue();  // может быть null

            // Можно проверить, что поля не пустые, если нужно
            // Например: if (newLogin.isBlank() || newPass.isBlank() || newFio.isBlank() || newRole == null) return;

            user.setLogin(newLogin);
            user.setPassword(newPass);
            user.setFullName(newFio);
            user.setRole(newRole);

            updateUser(user); // PUT /users/{id}
        });

        // Кнопка "Удалить" (удаляет всего пользователя)
        Button deleteBtn = new Button("Удалить");
        deleteBtn.setStyle("-fx-background-color: #FF5555; -fx-background-radius: 12px;");
        deleteBtn.setTextFill(Color.WHITE);
        deleteBtn.setFont(Font.font("System Bold", 12));

        deleteBtn.setOnAction(e -> {
            deleteUser(user.getId());
            container.getChildren().remove(row); // убираем из UI
        });

        // Складываем всё в строку
        row.getChildren().addAll(
                loginField, passwordField, fullNameField, roleCombo,
                editBtn, deleteBtn
        );
        container.getChildren().add(row);
    }

    /**
     * Создаём TextField с базовыми настройками
     */
    private TextField createTextField(String text) {
        TextField tf = new TextField(text);
        tf.setPrefWidth(200);
        tf.setFont(Font.font(16));
        tf.setStyle("-fx-background-radius: 21px;"
                + "-fx-font-size: 16px;");
        return tf;
    }

    /** PUT /users/{id} */
    private void updateUser(User user) {
        try {
            restTemplate.put(baseUrl + "/users" + "/" + user.getId(), user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** DELETE /users/{id} */
    private void deleteUser(Long userId) {
        try {
            restTemplate.delete(baseUrl + "/users" + "/" + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавление нового пользователя (по нажатию «Добавить»).
     * Проверяем, что логин/пароль/ФИО/роль не пусты.
     */
    @FXML
    void addUserGo(ActionEvent event) {
        String l = loginfield.getText();
        String p = passwordfield.getText();
        String f = fullnamefield.getText();
        Role r   = comboBoxRole.getValue();

        // Проверяем, что поля заполнены
        if (l.isBlank() || p.isBlank() || f.isBlank() || r == null) {
            return; // или можно вывести Alert
        }

        User newUser = new User();
        newUser.setLogin(l);
        newUser.setPassword(p);
        newUser.setFullName(f);
        newUser.setRole(r);

        try {
            User created = restTemplate.postForObject(baseUrl + "/users", newUser, User.class);
            if (created != null) {
                // Добавляем строку (HBox) в интерфейс
                addUserRow(created);
            }
            // Очищаем поля
            loginfield.clear();
            passwordfield.clear();
            fullnamefield.clear();
            comboBoxRole.setValue(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Кнопка «Выйти»
     */
    @FXML
    void exitGo(ActionEvent event) {
        // Очищаем UI-контейнер с пользователями
        container.getChildren().clear();

        // Очищаем comboBox (список ролей)
        comboBoxRole.getItems().clear();
        comboBoxRole.setValue(null);
        comboBoxRole.setConverter(null);

        // Очищаем поля ввода
        loginfield.clear();
        passwordfield.clear();
        fullnamefield.clear();

        // Очищаем список ролей
        if (allRoles != null) {
            allRoles.clear();
        }

        // Обнуляем ссылки, чтобы помочь GC (сборщику мусора)
        scrollPane.setContent(null);

        // Переход на другую сцену
        transitiionButton.transition(exitbutton, "/com/example/dsap1/AdminConsole.fxml", "Админ");
    }


    // DTO классы
    public static class User {
        private Long id;
        private String login;
        private String password;
        private String fullName;
        private Role role;

        // геттеры/сеттеры...
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
    }

    public static class Role {
        private Long id;
        private String name;

        // геттеры/сеттеры...
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
