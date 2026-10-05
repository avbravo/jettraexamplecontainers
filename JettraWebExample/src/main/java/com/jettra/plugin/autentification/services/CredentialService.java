package com.jettra.plugin.autentification.services;

import com.jettra.plugin.autentification.entity.Credential;
import com.jettra.plugin.autentification.model.CredentialRecordModelConverter;
import com.jettra.plugin.autentification.restclient.CredentialRestClient;
import com.jettra.plugin.autentification.restclient.ICredentialRestClient;
import java.util.List;

public class CredentialService {

    private static final CredentialRecordModelConverter converter = new CredentialRecordModelConverter();
    private static final ICredentialRestClient client = new CredentialRestClient();

    public static List<Credential> findAll() {
        List<Credential> records = client.findAll();
        if (records == null) return List.of();
        return records;
    }

    public static void save(Credential record) {
        client.save(record);
    }

    public static void delete(String id) {
        client.delete(id);
    }
}
