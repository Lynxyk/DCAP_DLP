package com.example.dlp.model.emailDTO;

public class VerificationData {
    private final String code;
    private final String encodedPassword;
    private final long expiresAt;

    public VerificationData(String code, String encodedPassword, long expiresAt) {
        this.code = code;
        this.encodedPassword = encodedPassword;
        this.expiresAt = expiresAt;
    }

    public String getCode() { return code; }
    public String getEncodedPassword() { return encodedPassword; }
    public long getExpiresAt() { return expiresAt; }
}
