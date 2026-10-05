package com.jettra.plugin.acreditation.repository;

import com.jettra.plugin.acreditation.entity.Feature;
import com.jettra.plugin.acreditation.entity.Permission;
import com.jettra.plugin.acreditation.entity.Role;
import io.jettra.core.inject.annotation.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PermissionRepository {

    private static List<Feature> featureResources = new ArrayList<>();
    private static List<Permission> db = new ArrayList<>();
    @Inject
   static FeatureRepository featureResourceRepository;

    @Inject
    RoleRepository roleRepository;

    static {

        List<Role> roles = RoleRepository.findAll();

       featureResources =featureResourceRepository.findAll();

        for (Role r : roles) {
            for (Feature ao : featureResources) {
                addPermission(ao.name(), r, ao, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
            }
        }

        // Navigation / Administration / Credentials (secured/restricted)
            }

    private static void addPermission(String name, Role role, Feature acreditationOptions, Boolean reader,
            Boolean write,
            Boolean delete,
            Boolean report) {

        db.add(new Permission(
                UUID.nameUUIDFromBytes((name).getBytes()),
                role,
                acreditationOptions,
                reader,
                write,
                delete,
                report
        ));

    }

    public static List<Permission> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Permission record) {
        if (record.id() == null) {
            record = new Permission(UUID.randomUUID(), record.role(), record.feature(), record.reader(), record.write(),record.delete(),record.report());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Permission> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
