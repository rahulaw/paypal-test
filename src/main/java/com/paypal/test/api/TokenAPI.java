package com.paypal.test.api;

import com.google.inject.Inject;
import com.paypal.test.dao.TokenDAO;
import com.paypal.test.UserConfiguration;
import com.paypal.test.entities.Tokens;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rahulaw on 28/01/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TokenAPI {

    private final TokenDAO tokenDAO;
    private final UserConfiguration userConfiguration;


    public String issueToken(long userId) {
        String token = UUID.randomUUID().toString();
        Date expiryDate = DateUtils.addHours(new Date(), userConfiguration.getTokenExpiryHours());
        //This function will take care of create / update.
        tokenDAO.storeToken(userId, token, expiryDate.getTime());
        return token;
    }

    public boolean validateToken(String token) {
        boolean isValidToken = false ;
        Tokens tokens = tokenDAO.getToken(token);
        if(tokens != null && (tokens.getExpiryTime() - new Date().getTime() > 0)) {
            isValidToken = true ;
        }
        return isValidToken;
    }

    public void disableToken(String token) {
        tokenDAO.deleteToken(token);
    }
}
