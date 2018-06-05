package com.lunapps.controllers.dto;

import lombok.Getter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class RatingDto {
    @NotNull
    private Long dealId;

    private String comment;

    @NotNull
    @DecimalMin("1")
    @DecimalMax("5")
    private Long rating;
}
