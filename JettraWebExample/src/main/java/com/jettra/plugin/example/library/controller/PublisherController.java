package com.jettra.plugin.example.library.controller;

import com.jettra.plugin.example.library.entity.Publisher;
import com.jettra.plugin.example.library.repository.PublisherRepository;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.core.Response;

import java.util.List;

@Path("/library/publishers")
public class PublisherController {

    @GET
    @Produces("application/json")
    public List<Publisher> findAll() {
        return PublisherRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Publisher publisher) {
        PublisherRepository.save(publisher);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        PublisherRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
