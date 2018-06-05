package com.lunapps.convertor.payment;

import com.lunapps.controllers.dto.payment.PayoutRequestCreateDto;
import com.lunapps.controllers.dto.payment.PayoutRequestDto;
import com.lunapps.models.payment.PayoutRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PayoutRequestDtoConverter {

    public PayoutRequest convert(final PayoutRequestCreateDto dto) {
        return PayoutRequest.builder()
                .sum(dto.getSum())
                .paypalEmail(dto.getPaypalEmail())
                .build();
    }

    public PayoutRequestDto convert(final PayoutRequest payout) {
        return PayoutRequestDto.builder()
                .id(payout.getId())
                .sum(payout.getSum())
                .paypalEmail(payout.getPaypalEmail())
                .state(payout.getState())
                .avatarUrl(payout.getUser().getAvatarUrl())
                .firstname(payout.getUser().getFirstname())
                .lastname((payout.getUser().getLastname()))
                .build();
    }

    public List<PayoutRequestDto> convert(final List<PayoutRequest> payoutRequests) {

        List<PayoutRequestDto> payoutRequestDtoList = new ArrayList<>(payoutRequests.size());
        for(PayoutRequest payoutRequest : payoutRequests) {
            payoutRequestDtoList.add(convert(payoutRequest));
        }

        return payoutRequestDtoList;
    }

}
