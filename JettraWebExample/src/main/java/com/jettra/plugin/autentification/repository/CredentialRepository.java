package com.jettra.plugin.autentification.repository;

import com.jettra.plugin.autentification.entity.Credential;
import com.jettra.plugin.autentification.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

public class CredentialRepository {
    private static final List<Credential> db = new ArrayList<>();

    static {
        User adminUser = UserRepository.findAll().stream().filter(u -> u.firstName().equals("Admin")).findFirst().orElse(null);
        db.add(new Credential(
            UUID.nameUUIDFromBytes("admin-cred-id".getBytes()),
            adminUser,
            "admin",
            "admin",
            true,
            Instant.now()
        ));

        User demoUser = UserRepository.findAll().stream().filter(u -> u.firstName().equals("Demo")).findFirst().orElse(null);
        db.add(new Credential(
            UUID.nameUUIDFromBytes("demo-cred-id".getBytes()),
            demoUser,
            "demo",
            "demo",
            true,
            Instant.now()
        ));
    }

    public static List<Credential> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Credential record) {
        if (record.id() == null) {
            record = new Credential(UUID.randomUUID(), record.user(), record.username(), record.passwordHash(), record.active(), record.lastLogin());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Credential> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
