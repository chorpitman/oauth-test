package com.lunapps.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealException extends RuntimeException {
    private String code;

    public DealException() {
        super();
    }

    public DealException(String message) {
        super(message);
    }

    public DealException(String message, String code) {
        super(message);
        this.code = code;
    }

    public DealException(Throwable cause) {
        super(cause);
    }

    public DealException(String message, Throwable cause) {
        super(message, cause);
    }

    public DealException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }
}
