package com.example.dsap1.Controllers.Utils;

import com.example.dsap1.Controllers.model.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
//авторизация
@Component
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${url.from.bd}")
    private String baseUrl;

    public UserInfoDTO login(String login, String password) {
        // Собираем URL с параметрами
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/users/check-credentials")
                .queryParam("login", login)
                .queryParam("password", password)
                .toUriString();

        try {
            ResponseEntity<UserInfoDTO> response = restTemplate.postForEntity(url, null, UserInfoDTO.class);
            return response.getBody(); // вернёт объект с fullName и roleName
        } catch (Exception e) {
            System.out.println("Ошибка авторизации: " + e.getMessage());
            return null;
        }
    }
}
