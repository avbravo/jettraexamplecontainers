package com.jettra.plugin.example.repository;

import com.jettra.plugin.example.crud.model.PaisModel;
import java.util.List;

public interface PaisRepository {
    List<PaisModel> findAll();
    PaisModel findByCode(String code);
    void save(PaisModel pais);
    void delete(String code);
}
