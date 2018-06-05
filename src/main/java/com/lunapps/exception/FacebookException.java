package com.lunapps.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by olegchorpita on 7/19/17.
 */
@Getter
@Setter
public class FacebookException extends RuntimeException {

    public FacebookException() {
        super();
    }

    public FacebookException(String message) {
        super(message);
    }

    public FacebookException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacebookException(Throwable cause) {
        super(cause);
    }
}
