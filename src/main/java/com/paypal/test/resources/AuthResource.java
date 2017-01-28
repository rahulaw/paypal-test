package com.paypal.test.resources;

import com.google.inject.Inject;
import com.paypal.test.annotations.Secured;
import com.paypal.test.api.UserAPI;
import com.paypal.test.entities.User;
import com.paypal.test.models.UserAuthRequest;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rahulaw on 28/01/17.
 */
@Path("/v1/authentication")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthResource {

    private final UserAPI userAPI;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response loginUser(UserAuthRequest userAuthRequest) {
        try {
            checkNotNull(userAuthRequest, "request cannot be null");
            checkNotNull(userAuthRequest.getUserName(), "user name cannot be null");
            checkNotNull(userAuthRequest.getPassword(), "password cannot be null");

            User user = userAPI.authenticate(userAuthRequest.getUserName(), userAuthRequest.getPassword());
            //Should we issue a new token every time we get a login request ??
            String token = userAPI.issueToken(user.getId());
            return Response.ok(token).build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Response logoutUser() {
        try {
            return Response.ok("").build();
        } catch (NotAuthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
