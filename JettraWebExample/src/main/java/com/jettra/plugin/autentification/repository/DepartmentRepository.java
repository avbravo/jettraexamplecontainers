package com.jettra.plugin.autentification.repository;

import com.jettra.plugin.autentification.entity.Department;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DepartmentRepository {
    private static final List<Department> db = new ArrayList<>();

    static {
        db.add(new Department(UUID.fromString("66666666-6666-6666-6666-666666666666"), "IT Department", null));
        db.add(new Department(UUID.fromString("77777777-7777-7777-7777-777777777777"), "Human Resources", null));
    }

    public static List<Department> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Department record) {
        if (record.id() == null) {
            record = new Department(UUID.randomUUID(), record.name(), record.parentDepartmentId());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Department> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
