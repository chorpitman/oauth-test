package com.lunapps.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private final static String regExp = "^.+@.+\\..+$";

    @NotNull
    @NotBlank
    @Email(message = "please provide a valid email address", regexp = regExp)
    @Size(message = "user email size must be between 5 and 100 characters", min = 5, max = 100)
    private String email;

    @NotNull
    @NotBlank(message = "Please enter your password")
    @Size(message = "user password lengths must be higher or equals 8", min = 8, max = 255)
    private String password;
}
