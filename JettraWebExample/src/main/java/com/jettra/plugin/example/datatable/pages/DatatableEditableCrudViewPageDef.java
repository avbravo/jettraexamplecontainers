package com.jettra.plugin.example.datatable.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.plugin.example.model.ArticuloModel.class, 
          repository = com.jettra.plugin.example.repository.ArticuloRepository.class,
          editable = true,
          report = true,
          reportOrientation = "PORTRAIT",
          reportTitle = "REPORTE DATATABLE EDITABLE @CrudView",
          reportHeaderColor = "#28a745")
public interface DatatableEditableCrudViewPageDef {
}
