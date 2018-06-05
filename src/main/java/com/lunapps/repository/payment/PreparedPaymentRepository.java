package com.lunapps.repository.payment;

import com.lunapps.models.payment.PreparedPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreparedPaymentRepository extends JpaRepository<PreparedPayment, Long> {

    List<PreparedPayment> findAllByPaymentId(final String paymentId);

}
