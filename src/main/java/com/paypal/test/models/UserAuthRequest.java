package com.paypal.test.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by rahulaw on 28/01/17.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequest {
    @JsonProperty("user_name")
    @NotEmpty
    String userName ;
    @NotEmpty
    String password ;
}
