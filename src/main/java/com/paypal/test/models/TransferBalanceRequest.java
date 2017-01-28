package com.paypal.test.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by rahulaw on 29/01/17.
 */
@Data
@NoArgsConstructor
public class TransferBalanceRequest {
    @JsonProperty("user_email")
    String userEmail ;
    double amount ;
}
