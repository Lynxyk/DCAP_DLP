package com.example.dlp.model;

// добавлено поле roleId
public class UserInfoDTO {
    private String fullName;
    private String roleName;
    private Long roleId;

    // добавлен параметр roleId в конструктор
    public UserInfoDTO(String fullName, String roleName, Long roleId) {
        this.fullName = fullName;
        this.roleName = roleName;
        this.roleId = roleId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    // добавлен getter для roleId
    public Long getRoleId() {
        return roleId;
    }
}
