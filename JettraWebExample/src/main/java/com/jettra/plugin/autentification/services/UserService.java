package com.jettra.plugin.autentification.services;

import com.jettra.plugin.autentification.entity.User;
import com.jettra.plugin.autentification.model.UserRecordModelConverter;
import com.jettra.plugin.autentification.restclient.IUserRestClient;
import com.jettra.plugin.autentification.restclient.UserRestClient;
import java.util.List;

public class UserService {

    private static final UserRecordModelConverter converter = new UserRecordModelConverter();
    private static final IUserRestClient client = new UserRestClient();

    public static List<User> findAll() {
        List<User> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(User record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
