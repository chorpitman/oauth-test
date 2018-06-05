package com.lunapps.services.payment;

import com.lunapps.models.payment.ExecutedPayment;
import com.lunapps.models.payment.Payment;

import java.util.List;

public interface PaymentService {

    Payment create(final Payment payment);

    Payment create(ExecutedPayment executedPayment);

    List<Payment> findAllByPaymentIdAndPayerId(final String paymentId, final String payerId);

}
