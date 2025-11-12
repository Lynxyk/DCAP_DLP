package com.example.dsap1.Controllers.model;

public class TextCheckRequest {
    private String text;
    private Long roleId;
    private String extension;

    // Геттеры и сеттеры
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }

    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }
}
