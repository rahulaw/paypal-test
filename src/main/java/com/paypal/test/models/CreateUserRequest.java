package com.paypal.test.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by rahulaw on 28/01/17.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserRequest {
    @JsonProperty("nick_name")
    String nickName;
    String email;
    String password;
}
