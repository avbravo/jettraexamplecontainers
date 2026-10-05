package com.jettra.plugin.autentification.repository;

import com.jettra.plugin.autentification.entity.User;
import com.jettra.plugin.autentification.entity.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRepository {
    private static final List<User> db = new ArrayList<>();

    static {
        List<Role> allRoles = RoleRepository.findAll();
        
        Set<Role> adminRoles = allRoles.stream().collect(Collectors.toSet()); // ADMIN, MANAGER, DEMO
        Set<Role> demoRoles = allRoles.stream()
            .filter(r -> r.name().equals("DEMO"))
            .collect(Collectors.toSet()); // DEMO

        db.add(new User(
            UUID.nameUUIDFromBytes("admin-user-id".getBytes()),
            "Admin", "System",
            "admin@jettra.com", "123-456-789", true,
            null, adminRoles
        ));

        db.add(new User(
            UUID.nameUUIDFromBytes("demo-user-id".getBytes()),
            "Demo", "User",
            "demo@jettra.com", "987-654-321", true,
            null, demoRoles
        ));
    }

    public static List<User> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(User record) {
        if (record.id() == null) {
            record = new User(UUID.randomUUID(), record.firstName(), record.lastName(), record.email(), record.phone(), record.active(), record.department(), record.roles());
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
