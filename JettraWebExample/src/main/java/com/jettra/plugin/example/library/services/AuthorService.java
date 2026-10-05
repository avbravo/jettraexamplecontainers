package com.jettra.plugin.example.library.services;

import com.jettra.plugin.example.library.entity.Author;
import com.jettra.plugin.example.library.restclient.IAuthorRestClient;
import com.jettra.plugin.example.library.model.AuthorRecordModelConverter;
import com.jettra.plugin.example.library.restclient.AuthorRestClient;
import io.jettra.jwt.JettraJWT;
import io.jettra.wui.components.Console;
import java.util.HashMap;
import java.util.List;

public class AuthorService {
    
    private static final AuthorRecordModelConverter converter = new AuthorRecordModelConverter();
    private static final IAuthorRestClient client = new AuthorRestClient();

    private static String getJwtToken() {
        String user = "anonymous";
        if (io.jettra.server.core.JettraContext.getCurrent() != null) {
            Object sessionUser = io.jettra.server.core.JettraContext.getCurrent().get(io.jettra.server.core.JettraContext.Scope.SESSION, "username");
            if (sessionUser != null) {
                user = sessionUser.toString();
            }
        }
        JettraJWT jwt = new JettraJWT("default_secret_key_jettra_rest_2026", 3600000);
        return "Bearer " + jwt.generateToken(new HashMap<>(), user);
    }

    public static List<Author> findAll() {
        Console.addMessage("miConsola", "AuthorService.findAll()..", "info");
        System.out.println("...> paso f-1 AuthorService.findAll()");
        List<Author> records = client.findAll(getJwtToken());
        System.out.println("...> paso f-2  records.size() "+records.size());
        if (records == null) return List.of();
        return records;
    }

    public static void save(Author record) {
        Console.addMessage("miConsola", "AuthorService.save()..", "info");
        client.save(getJwtToken(), record);

    }

    public static void delete(String id) {
        Console.addMessage("miConsola", "AuthorService.delete(" + id + ")..", "info");
        client.delete(getJwtToken(), id);
    }
}
