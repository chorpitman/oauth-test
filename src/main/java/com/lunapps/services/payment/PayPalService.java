package com.lunapps.services.payment;

import com.lunapps.models.payment.PaymentApproval;
import com.lunapps.models.payment.PaymentResult;
import com.lunapps.models.payment.PayoutRequest;
import com.paypal.api.payments.PayoutBatch;

import java.math.BigDecimal;
import java.util.List;

public interface PayPalService {

    PaymentApproval createPayment(BigDecimal sum, String redirectUrl);

    PaymentResult processPayment(String paymentId, String payerId);

    PayoutBatch payoutPayment(List<PayoutRequest> payoutRequests);
}
