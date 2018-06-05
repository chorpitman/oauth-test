package com.lunapps.services.payment;

import com.lunapps.models.payment.PreparedPayment;

import java.util.List;

public interface PreparedPaymentService {

    PreparedPayment create(final PreparedPayment preparedPayment);

    List<PreparedPayment> findAllByPaymentId(final String paymentId);

    void delete(PreparedPayment preparedPayment);
}
