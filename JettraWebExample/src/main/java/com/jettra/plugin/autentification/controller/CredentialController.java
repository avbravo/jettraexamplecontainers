package com.jettra.plugin.autentification.controller;


import com.jettra.plugin.autentification.entity.Credential;
import com.jettra.plugin.autentification.repository.CredentialRepository;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/credentials")
public class CredentialController {

    @GET
    @Produces("application/json")
    public List<Credential> findAll() {
        return CredentialRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Credential credential) {
        CredentialRepository.save(credential);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        CredentialRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
