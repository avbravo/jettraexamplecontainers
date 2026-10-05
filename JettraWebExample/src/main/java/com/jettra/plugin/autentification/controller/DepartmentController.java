package com.jettra.plugin.autentification.controller;

import com.jettra.plugin.autentification.entity.Department;
import com.jettra.plugin.autentification.repository.DepartmentRepository;
import io.jettra.rest.annotations.Consumes;
import io.jettra.rest.annotations.DELETE;
import io.jettra.rest.annotations.GET;
import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.PathParam;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/departments")
public class DepartmentController {

    @GET
    @Produces("application/json")
    public List<Department> findAll() {
        return DepartmentRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Department department) {
        DepartmentRepository.save(department);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        DepartmentRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
