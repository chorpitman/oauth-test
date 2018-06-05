package com.lunapps.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by olegchorpita on 9/13/17.
 */
@Getter
@Setter
public class UserPasswordUpdateDto {
    @NotNull
    @Size(message = "user password lengths must be higher or equals 8", min = 8, max = 255)
    private String password;

    @NotNull
    @Size(message = "user password lengths must be higher or equals 8", min = 8, max = 255)
    private String newPassword;
}
