package com.paypal.test.filters;

import com.google.inject.Inject;
import com.paypal.test.api.TokenAPI;
import com.paypal.test.models.ErrorResponse;
import com.paypal.test.annotations.Secured;
import lombok.RequiredArgsConstructor;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by rahulaw on 28/01/17.
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthenticationFilter implements ContainerRequestFilter {

    private final TokenAPI tokenAPI;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("token ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        String token = authorizationHeader.substring("token".length()).trim();
        boolean isValidToken = false ;
        try {
            isValidToken = tokenAPI.validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("error in validating token")).build());
        }
        if(!isValidToken) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("token is not valid")).build());
        }
    }

}