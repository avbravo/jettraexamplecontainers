
package com.jettra.plugin.example.crudview.model;

import com.jettra.plugin.example.crud.model.DeporteModel;
import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.core.annotations.TableColumnField;
import io.jettra.wui.core.annotations.ViewSelectMany;
import io.jettra.wui.core.annotations.ViewSelectOne;
import java.util.List;

@JettraViewModel
public class SubGrupoModel {
    @NotNull
    @Size(min = 2, max = 5)
    @PropertiesLabel(value = "lbl.id", label = "ID")
    private String id;
    
    @NotNull
    @Size(min = 3, max = 100)
    @PropertiesLabel(value = "lbl.name", label = "SubGrupo")
    private String name;
    
    @NotNull
    @ViewSelectOne(label = "name", source = "GrupoRepository", method = "findAll")
    @PropertiesLabel(value = "lbl.grupo", label = "Grupo")
    private GrupoModel grupoModel;
    
    @ViewSelectMany(label = "deporte", source = "DeporteRepository", method = "findAll")
    @PropertiesLabel(value = "lbl.deportes", label = "Deportes")
    @TableColumnField(field = "deporte")
    private List<DeporteModel> deportesModel;
    

    public SubGrupoModel() {}

    public SubGrupoModel(String id, String name, GrupoModel grupoModel) {
        this.id = id;
        this.name = name;
        this.grupoModel = grupoModel;
    }

    public SubGrupoModel(String id, String name, GrupoModel grupoModel, List<DeporteModel> deportesModel) {
        this.id = id;
        this.name = name;
        this.grupoModel = grupoModel;
        this.deportesModel = deportesModel;
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

    public GrupoModel getGrupoModel() {
        return grupoModel;
    }

    public void setGrupoModel(GrupoModel grupoModel) {
        this.grupoModel = grupoModel;
    }

    public List<DeporteModel> getDeportesModel() {
        return deportesModel;
    }

    public void setDeportesModel(List<DeporteModel> deportesModel) {
        this.deportesModel = deportesModel;
    }

    @Override
    public String toString() {
        return "SubGrupoModel{" + "id=" + id + ", name=" + name + ", grupoModel=" + grupoModel + ", deportesModel=" + deportesModel + '}';
    }

    
  

}
