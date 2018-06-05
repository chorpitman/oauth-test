package com.lunapps.controllers.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentCreateDto {

    @NotNull
    @Digits(integer=5, fraction=2)
    @DecimalMin(value = "0.1", inclusive = true)
    private BigDecimal sum;

}
