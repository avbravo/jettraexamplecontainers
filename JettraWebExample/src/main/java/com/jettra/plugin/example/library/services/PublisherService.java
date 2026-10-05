package com.jettra.plugin.example.library.services;

import com.jettra.plugin.example.library.entity.Publisher;
import com.jettra.plugin.example.library.model.PublisherRecordModelConverter;
import com.jettra.plugin.example.library.restclient.IPublisherRestClient;
import com.jettra.plugin.example.library.restclient.PublisherRestClient;
import java.util.List;

public class PublisherService {
    
    private static final PublisherRecordModelConverter converter = new PublisherRecordModelConverter();
    private static final IPublisherRestClient client = new PublisherRestClient();

    public static List<Publisher> findAll() {
        List<Publisher> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Publisher record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
