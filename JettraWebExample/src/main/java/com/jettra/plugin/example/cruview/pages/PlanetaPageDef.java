package com.jettra.plugin.example.cruview.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.plugin.example.crudview.model.PlanetaModel.class, repository = com.jettra.plugin.example.crud.repository.PlanetaRepository.class,
       report = true,
       reportOrientation = "LANDSCAPE",
       reportTitle = "INFORME GLOBAL DE PLANETAS",
       reportHeaderColor = "#007BFF")
public interface PlanetaPageDef {
}
