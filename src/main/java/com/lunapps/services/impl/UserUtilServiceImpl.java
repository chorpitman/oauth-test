package com.lunapps.services.impl;

import com.lunapps.exception.user.AdvertException;
import com.lunapps.exception.user.UserException;
import com.lunapps.models.User;
import com.lunapps.services.UserUtilService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static java.text.MessageFormat.format;

@Service
public class UserUtilServiceImpl implements UserUtilService {
    private final static String F_NAME = "firstname";
    private final static String L_NAME = "lastname";
    private final static String NICK_NAME = "nickname";
    private final static String EMAIL = "email";
    private final static String GENDER = "gender";
    private final static String AGE = "age";
    private final static String ADDRESS = "address";

    @Override
    public Boolean checkProfileFieldsFilling(final User foundUser) {
        if (Objects.isNull(foundUser.getFirstname())) {
            throw new UserException(format("user has empty field: {0} ", F_NAME));
        }
        if (Objects.isNull(foundUser.getLastname())) {
            throw new UserException(format("user has empty field: {0} ", L_NAME));
        }
        if (Objects.isNull(foundUser.getEmail())) {
            throw new UserException(format("user has empty field: {0} ", EMAIL));
        }
        if (Objects.isNull(foundUser.getAddress())) {
            throw new UserException(format("user has empty field: {0} ", ADDRESS));
        }
        if (Objects.isNull(foundUser.getNickname())) {
            throw new UserException(format("user has empty field: {0} ", NICK_NAME));
        }
        if (Objects.isNull(foundUser.getGender())) {
            throw new UserException(format("user has empty field: {0} ", GENDER));
        }
        if (Objects.isNull(foundUser.getAge())) {
            throw new UserException(format("user has empty field: {0} ", AGE));
        }
        return Boolean.TRUE;
    }

    @Override
    public void nullCheck(final User user, final Long userId) {
        if (Objects.isNull(user)) {
            throw new UserException(format("user with id: {0} does not exist", userId));
        }
    }

    @Override
    public void nullCheck(User user, String userEmail) {
        if (Objects.isNull(user)) {
            throw new UserException(format("user with email: {0} does not exist", userEmail));
        }
    }

    @Override
    public void checkBalance(final User user, final Double advertReward, final Long percent) {
        BigDecimal currentUserBalance = user.getBalance();
        BigDecimal expectedUserBalance = BigDecimal.valueOf(advertReward * percent / 100);

        if (currentUserBalance.compareTo(expectedUserBalance) < 0) {
            throw new AdvertException(format("user with id: {0} has to refill balance ", user.getId().toString()),
                    expectedUserBalance.subtract(currentUserBalance).toString());
        }
    }
}
