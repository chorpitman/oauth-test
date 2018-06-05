package com.lunapps.services.payment.impl;

import com.lunapps.models.User;
import com.lunapps.models.payment.ExecutedPayment;
import com.lunapps.models.payment.Payment;
import com.lunapps.models.payment.PreparedPayment;
import com.lunapps.repository.UserRepository;
import com.lunapps.repository.payment.PaymentRepository;
import com.lunapps.repository.payment.PreparedPaymentRepository;
import com.lunapps.services.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PreparedPaymentRepository preparedPaymentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Payment create(Payment payment) {

        payment.setCreatedDate(ZonedDateTime.now());
        payment.setUpdatedDate(ZonedDateTime.now());

        List<PreparedPayment> preparedPayments
                = preparedPaymentRepository.findAllByPaymentId(payment.getPaymentId());
        for (PreparedPayment preparedPayment : preparedPayments) {
            preparedPaymentRepository.delete(preparedPayment);
        }
        //todo нужно это сделать через локальные транзакции
        User user = payment.getUser();
        user.setBalance(user.getBalance().add(payment.getSum()));
        userRepository.save(user);

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment create(ExecutedPayment executedPayment) {

        Payment payment = Payment.builder()
                .sum(executedPayment.getSum()).currency(executedPayment.getCurrency())
                .user(executedPayment.getUser())
                .paymentId(executedPayment.getPaymentId())
                .payerId(executedPayment.getPayerId())
                .state(executedPayment.getState())
                .build();

        return create(payment);
    }

    @Override
    public List<Payment> findAllByPaymentIdAndPayerId(String paymentId, String payerId) {
        return paymentRepository.findAllByPaymentIdAndPayerId(paymentId, payerId);
    }
}
