package com.lunapps.controllers.dto.payment;

import com.lunapps.models.payment.PayoutRequestState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PayoutRequestDto {

    private Long id;
    private BigDecimal sum;
    private String paypalEmail;
    private PayoutRequestState state;
    private String avatarUrl;
    private String firstname;
    private String lastname;

}
