package com.project.DTO;

import lombok.Data;

@Data
public class SignupRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String jobTitle;
    private Double salary;
    private String department;
}
