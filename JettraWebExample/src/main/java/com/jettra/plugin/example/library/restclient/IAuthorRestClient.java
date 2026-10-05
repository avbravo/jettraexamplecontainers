/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jettra.plugin.example.library.restclient;

import com.jettra.plugin.example.library.entity.Author;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.HeaderParam;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.client.RestClient;
import java.util.List;

/**
 *
 * @author avbravo
 */
@RestClient(baseUri = "/api/library/authors")
public interface IAuthorRestClient {

    @GET
    List<Author> findAll(@HeaderParam("Authorization") String token);

    @POST
    void save(@HeaderParam("Authorization") String token, Author model);

    @DELETE
    @Path("/{id}")
    void delete(@HeaderParam("Authorization") String token, @PathParam("id") String id);
}
