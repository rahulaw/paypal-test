package com.paypal.test.exceptions;

/**
 * Created by rahulaw on 28/01/17.
 */
public class EmailIdNotValidException extends Exception {
    public EmailIdNotValidException(String msg) {
        super(msg);
    }

    public EmailIdNotValidException(String msg, Exception e) {
        super(msg, e);
    }
}
