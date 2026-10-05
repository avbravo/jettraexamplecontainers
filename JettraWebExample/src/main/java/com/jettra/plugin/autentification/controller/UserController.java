package com.jettra.plugin.autentification.controller;


import com.jettra.plugin.autentification.entity.User;
import com.jettra.plugin.autentification.repository.UserRepository;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/users")
public class UserController {

    @GET
    @Produces("application/json")
    public List<User> findAll() {
        return UserRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(User user) {
        UserRepository.save(user);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        UserRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
