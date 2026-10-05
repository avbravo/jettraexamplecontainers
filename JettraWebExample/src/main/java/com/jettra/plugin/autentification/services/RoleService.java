package com.jettra.plugin.autentification.services;


import com.jettra.plugin.autentification.entity.Role;
import com.jettra.plugin.autentification.model.RoleRecordModelConverter;
import com.jettra.plugin.autentification.restclient.IRoleRestClient;
import com.jettra.plugin.autentification.restclient.RoleRestClient;
import java.util.List;

public class RoleService {

    private static final RoleRecordModelConverter converter = new RoleRecordModelConverter();
    private static final IRoleRestClient client = new RoleRestClient();

    public static List<Role> findAll() {
        List<Role> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Role record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
