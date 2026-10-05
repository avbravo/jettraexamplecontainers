/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.flux.server.repository;

import com.flux.server.entity.ContenedorMaritimo;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author avbravo
 */
public interface ContenedorMaritimoRepository {
    List<ContenedorMaritimo> findAll();
    void save(ContenedorMaritimo record);
    void delete(String id);
    Optional<ContenedorMaritimo> findById(String id);
}
