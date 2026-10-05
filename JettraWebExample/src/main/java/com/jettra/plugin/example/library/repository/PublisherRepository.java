package com.jettra.plugin.example.library.repository;

import com.jettra.plugin.example.library.entity.Publisher;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublisherRepository {
    private static final List<Publisher> db = new ArrayList<>();
//    private static final PublisherRecordModelConverter converter = new PublisherRecordModelConverter();

    static {
        db.add(new Publisher("1", "Penguin Books", "London"));
        db.add(new Publisher("2", "Editorial Sudamericana", "Buenos Aires"));
    }

    public static List<Publisher> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Publisher record) {
        if (record.id() == null || record.id().isEmpty()) {
            record = new Publisher(String.valueOf(db.size() + 1), record.name(), record.address());
        }
        delete(record.id());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().equals(id));
    }
    
    public static Optional<Publisher> findById(String id) {
        return db.stream().filter(r -> r.id().equals(id)).findFirst();
    }
}
