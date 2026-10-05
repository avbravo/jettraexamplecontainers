package com.jettra.plugin.example.library.model;

import com.jettra.plugin.example.library.entity.Publisher;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.JettraViewModel;

@JettraViewModel
@ModelToRecordConversor(goal = Publisher.class)
public class PublisherModel {
    @PropertiesInRecord
    private String id;
    
    @PropertiesInRecord
    private String name;
    
    @PropertiesInRecord
    private String address;

    public PublisherModel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
