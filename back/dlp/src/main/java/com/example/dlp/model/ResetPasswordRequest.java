package com.example.dlp.model;

public class ResetPasswordRequest {
    private String login;
    private String newPassword;
    private String emailCode;

    public String getLogin() {
        return login;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmailCode() {
        return emailCode;
    }

}
