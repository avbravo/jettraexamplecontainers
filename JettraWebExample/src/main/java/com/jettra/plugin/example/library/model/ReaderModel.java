package com.jettra.plugin.example.library.model;

import com.jettra.plugin.example.library.entity.Reader;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.JettraViewModel;

@JettraViewModel
@ModelToRecordConversor(goal = Reader.class)
public class ReaderModel {
    @PropertiesInRecord
    private String id;
    
    @PropertiesInRecord
    private String name;
    
    @PropertiesInRecord
    private String email;

    public ReaderModel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
