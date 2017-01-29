package com.paypal.test.resources;

import com.google.inject.Inject;
import com.paypal.test.annotations.Secured;
import com.paypal.test.api.UserAPI;
import com.paypal.test.models.ErrorResponse;
import com.paypal.test.models.UserAuthRequest;
import com.paypal.test.models.UserAuthResponse;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rahulaw on 28/01/17.
 */
@Path("/v1/authentication")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthResource {

    private final UserAPI userAPI;
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response loginUser(UserAuthRequest userAuthRequest) {
        try {
            checkNotNull(userAuthRequest, "request cannot be null");
            Set<ConstraintViolation<UserAuthRequest>> violations = validator.validate(userAuthRequest);
            if (violations.size() > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Please enter mandatory fields")).build();
            }

            UserAuthResponse userAuthResponse = userAPI.login(userAuthRequest);
            return Response.ok(userAuthResponse).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("User / password is incorrect")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Response logoutUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            String token = TokenHelper.getTokenFromHeader(authHeader);
            userAPI.logout(token);
            return Response.ok(new UserAuthResponse()).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("User is already logged out")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

}
