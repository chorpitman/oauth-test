package com.lunapps.controllers.dto.deals;

import com.lunapps.models.deal.DealInfo;
import com.lunapps.models.deal.DealType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Builder
@Setter
@Getter
public class DealDto {
    @NotNull
    private Long serviceId;
    @NotNull
    private Double proposePrice;
    //this field need for build dto and choose what actions a have to do.
    @Null
    //todo change name
    private Long laborerId;
    @Null
    //todo change name
    private Long homeyId;
    @Null
    private DealType dealType;
    @Null
    private DealInfo dealInfo;
}
