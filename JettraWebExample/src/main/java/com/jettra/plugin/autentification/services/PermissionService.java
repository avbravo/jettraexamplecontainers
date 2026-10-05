package com.jettra.plugin.autentification.services;

import com.jettra.plugin.autentification.entity.Permission;
import com.jettra.plugin.autentification.model.PermissionRecordModelConverter;
import com.jettra.plugin.autentification.restclient.IPermissionRestClient;
import com.jettra.plugin.autentification.restclient.PermissionRestClient;
import java.util.List;

public class PermissionService {

    private static final PermissionRecordModelConverter converter = new PermissionRecordModelConverter();
    private static final IPermissionRestClient client = new PermissionRestClient();

    public static List<Permission> findAll() {
        List<Permission> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Permission record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
