package com.jettra.plugin.example.library.repository;

import com.jettra.plugin.example.library.entity.Author;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IAuthorRepository {
    

    public List<Author> findAll();

    public  void save(Author record);

    public void delete(String id) ;
    
    public  Optional<Author> findById(String id) ;
}
