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
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionIsolationLevel;

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
    private final DBI jdbi;
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
        Account fromAccount = accountDAO.getAccountDetails(fromUser.getId());
        double balanceLeft = fromAccount.getAmount() - request.getAmount() ;

        if(balanceLeft <= userConfiguration.getMinAmountInAccount()) {
            throw new RuntimeException("Insufficient balance");
        }

        updateBalance(request, fromUser, fromAccount);
        return new AccountDetailResponse(balanceLeft);
    }

    private void updateBalance(TransferBalanceRequest request, User fromUser, Account fromAccount) {
        Handle handle = jdbi.open();
        try {
            if (handle == null) {
                throw new RuntimeException("Database Connection issue");
            }

            handle.setTransactionIsolation(TransactionIsolationLevel.READ_COMMITTED);
            handle.begin();

            AccountDAO accountDAO = handle.attach(AccountDAO.class);
            TransactionHistoryDAO transactionHistoryDAO = handle.attach(TransactionHistoryDAO.class);

            double balanceLeft = fromAccount.getAmount() - request.getAmount();
            Account destAccount = accountDAO.getAccountDetailsByEmail(request.getUserEmail());

            accountDAO.updateAmount(fromAccount.getId(), balanceLeft);
            accountDAO.updateAmount(destAccount.getId(), destAccount.getAmount() + request.getAmount());

            addHistory(transactionHistoryDAO, AccountAction.TRANSFERRED, fromUser.getEmail(), request.getUserEmail(), fromAccount.getId(), destAccount.getId(), request.getAmount());
            addHistory(transactionHistoryDAO, AccountAction.RECEIVED, fromUser.getEmail(), request.getUserEmail(), fromAccount.getId(), destAccount.getId(), request.getAmount());

            handle.commit();
            handle.close();

        } catch (Exception e) {
            assert handle != null;
            handle.rollback();
            handle.close();
            throw new RuntimeException("Error in updating the amount");
        }
    }

    private void addHistory(TransactionHistoryDAO transactionHistory, AccountAction action, String fromUser, String toUser, long fromAccountId, long toAccountId, double amount) {
        String message = "";
        switch (action) {
            case TRANSFERRED:
                message = AccountAction.TRANSFERRED.toString() + " amount " + amount + " to user = " + toUser ;
                transactionHistory.add(fromAccountId, message);
                break;
            case RECEIVED:
                message = AccountAction.RECEIVED.toString() + " amount " + amount + " from user = " + fromUser ;
                transactionHistory.add(toAccountId, message);
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
