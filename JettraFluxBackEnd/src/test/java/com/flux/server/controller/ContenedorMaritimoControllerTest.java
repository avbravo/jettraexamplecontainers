package com.flux.server.controller;

import com.flux.server.controller.ContenedorMaritimoController;
import io.jettra.server.discoverer.DiscoveredLoad;
import io.jettra.test.annotation.JettraTest;
import io.jettra.test.annotation.RequiresRunningServer;
import io.jettra.test.core.JettraAssert;
import io.jettra.test.jwt.JwtTestClient;
import io.jettra.core.inject.annotation.Inject;
import com.flux.server.entity.ContenedorMaritimo;
import io.jettra.rest.core.Response;
import java.util.List;
import java.util.UUID;

@DiscoveredLoad
@RequiresRunningServer
public class ContenedorMaritimoControllerTest {

    public Integer serverPortTest = 9050;
    @Inject
    ContenedorMaritimoController contenedorMaritimoController;

    @JettraTest
    public void testInjectedControllerMethods() {
        try {
            // Test save()
            UUID newId = UUID.randomUUID();
            ContenedorMaritimo nuevoContenedor = new ContenedorMaritimo(newId, "Contenedor de Prueba", true);
            Response saveResponse = contenedorMaritimoController.save(nuevoContenedor);
            JettraAssert.assertNotNull(saveResponse, "Response de save no debe ser null");
            JettraAssert.assertEquals(200, saveResponse.getStatus(), "Status de save debe ser 200");

            // Test findAll()
            List<ContenedorMaritimo> list = contenedorMaritimoController.findAll();
            JettraAssert.assertNotNull(list, "La lista de contenedores no debe ser nula");
            IO.println("Contenedores desde inyeccion: " + list);

            // Test delete()
            Response deleteResponse = contenedorMaritimoController.delete(newId.toString());
            JettraAssert.assertNotNull(deleteResponse, "Response de delete no debe ser null");
            JettraAssert.assertEquals(200, deleteResponse.getStatus(), "Status de delete debe ser 200");

        } catch (Exception e) {
            JettraAssert.assertTrue(false, "Fallo en los metodos inyectados: " + e.getMessage());
        }
    }

    @JettraTest
    public void testHttpEndpoints() {
        JwtTestClient jwtClient = new JwtTestClient();
        try {
            // Autenticación con admin/admin
            String authPayload = "{\"username\":\"admin\", \"password\":\"admin\"}";
            String token = jwtClient.authenticate("http://localhost:" +serverPortTest + "/auth/login", authPayload);
            JettraAssert.assertNotNull(token, "El token de autenticación no debe ser nulo");
            
            String baseUrl = "http://localhost:" + serverPortTest + "/plugin/demo/contenedormaritimo";

            // Test POST (save)
            String newId = UUID.randomUUID().toString();
            String postPayload = "{\"id\":\"" + newId + "\", \"active\":true, \"nombre\":\"Test Contenedor POST\"}";
            String postResponse = jwtClient.postWithToken(baseUrl, postPayload);
            JettraAssert.assertTrue(postResponse.contains("Saved successfully"), "Response to POST should contain 'Saved successfully'. Actual: " + postResponse);
            IO.println("POST respuesta: " + postResponse);

            // Test GET (findAll)
            String getResponse = jwtClient.getWithToken(baseUrl);
            JettraAssert.assertNotNull(getResponse, "La respuesta no debe ser nula al listar contenedores");
            JettraAssert.assertTrue(getResponse.contains("Test Contenedor POST"), "La respuesta debe contener el contenedor recién creado");
            IO.println("GET respuesta: " + getResponse);

            // Test PUT (update)
            String putPayload = "{\"id\":\"" + newId + "\", \"active\":false, \"nombre\":\"Test Contenedor PUT\"}";
            String putResponse = jwtClient.putWithToken(baseUrl, putPayload);
            JettraAssert.assertTrue(putResponse.contains("Updated successfully"), "Response to PUT should contain 'Updated successfully'. Actual: " + putResponse);
            IO.println("PUT respuesta: " + putResponse);

            // Test DELETE
            String deleteResponse = jwtClient.deleteWithToken(baseUrl + "/" + newId);
            JettraAssert.assertTrue(deleteResponse.contains("Deleted successfully"), "Response to DELETE should contain 'Deleted successfully'. Actual: " + deleteResponse);
            IO.println("DELETE respuesta: " + deleteResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
            JettraAssert.assertTrue(false, "La prueba falló debido a una excepción: " + e.getMessage());
        }
    }
}
