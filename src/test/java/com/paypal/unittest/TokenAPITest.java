package com.paypal.unittest;

import com.paypal.test.UserConfiguration;
import com.paypal.test.api.TokenAPI;
import com.paypal.test.dao.TokenDAO;
import com.paypal.test.entities.Tokens;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.mockito.Mockito.when;
/**
 * Created by rahulaw on 29/01/17.
 */
public class TokenAPITest {

    TokenAPI tokenAPI;

    @Mock
    TokenDAO tokenDAO;
    @Mock
    UserConfiguration userConfiguration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tokenAPI = new TokenAPI(tokenDAO,userConfiguration);
    }

    @Test
    public void testIssueToken() {
        final String s = tokenAPI.issueToken(1);
        Assert.assertNotNull(s);
    }

    private Tokens createNewToken(String expectedToken, long expiry) {
        return new Tokens(1,1,expectedToken,expiry);
    }

    @Test
    public void testValidateTokenWithoutToken() {
        final String token = "token-1";
        when(tokenDAO.getToken(token)).thenReturn(null);
        boolean isValid = tokenAPI.validateToken(token);
        Assert.assertEquals(false, isValid);
    }

    @Test
    public void testValidateTokenWithExpiredToken() {
        final String token = "token-1";
        Date expiryDate = DateUtils.addDays(new Date(), -1);
        when(tokenDAO.getToken(token)).thenReturn(createNewToken(token,expiryDate.getTime()));
        boolean isValid = tokenAPI.validateToken(token);
        Assert.assertEquals(false, isValid);
    }

    @Test
    public void testValidateToken() {
        final String token = "token-1";
        Date expiryDate = DateUtils.addDays(new Date(), 1);
        when(tokenDAO.getToken(token)).thenReturn(createNewToken(token,expiryDate.getTime()));
        boolean isValid = tokenAPI.validateToken(token);
        Assert.assertEquals(true, isValid);
    }

    @Test
    public void testDisableToken() {
        tokenAPI.disableToken("token-1");
    }
}