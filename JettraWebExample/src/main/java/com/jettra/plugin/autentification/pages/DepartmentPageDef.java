package com.jettra.plugin.autentification.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.autentification.model.DepartmentModel;
import com.jettra.plugin.autentification.services.DepartmentService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = DepartmentModel.class,
        controller = DepartmentService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "REPORTE DE DEPARTAMENTOS",
        reportHeaderColor = "#007BFF")
public interface DepartmentPageDef {
}
