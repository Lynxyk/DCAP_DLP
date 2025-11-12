package com.example.dlp.model.emailDTO;

public class PasswordChangeConfirmation {
    private String login;
    private String code;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
