package com.jettra.plugin.example.library.services;

import com.jettra.plugin.example.library.entity.Reader;
import com.jettra.plugin.example.library.model.ReaderRecordModelConverter;
import com.jettra.plugin.example.library.restclient.IReaderRestClient;
import com.jettra.plugin.example.library.restclient.ReaderRestClient;
import java.util.List;

public class ReaderService {
    
    private static final ReaderRecordModelConverter converter = new ReaderRecordModelConverter();
    private static final IReaderRestClient client = new ReaderRestClient();

    public static List<Reader> findAll() {
        List<Reader> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Reader record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
