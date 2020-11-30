package com.authin.iam.api.validation;

public class TokenValidationException extends Exception {
    public TokenValidationException(String message) {
        super(message);
    }

    public TokenValidationException(Throwable cause) {
        super(cause);
    }
}
