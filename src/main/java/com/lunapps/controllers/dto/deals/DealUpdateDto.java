package com.lunapps.controllers.dto.deals;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DealUpdateDto {
    private Long dealId;
    private Boolean isConfirmed;
    //for laborer
    private Double proposePrice;
}