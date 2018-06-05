package com.lunapps.exception.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoException extends RuntimeException {
    private String code;

    public PhotoException() {
        super();
    }

    public PhotoException(String message) {
        super(message);
    }

    public PhotoException(String message, String code) {
        super(message);
        this.code = code;
    }

    public PhotoException(Throwable cause) {
        super(cause);
    }

    public PhotoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhotoException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }
}
