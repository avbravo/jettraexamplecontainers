package com.jettra.plugin.company.controller;

import com.jettra.plugin.company.entity.Company;
import com.jettra.plugin.company.repository.CompanyRepository;
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

@Path("/plugin/company/companys")
@DeclareRoles({"ADMIN", "MANAGER"})
@RolesAllowed({"ADMIN"})
public class CompanyController {

    @Inject
    CompanyRepository companyRepository;

    @GET
    @Produces("application/json")
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Company company) {
        companyRepository.save(company);
        return Response.ok("Saved successfully").build();
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(Company company) {
        companyRepository.save(company);
        return Response.ok("Updated successfully").build();
    }

    @DELETE

    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        companyRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
