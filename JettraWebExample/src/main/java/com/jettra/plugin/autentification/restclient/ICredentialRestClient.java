package com.jettra.plugin.autentification.restclient;

import com.jettra.plugin.autentification.entity.Credential;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/credentials")
public interface ICredentialRestClient {

    @GET
    List<Credential> findAll();

    @POST
    void save(Credential model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
