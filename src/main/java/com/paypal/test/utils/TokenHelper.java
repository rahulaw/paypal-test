package com.paypal.test.utils;

/**
 * Created by rahulaw on 29/01/17.
 */
public class TokenHelper {

    public static String getTokenFromHeader(String authHeader) {
        return authHeader.substring("token".length()).trim();
    }
}
