package com.lunapps.exception.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertException extends RuntimeException {
    private String sum;

    public AdvertException() {
        super();
    }

    public AdvertException(String message) {
        super(message);
    }

    public AdvertException(String message, String sum) {
        super(message);
        this.sum = sum;
    }

    public AdvertException(Throwable cause) {
        super(cause);
    }

    public AdvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdvertException(String message, Throwable cause, String sum) {
        super(message, cause);
        this.sum = sum;
    }
}
