package com.lunapps.services.payment.impl;

import com.lunapps.models.payment.PreparedPayment;
import com.lunapps.repository.payment.PreparedPaymentRepository;
import com.lunapps.services.payment.PreparedPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PreparedPaymentServiceImpl implements PreparedPaymentService {

    @Autowired
    private PreparedPaymentRepository preparedPaymentRepository;

    @Override
    @Transactional
    public PreparedPayment create(PreparedPayment preparedPayment) {

        preparedPayment.setCreatedDate(ZonedDateTime.now());
        preparedPayment.setUpdatedDate(ZonedDateTime.now());

        return preparedPaymentRepository.save(preparedPayment);
    }

    @Override
    public List<PreparedPayment> findAllByPaymentId(String paymentId) {
        return preparedPaymentRepository.findAllByPaymentId(paymentId);
    }

    @Override
    @Transactional
    public void delete(PreparedPayment preparedPayment) {
        preparedPaymentRepository.delete(preparedPayment);
    }
}
