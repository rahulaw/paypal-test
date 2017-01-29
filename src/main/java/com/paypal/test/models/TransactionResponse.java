package com.paypal.test.models;

import com.paypal.test.entities.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by rahulaw on 29/01/17.
 */
@Data
@AllArgsConstructor
public class TransactionResponse {
    List<TransactionHistory> transactionHistories;
}
