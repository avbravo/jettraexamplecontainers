package com.jettra.plugin.example.library.services;

import com.jettra.plugin.example.library.entity.Book;
import com.jettra.plugin.example.library.model.BookRecordModelConverter;
import com.jettra.plugin.example.library.restclient.BookRestClient;
import com.jettra.plugin.example.library.restclient.IBookRestClient;
import java.util.List;

public class BookService {
    
    private static final BookRecordModelConverter converter = new BookRecordModelConverter();
    private static final IBookRestClient client = new BookRestClient();

    public static List<Book> findAll() {
        List<Book> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Book record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
