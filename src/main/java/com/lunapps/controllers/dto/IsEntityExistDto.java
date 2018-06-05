package com.lunapps.controllers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IsEntityExistDto {
    private Boolean isEntityExist;
}