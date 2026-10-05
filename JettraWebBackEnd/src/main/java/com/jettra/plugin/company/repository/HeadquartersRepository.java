package com.jettra.plugin.company.repository;

import com.jettra.plugin.company.entity.Company;
import com.jettra.plugin.company.entity.Headquarters;
import io.jettra.core.inject.annotation.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HeadquartersRepository {

    private static final List<Headquarters> db = new ArrayList<>();
    @Inject
    static CompanyRepository companyRepository;

    static {

        List<Company> companys = companyRepository.findAll();

        db.add(new Headquarters(UUID.nameUUIDFromBytes("HEADQUARTERS-1".getBytes()), "HEADQUARTERS 1", companys.get(0), Boolean.TRUE));
        db.add(new Headquarters(UUID.nameUUIDFromBytes("HEADQUARTERS-2".getBytes()), "HEADQUARTERS 2", companys.get(0), Boolean.TRUE));
        db.add(new Headquarters(UUID.nameUUIDFromBytes("HEADQUARTERS-3".getBytes()), "HEADQUARTERS 3", companys.get(1), Boolean.TRUE));
        db.add(new Headquarters(UUID.nameUUIDFromBytes("HEADQUARTERS-4".getBytes()), "HEADQUARTERS 4", companys.get(1), Boolean.TRUE));

    }

    public static List<Headquarters> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Headquarters record) {
        if (record.id() == null) {
            record = new Headquarters(UUID.randomUUID(), record.name(), record.company(), Boolean.TRUE);
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Headquarters> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
