package com.jettra.plugin.example.cruview.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.crud.model.DeporteModel;
import com.jettra.plugin.example.crudview.model.GrupoModel;
import com.jettra.plugin.example.crudview.model.SubGrupoModel;
import com.jettra.plugin.example.repository.DeporteRepository;
import com.jettra.plugin.example.crud.repository.GrupoRepository;
import com.jettra.plugin.example.crud.repository.SubGrupoRepository;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.JettraSyncManager;
import io.jettra.wui.sync.SyncType;
import io.jettra.server.JettraServer;
import io.jettra.core.inject.annotation.InjectProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@JettraPageSincronized(SyncType.ALL)
public class SubGrupoPage extends DashboardBasePage {

    @InjectProperties(name = "messages")
    private Properties msg;

    public SubGrupoPage() {
        super("Gestión de SubGrupos");
    }

    @Override
    protected void initCenter(Center center, String username) {
        CrudView crudView = new CrudView(SubGrupoModel.class, SubGrupoRepository.class, msg);
        crudView.setReportEnabled(true);
        center.add(crudView.build());
    }

    @Override
    protected void onGet(Map<String, String> params) {
        if ("report".equals(params.get("action"))) {
            String format = params.get("format");
            if (format == null) format = "pdf";
            boolean print = "true".equals(params.get("print"));
            
            // Invoke report generation
            io.jettra.wui.mvc.JettraMVC.generateReport(this, SubGrupoModel.class, SubGrupoRepository.class, format, print);
        }
    }

    @Override
    protected void onPost(Map<String, String> params) {
        String action = params.get("action");
        String id = params.get("id");
        
        if ("save".equals(action)) {
            String grupoId = params.get("grupoModel");
            GrupoModel grupo = GrupoRepository.findById(grupoId);
            
            String deportesIds = params.get("deportesModel");
            List<DeporteModel> selectedDeportes = new ArrayList<>();
            if (deportesIds != null && !deportesIds.isEmpty()) {
                for (String code : deportesIds.split(",")) {
                    DeporteModel d = DeporteRepository.findByCode(code.trim());
                    if (d != null) selectedDeportes.add(d);
                }
            }
            
            SubGrupoRepository.save(new SubGrupoModel(id, params.get("name"), grupo, selectedDeportes));
            JettraSyncManager.notifyChange("SubGrupoModel", SyncType.UPDATE, "user");
        } else if ("delete".equals(action)) {
            SubGrupoRepository.delete(id);
            JettraSyncManager.notifyChange("SubGrupoModel", SyncType.DELETE, "user");
        }
        
        try {
            redirect(currentExchange, JettraServer.resolvePath("/subgrupo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
