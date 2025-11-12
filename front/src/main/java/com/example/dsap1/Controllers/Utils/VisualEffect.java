package com.example.dsap1.Controllers.Utils;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

@Component
public class VisualEffect {
    public void addButtonEffects(Button button) {
        // Наведение: тень + увеличение
        button.setOnMouseEntered(event -> {
            button.setEffect(new DropShadow(15, Color.WHITE));

            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.play();
        });

        // Уход курсора: убираем тень и возвращаем размер
        button.setOnMouseExited(event -> {
            button.setEffect(null);

            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
            scaleDown.setToX(1);
            scaleDown.setToY(1);
            scaleDown.play();
        });
    }
}
