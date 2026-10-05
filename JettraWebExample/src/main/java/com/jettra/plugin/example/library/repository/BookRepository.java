package com.jettra.plugin.example.library.repository;

import com.jettra.plugin.example.library.entity.Book;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private static final List<Book> db = new ArrayList<>();
//    private static final BookRecordModelConverter converter = new BookRecordModelConverter();

    static {
        db.add(new Book("1", "Cien años de soledad", "1", "2", 1967));
        db.add(new Book("2", "Harry Potter and the Philosopher's Stone", "2", "1", 1997));
    }

    public static List<Book> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Book record) {
        if (record.id() == null || record.id().isEmpty()) {
            record = new Book(String.valueOf(db.size() + 1), record.title(), record.authorId(), record.publisherId(), record.year());
        }
        delete(record.id());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().equals(id));
    }
}
