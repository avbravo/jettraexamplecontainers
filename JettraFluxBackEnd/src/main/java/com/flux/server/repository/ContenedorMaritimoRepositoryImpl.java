package com.flux.server.repository;

import com.flux.server.entity.ContenedorMaritimo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ContenedorMaritimoRepositoryImpl implements ContenedorMaritimoRepository{
    
    
    private static final List<ContenedorMaritimo> db = new ArrayList<>();

    static {
        // Navigation / Administration / Credentials (secured/restricted)
        addContenedor("Maersk Line",Boolean.TRUE);
        addContenedor("MSC - Mediterranean Shipping Company",Boolean.TRUE);
        addContenedor("CMA CGM",Boolean.TRUE);
        addContenedor("Maersk Line",Boolean.TRUE);
        addContenedor("Hapag-Lloyd)",Boolean.TRUE);
        addContenedor("Ocean Network Express)",Boolean.TRUE);
        addContenedor("COSCO Shipping)",Boolean.TRUE);
       
    }

    private static void addContenedor(String nombre, Boolean active) {
        db.add(new ContenedorMaritimo(UUID.nameUUIDFromBytes((nombre ).getBytes()), nombre , active));
        
    }

    @Override
    public  List<ContenedorMaritimo> findAll() {
        return new ArrayList<>(db);
    }

    public  void save(ContenedorMaritimo record) {
        if (record.id() == null) {
            record = new ContenedorMaritimo(UUID.randomUUID(), record.nombre(), record.active());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public  void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public  Optional<ContenedorMaritimo> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
