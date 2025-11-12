package com.example.dsap1.Controllers.Utils;

import com.example.dsap1.Controllers.model.TextCheckRequest;
import com.example.dsap1.Controllers.model.TextCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TextCheckService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${url.from.bd}")
    private String baseUrl;

    public boolean checkText(TextCheckRequest request) {
        try {
            ResponseEntity<TextCheckResponse> response = restTemplate.postForEntity(
                    baseUrl + "/check/text",
                    request,
                    TextCheckResponse.class
            );
            return response.getBody() != null && response.getBody().isPassed();
        } catch (Exception e) {
            System.out.println("Ошибка при проверке текста: " + e.getMessage());
            return false;
        }
    }
}
