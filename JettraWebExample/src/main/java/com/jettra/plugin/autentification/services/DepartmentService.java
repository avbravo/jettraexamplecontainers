package com.jettra.plugin.autentification.services;

import com.jettra.plugin.autentification.entity.Department;
import com.jettra.plugin.autentification.model.DepartmentRecordModelConverter;
import com.jettra.plugin.autentification.restclient.DepartmentRestClient;
import com.jettra.plugin.autentification.restclient.IDepartmentRestClient;
import java.util.List;

public class DepartmentService {

    private static final DepartmentRecordModelConverter converter = new DepartmentRecordModelConverter();
    private static final IDepartmentRestClient client = new DepartmentRestClient();

    public static List<Department> findAll() {
        List<Department> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Department record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
