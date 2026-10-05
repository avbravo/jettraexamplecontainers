package com.jettra.plugin.autentification.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.autentification.model.RoleModel;
import com.jettra.plugin.autentification.services.RoleService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = RoleModel.class,
        controller = RoleService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "REPORTE DE ROLES",
        reportHeaderColor = "#007BFF")
public interface RolePageDef {
}
