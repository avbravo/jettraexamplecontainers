package com.jettra.plugin.example.library.controller;

import com.jettra.plugin.example.library.entity.Author;
import com.jettra.plugin.example.library.repository.AuthorRepository;

import java.util.List;

import io.jettra.server.openapi.annotations.OpenApi;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.annotations.Secured;
import io.jettra.rest.core.Response;

@Secured
@OpenApi(title = "Library API", version = "v1.0", description = "API for Library management")
@Path("/library/authors")
public class AuthorController {

    private AuthorRepository authorRepository = new AuthorRepository();

    @GET
    @Produces("application/json")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Author author) {
        authorRepository.save(author);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        authorRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
