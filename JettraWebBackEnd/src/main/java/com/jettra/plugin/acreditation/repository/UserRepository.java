package com.jettra.plugin.acreditation.repository;

import com.jettra.plugin.acreditation.entity.RoleDepartament;
import com.jettra.plugin.acreditation.entity.User;
import com.jettra.plugin.acreditation.entity.Role;
import com.jettra.plugin.company.entity.Department;
import com.jettra.plugin.company.repository.DepartmentRepository;
import io.jettra.core.inject.annotation.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class UserRepository {

    private static final List<User> db = new ArrayList<>();

    @Inject
    static DepartmentRepository departmentRepository;
    @Inject
    static RoleRepository roleRepository;

    static {

        List<Role> availableRoles = roleRepository.findAll();
        List<Department> availableDepartments = departmentRepository.findAll();
        IO.println("------------------------------------------");
        IO.println("availableRoles: "+availableRoles.size());
        IO.println("availableDepartments: "+availableDepartments.size());
        // List<RoleDepartament> roleDepartaments = new ArrayList<>();
        Set<RoleDepartament> roleDepartaments = new HashSet<>();
        Set<RoleDepartament> roleDepartaments2 = new HashSet<>();
        Set<RoleDepartament> roleDepartaments3 = new HashSet<>();
        int minimo = 1;
        int maximo = availableDepartments.size()-1;
        int maximoRole = availableRoles.size()-1;
//        for (Role r : availableRoles) {
            // Obtiene el generador por defecto del sistema
            RandomGenerator generador = RandomGenerator.getDefault();

            // El método .nextInt(min, max) es exclusivo en el límite superior, sumamos 1
            int numeroAleatorio = generador.nextInt(minimo, maximo + 1);
            int numeroAleatorioRole = generador.nextInt(minimo, maximoRole + 1);

            RoleDepartament rd = new RoleDepartament(UUID.fromString("e3a39e8e-8a22-4a7b-bc43-22879555de11"),
                    availableRoles.get(numeroAleatorioRole), availableDepartments.get(numeroAleatorio), Boolean.TRUE);
            roleDepartaments.add(rd);
            
            // User 2
     
            numeroAleatorio = generador.nextInt(minimo, maximo + 1);
            numeroAleatorioRole = generador.nextInt(minimo, maximoRole + 1);

        rd = new RoleDepartament(UUID.fromString("e3a39e8e-8a22-4a7b-bc43-22879555de11"),
                    availableRoles.get(numeroAleatorioRole), availableDepartments.get(numeroAleatorio), Boolean.TRUE);
            roleDepartaments2.add(rd);
            
            
                // User 3
     
            numeroAleatorio = generador.nextInt(minimo, maximo + 1);
            numeroAleatorioRole = generador.nextInt(minimo, maximoRole + 1);

        rd = new RoleDepartament(UUID.fromString("e3a39e8e-8a22-4a7b-bc43-22879555de11"),
                    availableRoles.get(numeroAleatorioRole), availableDepartments.get(numeroAleatorio), Boolean.TRUE);
            roleDepartaments3.add(rd);
            

        // --- MOCK 1: Usuario Administrador General ---
//        Set<RoleDepartament> permisions1 = new RoleDepartament(id, user, roleFeature, department, Boolean.TRUE)
        db.add(new User(
                UUID.fromString("e3a39e8e-8a22-4a7b-bc43-22879555de11"),
                "jdoe_admin",
                "$2a$12$eImiTXuWVxfM37uY4JANjOL.oFqLPVMbDu73.T03qX53gLw.LqT72", // password123 hashed
                "8-765-4321", // identificationNumber
                "John",
                "Doe",
                "john.doe@company.com",
                "+507 6123-4567",
                true, // active
                "https://api.dicebear.com/7.x/avataaars/svg?seed=John",
                roleDepartaments
        ));

// --- MOCK 2: Usuario de Recursos Humanos ---
        db.add(new User(
                UUID.fromString("7b4b455b-f112-4217-91a0-7b24340d87fa"),
                "amartinez",
                "$2a$12$8K1G.Y4V8S7RDeZbx5Q6euC3kQ6jXbH8eYfWpLpZ4c.f5G5zG3O7m", // securePass456 hashed
                "E-8-12345", // identificationNumber
                "Ana",
                "Martínez",
                "ana.martinez@company.com",
                "+507 6987-6543",
                true, // active
                "https://api.dicebear.com/7.x/avataaars/svg?seed=Maria",
                roleDepartaments2
        ));

// --- MOCK 3: Usuario Inactivo / Consultor Externo ---
        db.add(new User(
                UUID.fromString("1f88ef4a-39b5-4b16-8bb0-2df19d08e5c8"),
                "csmith_consultant",
                "$2a$12$Vw7vV7Yy.KxYx0FhF88pUe6GZ7f1X8xY7xV7vV7Yy.KxYx0FhF88p", // tempPass789 hashed
                "PE-999-888", // identificationNumber
                "Carlos",
                "Smith",
                null, // email (opcional)
                null, // phone (opcional)
                false, // active
                "https://api.dicebear.com/7.x/avataaars/svg?seed=Carlos",
                roleDepartaments3
        ));

    }

    public static List<User> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(User record) {
        if (record.id() == null) {
            record = new User(UUID.randomUUID(),
                    record.username(),
                    record.passwordHash(),
                    record.identificationNumber(),
                    record.firstName(),
                    record.lastName(),
                    record.email(),
                    record.phone(),
                    record.active(),
                    record.photo(),
            record.roleDepartaments());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<User> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }

}
