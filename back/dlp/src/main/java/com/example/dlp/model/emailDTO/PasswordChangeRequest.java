package com.example.dlp.model.emailDTO;

public class PasswordChangeRequest {
    private String login;
    private String newPassword;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}