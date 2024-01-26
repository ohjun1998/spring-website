package com.mysite.sbb.user;

import lombok.Getter;


@Getter
public enum UserRole {// 사용자 인증 후 사용자에게 부여할 권한
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
