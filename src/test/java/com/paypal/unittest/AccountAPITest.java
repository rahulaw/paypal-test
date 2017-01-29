package com.paypal.unittest;

import com.paypal.test.UserConfiguration;
import com.paypal.test.api.AccountAPI;
import com.paypal.test.dao.AccountDAO;
import com.paypal.test.dao.TokenDAO;
import com.paypal.test.dao.TransactionHistoryDAO;
import com.paypal.test.dao.UserDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skife.jdbi.v2.DBI;

/**
 * Created by rahulaw on 29/01/17.
 */
public class AccountAPITest {

    AccountAPI accountAPI;

    @Mock
    AccountDAO accountDAO;
    @Mock
    TokenDAO tokenDAO;
    @Mock
    UserDAO userDAO;
    @Mock
    TransactionHistoryDAO transactionHistoryDAO;
    @Mock
    DBI jdbi;
    @Mock
    UserConfiguration userConfiguration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountAPI = new AccountAPI(accountDAO,tokenDAO,userDAO,transactionHistoryDAO,jdbi,userConfiguration);
    }

    @Test
    public void testGetBalance() {

    }

    @Test
    public void testGetTransactions() {

    }

    @Test
    public void testTransferBalance() {

    }
}