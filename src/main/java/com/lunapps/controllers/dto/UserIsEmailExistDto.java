package com.lunapps.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserIsEmailExistDto {
    private final static String regExp = "^.+@.+\\..+$";

    @Email(message = "please provide a valid email address", regexp = regExp)
    private String email;
}