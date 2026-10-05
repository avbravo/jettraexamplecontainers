package com.jettra.plugin.example.library.repository;

import com.jettra.plugin.example.library.entity.Author;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepository implements IAuthorRepository{
    private static final List<Author> db = new ArrayList<>();
   // private static final AuthorRecordModelConverter converter = new AuthorRecordModelConverter();

    static {
        db.add(new Author("1", "Gabriel Garcia Marquez", "Colombia"));
        db.add(new Author("2", "J.K. Rowling", "UK"));
    }

    public  List<Author> findAll() {
        return new ArrayList<>(db);
    }
//    public static List<Author> findAll() {
//        return new ArrayList<>(db);
//    }

    public  void save(Author record) {
        if (record.id() == null || record.id().isEmpty()) {
            record = new Author(String.valueOf(db.size() + 1), record.name(), record.country());
        }
        delete(record.id()); // remove if exists
        db.add(record);
    }

    public  void delete(String id) {
        db.removeIf(r -> r.id().equals(id));
    }
    
    public  Optional<Author> findById(String id) {
        return db.stream().filter(r -> r.id().equals(id)).findFirst();
    }
}
