package com.paypal.test.resources;

import com.google.inject.Inject;
import com.paypal.test.annotations.Secured;
import com.paypal.test.api.UserAPI;
import com.paypal.test.entities.User;
import com.paypal.test.models.CreateUserRequest;
import com.paypal.test.models.CreateUserResponse;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rahulaw on 28/01/17.
 */
@Path("/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserResource {

    private final UserAPI userAPI;
    private static final int defaultLimit = 100;
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Secured
    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@QueryParam("limit") int limit , @QueryParam("offset") int offset) {
        if(limit == 0) limit = defaultLimit;
        List<User> users = new ArrayList<User>();
        try {
            users = userAPI.getAllUserInfo(limit, offset);
        } catch(Exception e) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserRequest createUserRequest){
        CreateUserResponse createUserResponse ;
        try {
            checkNotNull(createUserRequest, "request cannot be null");
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);
            if (violations.size() > 0) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            User user = userAPI.createUser(createUserRequest);
            String token = userAPI.issueToken(user.getId());
            createUserResponse = new CreateUserResponse(token, user.getNickName());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).entity(createUserResponse).build();
    }

}
