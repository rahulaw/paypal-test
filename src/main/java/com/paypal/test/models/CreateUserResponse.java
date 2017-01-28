package com.paypal.test.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by rahulaw on 28/01/17.
 */
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
public class CreateUserResponse implements Serializable {
    String token;
    String userName;
}
