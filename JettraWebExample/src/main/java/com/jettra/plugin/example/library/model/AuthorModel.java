package com.jettra.plugin.example.library.model;

import com.jettra.plugin.example.library.entity.Author;
import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;

@JettraViewModel
@ModelToRecordConversor(goal = Author.class)
public class AuthorModel {

    @NotNull
    @Size(min = 2, max = 5)
    @PropertiesLabel(value = "lbl.id", label = "ID")
    @PropertiesInRecord
    private String id;

    @PropertiesInRecord
    @NotNull
    @Size(min = 2)
    @PropertiesLabel(value = "lbl.name", label = "Name")
    private String name;

    @PropertiesInRecord
    @Size(min = 3)
    @PropertiesLabel(value = "lbl.country", label = "Country")
    private String country;

    public AuthorModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
