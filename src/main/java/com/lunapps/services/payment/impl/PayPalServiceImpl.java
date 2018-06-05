package com.lunapps.services.payment.impl;

import com.lunapps.exception.PaymentException;
import com.lunapps.exception.user.UserException;
import com.lunapps.models.User;
import com.lunapps.models.payment.*;
import com.lunapps.repository.UserRepository;
import com.lunapps.security.Authentication;
import com.lunapps.services.UserUtilService;
import com.lunapps.services.payment.PayPalService;
import com.lunapps.services.payment.PaymentService;
import com.lunapps.services.payment.PreparedPaymentService;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;

import static java.lang.String.format;

@Service
public class PayPalServiceImpl implements PayPalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayPalServiceImpl.class);

    @Value("${payment.paypal.clientId}")
    private String clientId;
    @Value("${payment.paypal.clientSecret}")
    private String clientSecret;
    @Value("${payment.paypal.mode}")
    private String mode;
    @Value("${payment.paypal.currency}")
    private String currency;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PreparedPaymentService preparedPaymentService;
    @Autowired
    private Authentication authentication;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtilService userUtilService;

    @Override
    public PaymentApproval createPayment(BigDecimal sum, String redirectUrl) {

        String currentAuthUserEmail = authentication.getCurrentAuthUser();
        User foundUser = userRepository.findByEmail(currentAuthUserEmail);
        if (Objects.isNull(foundUser)) {
            throw new UserException(format("user with email: {0} does not exist", currentAuthUserEmail));
        }
        userUtilService.checkProfileFieldsFilling(foundUser);

        // Set payer details
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Set redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(redirectUrl + "/cancel");
        redirectUrls.setReturnUrl(redirectUrl + "/process");

        // Payment amount
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(sum.toString());

        // Transaction information
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("This is the payment for user account.");

        // Add transaction to a list
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Add payment details
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(transactions);

        // Create payment
        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, mode);
            Payment createdPayment = payment.create(apiContext);

            PreparedPayment preparedPayment = PreparedPayment.builder()
                    .sum(sum)
                    .currency(currency)
                    .user(foundUser)
                    .paymentId(createdPayment.getId())
                    .state(createdPayment.getState())
                    .build();
            preparedPaymentService.create(preparedPayment);

            Iterator<Links> links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return new PaymentApproval(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            LOGGER.error("Error creation payment ", e.getDetails());

            throw new PaymentException("Error creation payment");
        }

        return null;
    }

    @Override
    public PaymentResult processPayment(String paymentId, String payerId) {

        APIContext apiContext = new APIContext(clientId, clientSecret, mode);

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment createdPayment = payment.execute(apiContext, paymentExecution);

            LOGGER.info("Execute payment with paymentId: " + paymentId + " payerId: " + payerId + " state: " + createdPayment.getState());

            List<PreparedPayment> preparedPayments = preparedPaymentService.findAllByPaymentId(paymentId);
            if (preparedPayments.size() > 0) {
                //calculate net amount
                Transaction transaction = createdPayment.getTransactions().get(0);
                RelatedResources relatedResources = transaction.getRelatedResources().get(0);

                BigDecimal grossAmount = new BigDecimal(transaction.getAmount().getTotal());
                BigDecimal transactionFee = new BigDecimal(relatedResources.getSale().getTransactionFee().getValue());

                BigDecimal netAmount = grossAmount.subtract(transactionFee);

                PreparedPayment preparedPayment = preparedPayments.get(0);
                ExecutedPayment executedPayment = ExecutedPayment.builder()
                        .sum(netAmount).currency(currency)
                        .user(preparedPayment.getUser())
                        .paymentId(paymentId)
                        .payerId(payerId)
                        .state(createdPayment.getState())
                        .build();
                paymentService.create(executedPayment);
            }

            return new PaymentResult(createdPayment.getState());

        } catch (PayPalRESTException e) {
            LOGGER.error("Error execution payment ", e.getDetails());

            throw new PaymentException("Error execution payment");
        }
    }

    @Override
    public PayoutBatch payoutPayment(List<PayoutRequest> payoutRequests) {

        Payout payout = new Payout();

        PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();

        Random random = new Random();
        senderBatchHeader.setSenderBatchId(
                new Double(random.nextDouble()).toString()).setEmailSubject(
                "You have a Payment!");

        List<PayoutItem> items = new ArrayList<>();

        for (PayoutRequest payoutRequest : payoutRequests) {

            //Sum
            String sum = payoutRequest.getSum().setScale(2, RoundingMode.HALF_EVEN).toString();

            //Currency
            Currency amount = new Currency();
            amount.setValue(sum).setCurrency(currency);

            //Sender Item
            PayoutItem senderItem = new PayoutItem();
            senderItem.setRecipientType("Email")
                    .setNote("Thanks for your patronage")
                    .setReceiver(payoutRequest.getPaypalEmail())
                    //todo: setSenderItemId
                    .setSenderItemId(ZonedDateTime.now().toString()).setAmount(amount);

            items.add(senderItem);
        }

        payout.setSenderBatchHeader(senderBatchHeader).setItems(items);

        PayoutBatch batch = null;

        try {

            //Api Context
            APIContext apiContext = new APIContext(clientId, clientSecret, mode);

            //Create Batch Payout
            batch = payout.create(apiContext, new HashMap<>());

            LOGGER.info("Payout Batch With ID: " + batch.getBatchHeader().getPayoutBatchId());

        } catch (PayPalRESTException e) {
            LOGGER.error("Error Creation Payout Batch : ", e);

            throw new PaymentException("Error Creation Payout Batch : ", e);
        }

        return batch;
    }
}
