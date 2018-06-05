package com.lunapps.controllers.dto.deals;

import com.lunapps.models.deal.DealBrakeStatus;
import lombok.Getter;

@Getter
public class DealBrakeDto {
    private String brakeDescription;
    private DealBrakeStatus dealBrakeStatus;
}