package com.example.dlp.model.text;

public class TextCheckRequest {
    private String text;
    private Long roleId;
    private String extension;

    public String getText() { return text; }
    public Long getRoleId() { return roleId; }
    public String getExtension() { return extension; }

    public void setText(String text) { this.text = text; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public void setExtension(String extension) { this.extension = extension; }
}