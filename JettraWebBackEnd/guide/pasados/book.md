# Guía de JettraWebBackEnd

Esta guía detalla los pasos para crear desde cero un proyecto backend utilizando el framework JettraStack, configurando base de datos, seguridad, endpoints REST y documentación con Swagger.

## 1. Crear el proyecto Maven desde consola

Para iniciar, genera un proyecto base de Maven ejecutando el siguiente comando en tu terminal:

```bash
mvn archetype:generate \
    -DgroupId=com.jettra.example \
    -DartifactId=JettraWebBackEnd \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DinteractiveMode=false
```

Una vez creado, ingresa a la carpeta del proyecto:

```bash
cd JettraWebBackEnd
```

## 2. Configurar las dependencias en `pom.xml`

Abre el archivo `pom.xml` y reemplaza el contenido o añade las siguientes propiedades, dependencias de Jettra y plugins necesarios para compilar y ejecutar tu aplicación como un Fat JAR:

```xml
<properties>
    <maven.compiler.source>25</maven.compiler.source>
    <maven.compiler.target>25</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    
    <jettra.appserver.version>1.0.0-SNAPSHOT</jettra.appserver.version>
    <jettra.rest.version>1.0.0-SNAPSHOT</jettra.rest.version>
    <jettra.json.version>1.0.0-SNAPSHOT</jettra.json.version>
    <jettra.jwt.version>1.0.0-SNAPSHOT</jettra.jwt.version>
    <jettra.annotation.version>1.0.0-SNAPSHOT</jettra.annotation.version>
</properties>

<dependencies>
    <!-- Servidor Backend -->
    <dependency>
        <groupId>io.jettra</groupId>
        <artifactId>JettraAppServer</artifactId>
        <version>${jettra.appserver.version}</version>
    </dependency>
    <!-- REST, JSON, JWT, Annotations -->
    <dependency>
        <groupId>io.jettra</groupId>
        <artifactId>JettraRest</artifactId>
        <version>${jettra.rest.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jettra</groupId>
        <artifactId>JettraJSON</artifactId>
        <version>${jettra.json.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jettra</groupId>
        <artifactId>JettraJWT</artifactId>
        <version>${jettra.jwt.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jettra</groupId>
        <artifactId>JettraAnnotation</artifactId>
        <version>${jettra.annotation.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Compilador con procesador de anotaciones de Jettra -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.jettra</groupId>
                        <artifactId>JettraAnnotation</artifactId>
                        <version>${jettra.annotation.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
        <!-- Plugin para generar Fat JAR -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals><goal>shade</goal></goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>com.jettra.main.Main</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## 3. Crear clases: Entity, Repository, y Controller

### Entity
La Entidad representa el modelo de datos. Por ejemplo, `Company`:

```java
package com.jettra.plugin.company.entity;

import java.util.UUID;

public record Company(UUID id, String name, Boolean active) {}
```

### Repository
El repositorio se encarga de las operaciones con la base de datos (aquí se muestra un ejemplo en memoria, pero puedes conectarlo a un ORM/DB real):

```java
package com.jettra.plugin.company.repository;

import com.jettra.plugin.company.entity.Company;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompanyRepository {
    private static final List<Company> db = new ArrayList<>();

    public static List<Company> findAll() { return new ArrayList<>(db); }

    public static void save(Company record) {
        if (record.id() == null) {
            record = new Company(UUID.randomUUID(), record.name(), true);
        }
        db.removeIf(r -> r.id().equals(record.id()));
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }
}
```

### Controller
Expone los endpoints. Usa las anotaciones de `io.jettra.rest.annotations` como `@GET`, `@POST`, `@PUT`, `@DELETE`:

```java
package com.jettra.plugin.company.controller;

import com.jettra.plugin.company.entity.Company;
import com.jettra.plugin.company.repository.CompanyRepository;
import io.jettra.core.inject.annotation.Inject;
import io.jettra.rest.annotations.*;
import io.jettra.rest.core.Response;
import io.jettra.server.discoverer.Discovered;
import java.util.List;

@Discovered
@Path("/plugin/company/companies")
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
```

## 4. Crear Usuarios en la Base de Datos y JettraSecurityDB

Para habilitar la seguridad y usar bases de datos, configuras JettraSecurity para acceder a tus tablas de usuarios (ej. usando JDBC/JPA o el sistema de Jettra). 

Debes crear un controlador que exponga el endpoint de Autenticación, donde se validen las credenciales del usuario:

```java
package com.jettra.main.jwt;

import io.jettra.rest.annotations.POST;
import io.jettra.rest.annotations.Path;
import io.jettra.rest.annotations.Produces;
import io.jettra.rest.core.Response;
import io.jettra.server.discoverer.Discovered;
// (Importa tus repositorios de usuarios)

@Discovered
@Path("/auth")
public class AuthController {
    
    @POST
    @Path("/login")
    @Produces("application/json")
    public Response login(LoginRequest request) {
        // 1. Validar el usuario en la Base de Datos (JettraSecurityDB / UserRepository)
        // 2. Si es válido, generar un JWT
        String token = "tu.token.jwt";
        return Response.ok("{\"token\":\"" + token + "\"}").build();
    }
}
```

Para proteger tus controladores (como `CompanyController`), añade las anotaciones `@Secured` y `@RolesAllowed`:

```java
@Secured
@DeclareRoles({"ADMIN", "USER"})
@RolesAllowed({"ADMIN"})
public class CompanyController { ... }
```

## 5. Configurar e Integrar Swagger (OpenAPI)

El servidor `JettraAppServer` tiene integración nativa con SwaggerUI y genera la documentación OpenAPI de forma automática para las clases descubiertas con `@Discovered` y `@Path`.

Para utilizarlo:
1. Enciende el servidor (`JettraServer.start(...)`).
2. Configura el endpoint de Swagger UI. Normalmente es provisto por `SwaggerUIHandler`.
3. Navega a `http://localhost:8080/swagger-ui` (o la ruta que configures).

### Envío de Parámetros JSON
Jettra analiza los parámetros de tus métodos (ej. `Company company`) y automáticamente los procesa como **RequestBody** para la documentación de Swagger.

Puedes utilizar anotaciones extra para enriquecer el Swagger si lo deseas:
```java
import io.jettra.server.openapi.annotations.Operation;
import io.jettra.server.openapi.annotations.ApiResponse;

@Operation(summary = "Crear nueva empresa", description = "Añade una empresa a la base de datos")
@ApiResponse(responseCode = "200", description = "Empresa creada exitosamente")
@POST
public Response save(Company company) {
   // ...
}
```

¡Con estos pasos tendrás un Backend modular, seguro y documentado usando el framework JettraStack!
