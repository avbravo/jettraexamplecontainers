package com.jettra.main;

import io.jettra.jwt.request.LoginRequest;
import io.jettra.jwt.request.LoginResponse;
import io.jettra.server.openapi.annotations.OpenApi;
import io.jettra.server.openapi.annotations.Operation;
import io.jettra.server.openapi.annotations.RequestBody;
import io.jettra.server.openapi.annotations.ApiResponse;
import io.jettra.jwt.JettraJWT;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PermitAll;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.core.Response;

import java.util.HashMap;

@OpenApi(title = "Authentication API", version = "v1.0", description = "API for Authentication")
@Path("/auth")
public class AuthController {

    private static final String JWT_SECRET = "default_secret_key_jettra_rest_2026";
    private static final long JWT_EXPIRATION = 3600000; // 1 hour

    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    @PermitAll
    @Operation(summary = "Login and get JWT", description = "Authenticates a user and returns a Bearer token")
    @ApiResponse(responseCode = "200", description = "Successful authentication")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public Response login(@RequestBody(description = "Login credentials", required = true) LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null 
            || request.getUsername().trim().isEmpty() || request.getPassword().trim().isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"Invalid credentials\"}").build();
        }

        // Basic validation for demo purposes
        // Any non-empty username/password will return a valid token
        JettraJWT jwt = new JettraJWT(JWT_SECRET, JWT_EXPIRATION);
        String token = "Bearer " + jwt.generateToken(new HashMap<>(), request.getUsername());

        return Response.ok(new LoginResponse(token)).build();
    }
}
