package com.jettra.plugin.example.repository;

import com.jettra.plugin.example.crud.model.PaisModel;
import java.util.ArrayList;
import java.util.List;

public class PaisRepositoryImpl implements PaisRepository {
    private static final List<PaisModel> paises = new ArrayList<>();

    static {
        paises.add(new PaisModel("PA", "Panamá"));
        paises.add(new PaisModel("CR", "Costa Rica"));
        paises.add(new PaisModel("US", "Estados Unidos"));
    }

    @Override
    public List<PaisModel> findAll() {
        return new ArrayList<>(paises);
    }

    @Override
    public PaisModel findByCode(String code) {
        return paises.stream().filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
    }

    @Override
    public void save(PaisModel pais) {
        PaisModel existing = findByCode(pais.getCode());
        if (existing != null) {
            existing.setName(pais.getName());
        } else {
            paises.add(pais);
        }
    }

    @Override
    public void delete(String code) {
        paises.removeIf(p -> p.getCode().equals(code));
    }
}
