package com.paypal.test.exceptions;

/**
 * Created by rahulaw on 28/01/17.
 */
public class EmailIdNotUniqueException extends Exception {
    public EmailIdNotUniqueException(String msg) {
        super(msg);
    }

    public EmailIdNotUniqueException(String msg, Exception e) {
        super(msg, e);
    }
}
