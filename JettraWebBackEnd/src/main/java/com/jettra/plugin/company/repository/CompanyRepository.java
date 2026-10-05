package com.jettra.plugin.company.repository;

import com.jettra.plugin.company.entity.Company;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompanyRepository {
    private static final List<Company> db = new ArrayList<>();

    static {

        db.add(new Company(UUID.nameUUIDFromBytes("COMPANY 1".getBytes()), "COMPANY 1", Boolean.TRUE));
        db.add(new Company(UUID.nameUUIDFromBytes("COMPANY 2".getBytes()), "COMPANY 2", Boolean.TRUE));

    }

    public static List<Company> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Company record) {
        if (record.id() == null) {
            record = new Company(UUID.randomUUID(), record.name(), Boolean.TRUE);
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Company> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
