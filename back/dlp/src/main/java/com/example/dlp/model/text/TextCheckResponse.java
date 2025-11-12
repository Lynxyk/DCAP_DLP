package com.example.dlp.model.text;

import java.util.List;

public class TextCheckResponse {
    private boolean passed;

    public TextCheckResponse(boolean passed) {
        this.passed = passed;
    }

    public boolean isPassed() { return passed; }
}
