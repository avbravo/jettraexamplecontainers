package com.jettra.plugin.autentification.restclient;

import com.jettra.plugin.autentification.entity.Department;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.client.RestClient;
import java.util.List;

@RestClient(baseUri = "/api/autentification/departments")
public interface IDepartmentRestClient {

    @GET
    List<Department> findAll();

    @POST
    void save(Department model);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);
}
