package com.lunapps.exception.user;

/**
 * Created by olegchorpita on 7/13/17.
 */
public class UserEmailAlreadyExistsException extends RuntimeException {
    public UserEmailAlreadyExistsException() {
        super();
    }

    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UserEmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public UserEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
