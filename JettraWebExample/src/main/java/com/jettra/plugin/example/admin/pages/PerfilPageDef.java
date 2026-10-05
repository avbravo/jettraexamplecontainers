package com.jettra.plugin.example.admin.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.plugin.example.admin.model.PerfilModel.class, repository = com.jettra.plugin.example.admin.repository.PerfilRepository.class,
       report = true,
       reportOrientation = "LANDSCAPE",
       reportTitle = "REPORTE DE PERFILES",
       reportHeaderColor = "#007BFF")
public interface PerfilPageDef {
}
