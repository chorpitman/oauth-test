package com.lunapps.services.payment;

import com.lunapps.controllers.dto.payment.PayoutRequestBatchDto;
import com.lunapps.controllers.dto.payment.PayoutRequestCreateDto;
import com.lunapps.controllers.dto.payment.PayoutRequestDto;
import com.lunapps.models.payment.PayoutResult;

import java.util.List;

public interface PayoutRequestService {

    PayoutRequestDto create(final PayoutRequestCreateDto createDto);

    List<PayoutRequestDto> findAll();

    PayoutResult executeBatchPayout(PayoutRequestBatchDto batchDto);

}
