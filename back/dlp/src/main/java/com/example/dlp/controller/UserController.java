package com.example.dlp.controller;

import com.example.dlp.model.User;
import com.example.dlp.model.UserInfoDTO;
import com.example.dlp.model.emailDTO.PasswordChangeConfirmation;
import com.example.dlp.model.emailDTO.PasswordChangeRequest;
import com.example.dlp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping public List<User> all() { return service.findAll(); }
    @GetMapping("/{id}") public User get(@PathVariable Long id) { return service.findById(id); }
    @GetMapping("/by-login/{login}") public User getByLogin(@PathVariable String login) { return service.findByLogin(login); }
    @PostMapping public User create(@RequestBody User u) { return service.save(u); }
    @PutMapping("/{id}") public User update(@PathVariable Long id, @RequestBody User u) { return service.update(id, u); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
    @PostMapping("/check-credentials")
    public ResponseEntity<?> checkCredentials(@RequestParam String login, @RequestParam String password) {
        Optional<UserInfoDTO> userInfo = service.getUserInfoIfCredentialsValid(login, password);
        return userInfo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    @PostMapping("/request-password-change")
    public ResponseEntity<?> requestPasswordChange(@RequestBody PasswordChangeRequest request) {
        try {
            service.sendVerificationCode(request.getLogin(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Код отправлен на почту"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }

    @PostMapping("/confirm-password-change")
    public ResponseEntity<?> confirmPasswordChange(@RequestBody PasswordChangeConfirmation request) {
        if (service.confirmPasswordChange(request.getLogin(), request.getCode())) {
            return ResponseEntity.ok(Map.of("message", "Пароль успешно изменён"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный или просроченный код");
        }
    }


}
