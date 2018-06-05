package com.lunapps.controllers.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class PayoutRequestBatchDto {

    @NotNull
    private List<Long> payoutRequests;

}
