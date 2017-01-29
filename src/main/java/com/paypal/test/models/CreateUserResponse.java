package com.paypal.test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by rahulaw on 28/01/17.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateUserResponse {
    String token;
    String userName;
}
