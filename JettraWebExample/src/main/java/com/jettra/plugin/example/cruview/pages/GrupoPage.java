package com.jettra.plugin.example.cruview.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.crudview.model.GrupoModel;
import com.jettra.plugin.example.crud.repository.GrupoRepository;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.JettraSyncManager;
import io.jettra.wui.sync.SyncType;
import io.jettra.server.JettraServer;
import io.jettra.core.inject.annotation.InjectProperties;
import java.util.Map;
import java.util.Properties;

@JettraPageSincronized(SyncType.ALL)
public class GrupoPage extends DashboardBasePage {

    @InjectProperties(name = "messages")
    private Properties msg;

    public GrupoPage() {
        super("Gestión de Grupos");
    }

    @Override
    protected void initCenter(Center center, String username) {
        CrudView crudView = new CrudView(GrupoModel.class, GrupoRepository.class, msg);
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
            io.jettra.wui.mvc.JettraMVC.generateReport(this, GrupoModel.class, GrupoRepository.class, format, print);
        }
    }

    @Override
    protected void onPost(Map<String, String> params) {
        String action = params.get("action");
        String id = params.get("id"); // CrudView uses the field name for the ID in the form
        
        if ("save".equals(action)) {
            GrupoRepository.save(new GrupoModel(id, params.get("name")));
            JettraSyncManager.notifyChange("GrupoModel", SyncType.UPDATE, "user");
        } else if ("delete".equals(action)) {
            GrupoRepository.delete(id);
            JettraSyncManager.notifyChange("GrupoModel", SyncType.DELETE, "user");
        }
        
        try {
            redirect(currentExchange, JettraServer.resolvePath("/grupo"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
