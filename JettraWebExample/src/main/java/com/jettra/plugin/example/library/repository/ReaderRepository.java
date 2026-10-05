package com.jettra.plugin.example.library.repository;

import com.jettra.plugin.example.library.entity.Reader;
import java.util.ArrayList;
import java.util.List;

public class ReaderRepository {
    private static final List<Reader> db = new ArrayList<>();
//    private static final ReaderRecordModelConverter converter = new ReaderRecordModelConverter();

    static {
        db.add(new Reader("1", "Alice Smith", "alice@example.com"));
        db.add(new Reader("2", "Bob Johnson", "bob@example.com"));
    }

    public static List<Reader> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Reader record) {
        if (record.id() == null || record.id().isEmpty()) {
            record = new Reader(String.valueOf(db.size() + 1), record.name(), record.email());
        }
        delete(record.id());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().equals(id));
    }
}
