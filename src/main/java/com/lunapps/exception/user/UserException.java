package com.lunapps.exception.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserException extends RuntimeException {
    private String code;

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, String code) {
        super(message);
        this.code = code;
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }
}
