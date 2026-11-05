package io.jettra.examples.store.factura.rest;

import io.jettra.examples.store.driver.JettraDriver;
import io.jettra.examples.store.factura.model.Factura;
import io.jettra.examples.store.factura.service.FacturaService;
import io.jettra.rest.annotations.*;
import io.jettra.rest.core.Response;
import io.jettra.store.core.StorageMode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Endpoint REST para la gestión de Facturas con JettraRest y JettraDriver.
 * Permite realizar operaciones CRUD completas y conmutar en caliente entre
 * los tres modos de almacenamiento:
 *   - JVM_RAM
 *   - DISK_MEMORY
 *   - OFF_HEAP
 */
@Path("/api/facturas")
public class FacturaRestController {

    private final FacturaService facturaService;

    public FacturaRestController() {
        this.facturaService = new FacturaService(JettraDriver.open());
    }

    public FacturaRestController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public FacturaRestController(JettraDriver driver) {
        this.facturaService = new FacturaService(driver);
    }

    /**
     * GET /api/facturas?page=1&size=10
     * Lista facturas con soporte de paginación.
     */
    @GET
    public Response listFacturas(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        int p = (page != null && page > 0) ? page : 1;
        int s = (size != null && size > 0) ? size : 10;
        List<Factura> facturas = facturaService.list(p, s);
        long total = facturaService.count();

        Map<String, Object> payload = Map.of(
            "page", p,
            "size", s,
            "total", total,
            "storage_mode", facturaService.getStorageMode().name(),
            "data", facturas.stream().map(Factura::toMap).toList()
        );
        return Response.ok(payload).build();
    }

    /**
     * GET /api/facturas/{id}
     * Obtiene una factura por su ID.
     */
    @GET
    @Path("/{id}")
    public Response getFacturaById(@PathParam("id") String id) {
        Optional<Factura> opt = facturaService.findById(id);
        if (opt.isPresent()) {
            return Response.ok(opt.get().toMap()).build();
        }
        return Response.status(404).entity(Map.of("error", "Factura no encontrada", "id", id)).build();
    }

    /**
     * POST /api/facturas
     * Crea una nueva factura.
     */
    @POST
    public Response createFactura(Factura factura) {
        if (factura == null) {
            return Response.status(400).entity(Map.of("error", "Cuerpo de factura inválido")).build();
        }
        Factura created = facturaService.create(factura);
        return Response.created(created.toMap()).build();
    }

    /**
     * PUT /api/facturas/{id}
     * Actualiza una factura existente.
     */
    @PUT
    @Path("/{id}")
    public Response updateFactura(@PathParam("id") String id, Factura factura) {
        if (factura == null) {
            return Response.status(400).entity(Map.of("error", "Cuerpo de factura inválido")).build();
        }
        Optional<Factura> updated = facturaService.update(id, factura);
        if (updated.isPresent()) {
            return Response.ok(updated.get().toMap()).build();
        }
        return Response.status(404).entity(Map.of("error", "Factura no encontrada para actualizar", "id", id)).build();
    }

    /**
     * DELETE /api/facturas/{id}
     * Elimina una factura por su ID.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteFactura(@PathParam("id") String id) {
        boolean removed = facturaService.delete(id);
        if (removed) {
            return Response.ok(Map.of("status", "DELETED", "id", id)).build();
        }
        return Response.status(404).entity(Map.of("error", "Factura no encontrada para eliminar", "id", id)).build();
    }

    /**
     * GET /api/facturas/storage-mode
     * Consulta el modo de almacenamiento activo en example_factura_db.
     */
    @GET
    @Path("/storage-mode")
    public Response getStorageMode() {
        StorageMode mode = facturaService.getStorageMode();
        return Response.ok(Map.of(
            "database", FacturaService.DATABASE_NAME,
            "mode", mode.name(),
            "code", mode.getCode(),
            "description", mode.getDescription()
        )).build();
    }

    /**
     * POST /api/facturas/storage-mode/{mode}
     * Conmuta el modo de almacenamiento entre JVM_RAM, DISK_MEMORY u OFF_HEAP.
     */
    @POST
    @Path("/storage-mode/{mode}")
    public Response setStorageMode(@PathParam("mode") String modeStr) {
        try {
            StorageMode mode = facturaService.setStorageMode(modeStr);
            return Response.ok(Map.of(
                "database", FacturaService.DATABASE_NAME,
                "status", "MODIFIED",
                "new_mode", mode.name(),
                "code", mode.getCode(),
                "description", mode.getDescription()
            )).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(Map.of(
                "error", "Modo desconocido. Opciones: JVM_RAM, DISK_MEMORY, OFF_HEAP",
                "provided", modeStr
            )).build();
        }
    }
}

