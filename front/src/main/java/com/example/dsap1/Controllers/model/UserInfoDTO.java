package com.example.dsap1.Controllers.model;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

//модель объекта для авторизации
@Component
@Lazy
public class UserInfoDTO {
    private static String fullName;
    private static String roleName;
    private static Long roleId;

    public String getFullName() {
        return fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return fullName + " (" + roleName + ")";
    }

    public void setFullName(String fullName) {
        UserInfoDTO.fullName = fullName;
    }

    public void setRoleName(String roleName) {
        UserInfoDTO.roleName = roleName;
    }

    public void setRoleId(Long roleId) {
        UserInfoDTO.roleId = roleId;
    }
}