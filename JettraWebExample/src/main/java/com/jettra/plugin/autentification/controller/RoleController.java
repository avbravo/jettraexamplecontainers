package com.jettra.plugin.autentification.controller;

import com.jettra.plugin.autentification.entity.Role;
import com.jettra.plugin.autentification.repository.RoleRepository;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/roles")
public class RoleController {

    @GET
    @Produces("application/json")
    public List<Role> findAll() {
        return RoleRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Role role) {
        RoleRepository.save(role);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        RoleRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
