package com.bestbranch.geulboxapi.user.password.service.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class PasswordInitializationConfirmationRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String authNo;
}
