package com.lunapps.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * Created by olegchorpita on 9/18/17.
 */
@Getter
@Setter
public class UserForgotPasswordDto {
    private final static String regExp = "^.+@.+\\..+$";

    @Email(message = "please provide a valid email address", regexp = regExp)
    private String email;
}
