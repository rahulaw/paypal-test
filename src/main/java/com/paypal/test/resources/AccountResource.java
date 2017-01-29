package com.paypal.test.resources;

import com.google.inject.Inject;
import com.paypal.test.annotations.Secured;
import com.paypal.test.api.AccountAPI;
import com.paypal.test.models.*;
import com.paypal.test.utils.TokenHelper;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * Created by rahulaw on 28/01/17.
 */
@Path("/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountResource {

    private final AccountAPI accountAPI;
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Secured
    @GET
    @Path("/balance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAccountDetails(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String token = TokenHelper.getTokenFromHeader(authHeader);
            AccountDetailResponse accountDetailResponse = accountAPI.getBalance(token);
            return Response.status(Response.Status.OK).entity(accountDetailResponse).build();
        } catch(Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Secured
    @GET
    @Path("/transactions")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTransactionDetails(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String token = TokenHelper.getTokenFromHeader(authHeader);
            TransactionResponse transactionResponse = accountAPI.getTransactions(token);
            return Response.status(Response.Status.OK).entity(transactionResponse).build();
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Secured
    @PUT
    @Path("/balance_transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferBalance(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, TransferBalanceRequest request) {
        try {
            Set<ConstraintViolation<TransferBalanceRequest>> violations = validator.validate(request);
            if (violations.size() > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Please enter mandatory fields")).build();
            }

            String token = TokenHelper.getTokenFromHeader(authHeader);
            AccountDetailResponse accountDetailResponse = accountAPI.transferBalance(token, request);
            return Response.status(Response.Status.OK).entity(accountDetailResponse).build();
        } catch(RuntimeException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ErrorResponse(e.getMessage())).build();
        } catch(Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        }
    }



}
