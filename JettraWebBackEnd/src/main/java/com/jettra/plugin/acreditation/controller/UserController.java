package com.jettra.plugin.acreditation.controller;

import com.jettra.plugin.acreditation.entity.User;
import com.jettra.plugin.acreditation.repository.UserRepository;
import io.jettra.core.inject.annotation.Inject;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.PUT;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.annotations.Secured;
import io.jettra.rest.annotations.accreditation.DeclareRoles;
import io.jettra.rest.annotations.accreditation.RolesAllowed;
import io.jettra.rest.core.Response;
import io.jettra.server.discoverer.Discovered;
import java.util.List;

@Secured
@Path("/plugin/accreditation/users")
@DeclareRoles({"ADMIN", "MANAGER"})
@RolesAllowed({"ADMIN"})
@Discovered
public class UserController {

    @Inject
    UserRepository userRepository;
    @GET
    @Produces("application/json")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(User user) {
        userRepository.save(user);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        userRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
