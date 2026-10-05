package com.jettra.plugin.autentification.restclient;

import com.jettra.plugin.autentification.entity.Permission;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/permissions")
public interface IPermissionRestClient {

    @GET
    List<Permission> findAll();

    @POST
    void save(Permission model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
