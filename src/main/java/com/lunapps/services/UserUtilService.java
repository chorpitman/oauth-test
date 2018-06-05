package com.lunapps.services;

import com.lunapps.models.User;

public interface UserUtilService {

    Boolean checkProfileFieldsFilling(final User foundUser);

    void nullCheck(final User user, final Long userId);

    void nullCheck(final User user, final String userEmail);

    void checkBalance(final User user, final Double aDouble, final Long percent);
}
