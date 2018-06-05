package com.lunapps.models.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class UserAdminFilter {

    private String firstname;

    private String lastname;

    private Boolean isUserAccountActive;

    private BigDecimal balance;

    private ZonedDateTime loginDate;
}