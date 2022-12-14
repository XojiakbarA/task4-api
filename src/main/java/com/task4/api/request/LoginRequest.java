package com.task4.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @NotNull
    private String email;
    @NotNull
    private String password;

}
