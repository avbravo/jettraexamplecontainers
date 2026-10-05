/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jettra.plugin.example.crud.repository;

import com.jettra.plugin.example.crudview.model.CancionModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avbravo
 */
public class CancionRepository {
    private static final List<CancionModel> cancions = new ArrayList<>();
    static{
        cancions.add(new CancionModel("01", "Black Metal"));
        cancions.add(new CancionModel("02", "Death Metal"));
    }
    public static List<CancionModel> findAll(){
        return new ArrayList<>(cancions);
    }
    
    public static CancionModel findById(String id){
        return cancions.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }
    
    public static void save(CancionModel cancionModel){
        CancionModel existing = findById(cancionModel.getId());
        if(existing != null){
            existing.setName(cancionModel.getName());
        }else{
            cancions.add(cancionModel);
        }
    }
    public static void delete(String id){
        cancions.removeIf(c ->c.getId().equals(id));
    }
}
