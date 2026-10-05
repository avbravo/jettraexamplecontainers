package com.jettra.plugin.autentification.repository;

import com.jettra.plugin.autentification.entity.Role;
import com.jettra.plugin.autentification.entity.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class RoleRepository {
    private static final List<Role> db = new ArrayList<>();

    static {
        List<Permission> allPerms = PermissionRepository.findAll();
        Set<Permission> adminPerms = new HashSet<>(allPerms);
        Set<Permission> managerPerms = new HashSet<>(allPerms);

        // DEMO perms: NOT associated with ADMINISTRATION or CREDENTIALS, and only QUERY permissions
        Set<Permission> demoPerms = allPerms.stream()
            .filter(p -> {
                String path = p.resourcePath();
                boolean isAdminPath = path.equals("/permiso") || path.equals("/rol") || path.equals("/perfil") 
                                   || path.equals("/usuario") || path.equals("/webdesigner") || path.equals("/kanban") 
                                   || path.equals("/swagger-ui");
                boolean isCredPath = path.equals("/credential") || path.equals("/permission") || path.equals("/role") 
                                  || path.equals("/department") || path.equals("/user");
                return !isAdminPath && !isCredPath && p.name().endsWith("_QUERY");
            })
            .collect(Collectors.toSet());

        db.add(new Role(UUID.nameUUIDFromBytes("ADMIN".getBytes()), "ADMIN", adminPerms));
        db.add(new Role(UUID.nameUUIDFromBytes("MANAGER".getBytes()), "MANAGER", managerPerms));
        db.add(new Role(UUID.nameUUIDFromBytes("DEMO".getBytes()), "DEMO", demoPerms));
    }

    public static List<Role> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Role record) {
        if (record.id() == null) {
            record = new Role(UUID.randomUUID(), record.name(), record.permissions());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Role> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
