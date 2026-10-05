package com.flux.server.controller;

import com.flux.server.entity.ContenedorMaritimo;
import com.flux.server.repository.ContenedorMaritimoRepository;
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
import io.jettra.rest.annotations.accreditation.RolesAllowed;
import io.jettra.rest.core.Response;
import io.jettra.server.discoverer.Discovered;
import io.jettra.server.openapi.annotations.OpenApi;
import io.jettra.server.openapi.annotations.Operation;
import java.util.List;
import jcf.systemRole;

@Secured
@Path("/plugin/demo/contenedormaritimo")
//@DeclareRoles({"ADMIN", "MANAGER"})
@RolesAllowed({systemRole.ADMIN})
@Discovered
@OpenApi(title = "Contenedor maritimo", version = "v1.0", description = "API for Library management")
public class ContenedorMaritimoController {

    @Inject
    ContenedorMaritimoRepository contenedirMaritimoRepository;

    @GET
    @Path("/")
    @Produces("application/json")
    @Operation(summary = "findAll", description = "Returns all records")
//    @PermitAll
    public List<ContenedorMaritimo> findAll() {
        IO.print("findAll() " + contenedirMaritimoRepository.findAll());
        return contenedirMaritimoRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "save", description = "Saves a new ContenedorMaritimo")
    public Response save(ContenedorMaritimo contenedirMaritimo) {
        contenedirMaritimoRepository.save(contenedirMaritimo);
        return Response.ok("{\"message\": \"Saved successfully\"}").build();
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "update", description = "Updates an existing ContenedorMaritimo")
    public Response update(ContenedorMaritimo contenedirMaritimo) {
        contenedirMaritimoRepository.save(contenedirMaritimo);
        return Response.ok("{\"message\": \"Updated successfully\"}").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    @Operation(summary = "delete", description = "Deletes a ContenedorMaritimo by id")
    public Response delete(@PathParam("id") String id) {
        contenedirMaritimoRepository.delete(id);
        return Response.ok("{\"message\": \"Deleted successfully\"}").build();
    }
}
