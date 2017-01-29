package com.paypal.test.api;

import com.google.inject.Inject;
import com.paypal.test.UserConfiguration;
import com.paypal.test.dao.AccountDAO;
import com.paypal.test.dao.TokenDAO;
import com.paypal.test.dao.TransactionHistoryDAO;
import com.paypal.test.dao.UserDAO;
import com.paypal.test.entities.Account;
import com.paypal.test.entities.Tokens;
import com.paypal.test.entities.TransactionHistory;
import com.paypal.test.entities.User;
import com.paypal.test.enums.AccountAction;
import com.paypal.test.models.AccountDetailResponse;
import com.paypal.test.models.TransactionResponse;
import com.paypal.test.models.TransferBalanceRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by rahulaw on 29/01/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountAPI {

    private final AccountDAO accountDAO;
    private final TokenDAO tokenDAO;
    private final UserDAO userDAO;
    private final TransactionHistoryDAO transactionHistoryDAO;
    private final UserConfiguration userConfiguration;

    public AccountDetailResponse getBalance(String token) {
        Tokens tokens = tokenDAO.getToken(token);
        Account account = accountDAO.getAccountDetails(tokens.getUserId());
        return new AccountDetailResponse(account.getAmount()) ;
    }

    public TransactionResponse getTransactions(String token) {
        Tokens tokens = tokenDAO.getToken(token);
        final List<TransactionHistory> historyForUser = transactionHistoryDAO.getHistoryForUser(tokens.getUserId());
        return new TransactionResponse(historyForUser);
    }

    public AccountDetailResponse transferBalance(String token, TransferBalanceRequest request) {
        if(!isPresent(request.getUserEmail())) {
            throw new RuntimeException("email id is not present in database");
        }

        if(request.getAmount() == 0) {
            throw new RuntimeException("transfer amount can not be 0");
        }

        User fromUser = userDAO.getUserByToken(token);
        Account account = accountDAO.getAccountDetails(fromUser.getId());
        double balanceLeft = account.getAmount() - request.getAmount() ;

        if(balanceLeft <= userConfiguration.getMinAmountInAccount()) {
            throw new RuntimeException("Insufficient balance");
        }

        //Do this inside a transaction
        Account destAccount = accountDAO.getAccountDetailsByEmail(request.getUserEmail());
        accountDAO.updateAmount(account.getId(), balanceLeft);
        accountDAO.updateAmount(destAccount.getId(), destAccount.getAmount() + request.getAmount());
        addHistory(AccountAction.TRANSFERRED, fromUser.getEmail(), request.getUserEmail(), account.getId(), destAccount.getId(), request.getAmount());
        addHistory(AccountAction.RECEIVED , fromUser.getEmail(), request.getUserEmail(), account.getId(), destAccount.getId(), request.getAmount());

        return new AccountDetailResponse(balanceLeft);
    }

    private void addHistory(AccountAction action, String fromUser, String toUser, long fromAccountId, long toAccountId, double amount) {
        String message = "";
        switch (action) {
            case TRANSFERRED:
                message = AccountAction.TRANSFERRED.toString() + " amount " + amount + " to user = " + toUser ;
                transactionHistoryDAO.add(fromAccountId, message);
                break;
            case RECEIVED:
                message = AccountAction.RECEIVED.toString() + " amount " + amount + " from user = " + fromUser ;
                transactionHistoryDAO.add(toAccountId, message);
                break;
            default :
                break;
        }
    }

    private boolean isPresent(String userEmail) {
        User user = userDAO.getUserByEmail(userEmail);
        return user != null;
    }
}
