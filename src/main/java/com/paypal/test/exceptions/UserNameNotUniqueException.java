package com.paypal.test.exceptions;

/**
 * Created by rahulaw on 28/01/17.
 */
public class UserNameNotUniqueException extends Exception {
    public UserNameNotUniqueException(String msg) {
        super(msg);
    }

    public UserNameNotUniqueException(String msg, Exception e) {
        super(msg, e);
    }
}
