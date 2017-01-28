package com.paypal.test.models;

import lombok.*;

/**
 * Created by rahulaw on 28/01/17.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AccountDetailResponse {
    double amount;
    long userId;
}
