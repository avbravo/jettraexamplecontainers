package com.jettra.plugin.example.cruview.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.plugin.example.rules.model.ReglasModel.class, 
          repository = com.jettra.plugin.example.crud.repository.ReglasRepository.class,
          report = true,
          reportOrientation = "PORTRAIT",
          reportTitle = "REPORTE DE REGLAS Y COMPUTOS",
          reportHeaderColor = "#28a745")
public interface ReglasViewCrudPageDef {
}
