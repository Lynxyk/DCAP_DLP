package com.example.dsap1.Controllers.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
//восстановление пароля
@Component
@Lazy
public class PasswordResetService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.from.bd}")
    private String baseUrl;

    public String sendCode(String login, String newPassword) {
        String url = baseUrl + "/users/request-password-change";

        Map<String, String> body = new HashMap<>();
        body.put("login", login);
        body.put("newPassword", newPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            return (String) response.getBody().get("message");
        } catch (Exception e) {
            System.out.println("Ошибка отправки кода: " + e.getMessage());
            return "Ошибка отправки кода";
        }
    }

    public String confirmCode(String login, String code) {
        String url = baseUrl + "/users/confirm-password-change";

        Map<String, String> body = new HashMap<>();
        body.put("login", login);
        body.put("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            return (String) response.getBody().get("message");
        } catch (Exception e) {
            System.out.println("Ошибка подтверждения кода: " + e.getMessage());
            return "Ошибка подтверждения кода";
        }
    }
}
