package com.lunapps.repository.payment;

import com.lunapps.models.payment.PayoutRequest;
import com.lunapps.models.payment.PayoutRequestState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutRequestRepository extends JpaRepository<PayoutRequest, Long> {

    List<PayoutRequest> findAllByState(final PayoutRequestState payoutRequestState);

    List<PayoutRequest> findAllByIdIsIn(final List<Long> ids);

}
