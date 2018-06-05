package com.lunapps.services.payment.impl;

import com.lunapps.controllers.dto.payment.PayoutRequestBatchDto;
import com.lunapps.controllers.dto.payment.PayoutRequestCreateDto;
import com.lunapps.controllers.dto.payment.PayoutRequestDto;
import com.lunapps.convertor.payment.PayoutRequestDtoConverter;
import com.lunapps.exception.PayoutException;
import com.lunapps.exception.user.UserException;
import com.lunapps.models.User;
import com.lunapps.models.payment.PayoutRequest;
import com.lunapps.models.payment.PayoutRequestState;
import com.lunapps.models.payment.PayoutResult;
import com.lunapps.repository.UserRepository;
import com.lunapps.repository.payment.PayoutRequestRepository;
import com.lunapps.security.Authentication;
import com.lunapps.services.UserUtilService;
import com.lunapps.services.payment.PayPalService;
import com.lunapps.services.payment.PayoutRequestService;
import com.paypal.api.payments.PayoutBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
public class PayoutRequestServiceImpl implements PayoutRequestService {

    @Autowired
    private PayoutRequestRepository payoutRequestRepository;
    @Autowired
    private PayoutRequestDtoConverter payoutRequestDtoConverter;
    @Autowired
    private PayPalService payPalService;
    @Autowired
    private Authentication authentication;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtilService userUtilService;
    @Value("${payment.paypal.currency}")
    private String currency;

    @Override
    @Transactional
    public PayoutRequestDto create(PayoutRequestCreateDto createDto) {

        String currentAuthUserEmail = authentication.getCurrentAuthUser();
        User foundUser = userRepository.findByEmail(currentAuthUserEmail);
        if (Objects.isNull(foundUser)) {
            throw new UserException(format("user with email: {0} does not exist", currentAuthUserEmail));
        }
        userUtilService.checkProfileFieldsFilling(foundUser);

        PayoutRequest payoutRequest = payoutRequestDtoConverter.convert(createDto);
        payoutRequest.setCurrency(currency);
        payoutRequest.setUser(foundUser);
        payoutRequest.setState(PayoutRequestState.NEW);
        payoutRequest.setCreatedDate(ZonedDateTime.now());
        payoutRequest.setUpdatedDate(ZonedDateTime.now());

        //check current available user balance
        BigDecimal balance = foundUser.getBalance() != null ? foundUser.getBalance() : new BigDecimal(0);
        if(balance.compareTo(payoutRequest.getSum()) < 0) {
            throw new PayoutException("Requested sum more then balance");
        }

        User user = payoutRequest.getUser();
        user.setBalance(user.getBalance().subtract(payoutRequest.getSum()));
        userRepository.save(user);

        payoutRequestRepository.save(payoutRequest);

        PayoutRequestDto payoutRequestDto = payoutRequestDtoConverter.convert(payoutRequest);

        return payoutRequestDto;
    }

    @Override
    public List<PayoutRequestDto> findAll() {

        List<PayoutRequest> payoutRequests =  payoutRequestRepository.findAllByState(PayoutRequestState.NEW);

        List<PayoutRequestDto> payoutRequestDtoList = payoutRequestDtoConverter.convert(payoutRequests);

        return payoutRequestDtoList;
    }

    @Override
    @Transactional
    public PayoutResult executeBatchPayout(PayoutRequestBatchDto batchDto) {

        List<PayoutRequest> payoutRequests =  payoutRequestRepository.findAllByIdIsIn(batchDto.getPayoutRequests());

        PayoutBatch payoutBatch = payPalService.payoutPayment(payoutRequests);

        changePayoutStatus(batchDto);

        return  new PayoutResult(payoutBatch.getBatchHeader().getBatchStatus());
    }

    @Transactional
    public void changePayoutStatus(PayoutRequestBatchDto batchDto) {

        List<PayoutRequest> payoutRequests =  payoutRequestRepository.findAllByIdIsIn(batchDto.getPayoutRequests());

        for(PayoutRequest payoutRequest : payoutRequests) {
            payoutRequest.setState(PayoutRequestState.DONE);
            payoutRequestRepository.save(payoutRequest);
        }
    }
}
