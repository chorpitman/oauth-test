package com.lunapps.exception;

public class PayoutException extends RuntimeException {

    public PayoutException() {
        super();
    }

    public PayoutException(String message) {
        super(message);
    }

    public PayoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayoutException(Throwable cause) {
        super(cause);
    }

    protected PayoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
