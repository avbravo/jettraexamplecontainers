package com.jettra.plugin.company.controller;

import com.jettra.plugin.company.entity.Headquarters;
import com.jettra.plugin.company.repository.HeadquartersRepository;
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
import java.util.List;
import io.jettra.server.discoverer.Discovered;

@Discovered
@Secured

@Path("/plugin/company/headquarters")
@DeclareRoles({"ADMIN", "MANAGER"})
@RolesAllowed({"ADMIN"})
public class HeadquartersController {

    @Inject
    HeadquartersRepository headquartersRepository;
    @GET
    @Produces("application/json")
    public List<Headquarters> findAll() {
        return headquartersRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Headquarters company) {
       headquartersRepository.save(company);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    
@Path("/{id}")
    @Produces("application/json")
    public Response delete(
@PathParam("id") String id) {
       headquartersRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
