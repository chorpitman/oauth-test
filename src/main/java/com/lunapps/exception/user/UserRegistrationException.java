package com.lunapps.exception.user;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by olegchorpita on 8/9/17.
 */
@Getter
@Setter
public class UserRegistrationException extends RuntimeException {
    private String code;

    public UserRegistrationException() {
        super();
    }

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public UserRegistrationException(Throwable cause) {
        super(cause);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRegistrationException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }
}
