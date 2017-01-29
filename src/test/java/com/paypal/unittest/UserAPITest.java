package com.paypal.unittest;

import com.paypal.test.UserConfiguration;
import com.paypal.test.api.TokenAPI;
import com.paypal.test.api.UserAPI;
import com.paypal.test.dao.AccountDAO;
import com.paypal.test.dao.UserDAO;
import com.paypal.test.entities.User;
import com.paypal.test.models.CreateUserRequest;
import com.paypal.test.models.UserAuthRequest;
import com.paypal.test.models.UserAuthResponse;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.NotAuthorizedException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by rahulaw on 29/01/17.
 */
public class UserAPITest {

    UserAPI userAPI;

    @Mock
    UserDAO userDAO;
    @Mock
    AccountDAO accountDAO;
    @Mock
    TokenAPI tokenAPI;
    @Mock
    UserConfiguration userConfiguration;
    @Mock
    DBI jdbi;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userAPI = new UserAPI(userDAO, accountDAO, tokenAPI, userConfiguration, jdbi);
    }

    @Test(expected=NotAuthorizedException.class)
    public void testLoginShouldThrowError() throws NoSuchAlgorithmException {
        UserAuthRequest userAuthRequest = createUserAuthRequest();
        when(userDAO.validateUser(userAuthRequest.getUserName(), userAPI.getMd5Password(userAuthRequest.getPassword()))).thenReturn(null);
        userAPI.login(userAuthRequest);
    }

    @Test
    public void testLogin() throws NoSuchAlgorithmException {
        UserAuthRequest userAuthRequest = createUserAuthRequest();
        String expectedToken = "token1";
        when(userDAO.validateUser(userAuthRequest.getUserName(), userAPI.getMd5Password(userAuthRequest.getPassword()))).thenReturn(createUser(1,"u1",false));
        when(tokenAPI.issueToken(1)).thenReturn(expectedToken);

        UserAuthResponse userAuthResponse = userAPI.login(userAuthRequest);
        Assert.assertEquals(expectedToken, userAuthResponse.getToken());
    }

    @Test
    public void testGetAllUserInfo() {
        List<User> users = new ArrayList<>();
        users.add(createUser(1,"u1",false));
        users.add(createUser(2,"u2",false));
        when(userDAO.getAllUsers(100,0)).thenReturn(users);
        final List<User> allUserInfo = userAPI.getAllUserInfo(100, 0);
        Assert.assertEquals(2, allUserInfo.size());
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testCreateNewUserWithInvalidEmail() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("email id is not valid");

        CreateUserRequest createUserRequest = createNewUserRequest("email",0);
        userAPI.createNewUser(createUserRequest);
    }

    @Test
    public void testCreateNewUserWithDuplicateEmail() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("email id already exists");

        when(userDAO.getUserByEmail("email@abc.com")).thenReturn(createUser(1,"u1",false));
        CreateUserRequest createUserRequest = createNewUserRequest("email@abc.com",0);

        userAPI.createNewUser(createUserRequest);
    }

    @Test
    public void testCreateNewUserWithDuplicateUsername() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("username already exists");

        when(userDAO.getUserByEmail("email@abc.com")).thenReturn(null);
        when(userDAO.getUserByUsername("u1")).thenReturn(createUser(1,"u1",false));

        CreateUserRequest createUserRequest = createNewUserRequest("email@abc.com",0);
        userAPI.createNewUser(createUserRequest);
    }

    @Test
    public void testCreateNewUser() {
        when(userDAO.getUserByEmail("email@abc.com")).thenReturn(null);
        when(userDAO.getUserByUsername("u1")).thenReturn(null);

        CreateUserRequest createUserRequest = createNewUserRequest("email@abc.com",0);
//        userAPI.createNewUser(createUserRequest);
    }

    @Test
    public void testLogout() {
        //should not throw any exception
        userAPI.logout("str-token");
    }

    private UserAuthRequest createUserAuthRequest() {
        return new UserAuthRequest("user-1","password-1");
    }

    private CreateUserRequest createNewUserRequest(String email,int isAdmin) {
        return new CreateUserRequest("u1",email,"pass",isAdmin);
    }

    private User createUser(long id,String name,boolean isAdmin) {
        return new User(id,name,"email",isAdmin,"pass");
    }
}