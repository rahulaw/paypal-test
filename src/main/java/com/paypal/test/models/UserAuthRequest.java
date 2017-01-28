package com.paypal.test.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by rahulaw on 28/01/17.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequest {
    @JsonProperty("user_name")
    String userName ;
    String password ;
}
