package com.mysite.sbb.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

import com.mysite.sbb.user.UserController;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) //값을 중복하여 저장할 수 없다.
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
    
    @Column(name = "failed_attempt")
    private Integer failedAttempt = 0;
    
    @Column(name = "is_locked")
    private Boolean isLocked = false;
}
