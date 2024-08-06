package com.project.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class LoginResponse {
    private String token;
    private Date creation;
    private Date expiration;
}
