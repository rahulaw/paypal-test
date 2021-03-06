package com.paypal.test.api;

import com.google.inject.Inject;
import com.paypal.test.UserConfiguration;
import com.paypal.test.dao.AccountDAO;
import com.paypal.test.dao.UserDAO;
import com.paypal.test.entities.User;
import com.paypal.test.exceptions.EmailIdNotUniqueException;
import com.paypal.test.exceptions.EmailIdNotValidException;
import com.paypal.test.exceptions.UserNameNotUniqueException;
import com.paypal.test.models.CreateUserRequest;
import com.paypal.test.models.CreateUserResponse;
import com.paypal.test.models.UserAuthRequest;
import com.paypal.test.models.UserAuthResponse;
import lombok.RequiredArgsConstructor;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionIsolationLevel;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.NotAuthorizedException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by rahulaw on 28/01/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserAPI {

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final TokenAPI tokenAPI;
    private final UserConfiguration userConfiguration;
    private final DBI jdbi;
    private static final String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";

    public UserAuthResponse login(UserAuthRequest userAuthRequest) {
        String password = "";
        try {
            password = getMd5Password(userAuthRequest.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        User user = authenticate(userAuthRequest.getUserName(), password);
        String token = issueToken(user.getId());
        return new UserAuthResponse(token);
    }

    public List<User> getAllUserInfo(int limit, int offset) {
        return userDAO.getAllUsers(limit, offset);
    }

    public CreateUserResponse createNewUser(CreateUserRequest createUserRequest) {
        User user = createUser(createUserRequest);
        String token = issueToken(user.getId());
        return new CreateUserResponse(token, user.getUserName());
    }

    public void logout(String token) {
        tokenAPI.disableToken(token);
    }

    private String issueToken(long userId) {
        return tokenAPI.issueToken(userId);
    }

    private User authenticate(String userName, String password) {
        User user = userDAO.validateUser(userName, password);
        if(user == null) {
            throw new NotAuthorizedException("User / password is incorrect");
        }
        return user;
    }

    private User createUser(CreateUserRequest createUserRequest) {
        User user = null ;
        try {
            checkValidEmail(createUserRequest.getEmail());
            checkUniqueEmail(createUserRequest.getEmail());
            checkUniqueUserName(createUserRequest.getUserName());
            user = createUserAndAccount(createUserRequest);
        } catch(EmailIdNotValidException | EmailIdNotUniqueException | UserNameNotUniqueException | NoSuchAlgorithmException e1) {
            throw new RuntimeException(e1.getMessage());
        }
        return user ;
    }

    private User createUserAndAccount(CreateUserRequest createUserRequest) throws NoSuchAlgorithmException {
        User user = null;
        Handle handle = jdbi.open();
        try {
            if (handle == null) {
                throw new RuntimeException("Database Connection issue");
            }

            handle.setTransactionIsolation(TransactionIsolationLevel.READ_COMMITTED);
            handle.begin();

            UserDAO userDAO = handle.attach(UserDAO.class);
            AccountDAO accountDAO = handle.attach(AccountDAO.class);

            userDAO.createNewUser(createUserRequest.getUserName(), createUserRequest.getEmail(), getMd5Password(createUserRequest.getPassword()), createUserRequest.getIsAdmin());
            user = userDAO.getUserByEmail(createUserRequest.getEmail());
            accountDAO.createAccount(user.getId(), userConfiguration.getDefaultAccountBalance());

            handle.commit();
            handle.close();

        } catch (Exception e) {
            assert handle != null;
            handle.rollback();
            handle.close();
            throw new RuntimeException("Error in creating the user");
        }
        return user;
    }

    public String getMd5Password(String password) throws NoSuchAlgorithmException {
        String md5 = null;
        if(null == password) return null;
        try {
            password += salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes(), 0, password.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("Error in creating hash of password");
        }
        return md5;
    }

    private void checkUniqueEmail(String email) throws EmailIdNotUniqueException {
        User user = userDAO.getUserByEmail(email);
        if(user != null) throw new EmailIdNotUniqueException("email id already exists");
    }

    private void checkUniqueUserName(String userName) throws UserNameNotUniqueException {
        User user = userDAO.getUserByUsername(userName);
        if(user != null) throw new UserNameNotUniqueException("username already exists");
    }

    private void checkValidEmail(String email) throws EmailIdNotValidException {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            throw new EmailIdNotValidException("email id is not valid");
        }
    }

}
