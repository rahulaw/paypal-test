package com.paypal.test.api;

import com.google.inject.Inject;
import com.paypal.test.dao.AccountDAO;
import com.paypal.test.dao.UserDAO;
import com.paypal.test.entities.User;
import com.paypal.test.exceptions.EmailIdNotValidException;
import com.paypal.test.models.CreateUserRequest;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.NotAuthorizedException;
import java.util.List;

/**
 * Created by rahulaw on 28/01/17.
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserAPI {

    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final TokenAPI tokenAPI;

    public User authenticate(String userName, String password) {
        User user = userDAO.validateUser(userName,password);
        if(user == null) {
            throw new NotAuthorizedException("User / password is incorrect");
        }
        return user;
    }

    public String issueToken(long userId) {
        return tokenAPI.issueToken(userId);
    }

    public List<User> getAllUserInfo(int limit, int offset) {
        return userDAO.getAllUsers(limit, offset);
    }

    public User createUser(CreateUserRequest createUserRequest) {
//        checkValidEmail(createUserRequest.getEmail());
        checkUniqueNickName(createUserRequest.getNickName());
//        checkUniqueEmail(createUserRequest.getEmail());
        userDAO.createNewUser(createUserRequest.getNickName(), createUserRequest.getEmail(), createUserRequest.getPassword());
        User user = userDAO.getUserByEmail(createUserRequest.getEmail());
        accountDAO.createAccount(user.getId(),100.0);
        return user ;
    }

    private void checkUniqueEmail(String email) throws EmailIdNotValidException {


    }

    private void checkUniqueNickName(String nickName) {

    }

    private void checkValidEmail(String email) throws EmailIdNotValidException {
        throw new EmailIdNotValidException("email id is not valid");
    }
}
