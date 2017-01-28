package com.paypal.test.resources;

import com.google.inject.Inject;
import com.paypal.test.annotations.Secured;
import com.paypal.test.dao.AccountDAO;
import com.paypal.test.dao.TokenDAO;
import com.paypal.test.dao.UserDAO;
import com.paypal.test.entities.Account;
import com.paypal.test.entities.Tokens;
import com.paypal.test.entities.User;
import com.paypal.test.models.AccountDetailResponse;
import com.paypal.test.models.ErrorResponse;
import com.paypal.test.models.TransferBalanceRequest;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rahulaw on 28/01/17.
 */
@Path("/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountResource {

    private final TokenDAO tokenDAO;
    private final AccountDAO accountDAO;
    private final UserDAO userDAO;

    @Secured
    @GET
    @Path("/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAccountDetails(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring("token".length()).trim();
        Tokens tokens = tokenDAO.getToken(token);
        Account account = accountDAO.getAccountDetails(tokens.getUserId());
        return Response.status(Response.Status.OK).entity(new AccountDetailResponse(account.getAmount(), tokens.getUserId())).build();
    }

    @Secured
    @PUT
    @Path("/balance_transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferBalance(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, TransferBalanceRequest request) {
        String token = authHeader.substring("token".length()).trim();
        Tokens tokens = tokenDAO.getToken(token);

        Account account = accountDAO.getAccountDetails(tokens.getUserId());
        if(!isPresent(request.getUserEmail())) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ErrorResponse("email id is not present in database")).build();
        }
        Account destAccount = accountDAO.getAccountDetailsByEmail(request.getUserEmail());

        //check for insufficient funds
        accountDAO.updateAmount(account.getId(), account.getAmount() - request.getAmount());
        accountDAO.updateAmount(destAccount.getId(), destAccount.getAmount() + request.getAmount());

        return Response.status(Response.Status.OK).entity(new AccountDetailResponse(account.getAmount() - request.getAmount(), tokens.getUserId())).build();
    }

    private boolean isPresent(String userEmail) {
        User user = userDAO.getUserByEmail(userEmail);
        return user != null;
    }

}
