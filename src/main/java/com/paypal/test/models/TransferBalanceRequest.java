package com.paypal.test.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by rahulaw on 29/01/17.
 */
@Data
@NoArgsConstructor
public class TransferBalanceRequest {
    @JsonProperty("user_email")
    @NotEmpty
    String userEmail ;
    @NotNull
    double amount ;
}
