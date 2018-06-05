package com.lunapps.repository.payment;

import com.lunapps.models.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByPaymentIdAndPayerId(final String paymentId, final String payerId);

}
