package com.jettra.plugin.autentification.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.autentification.model.CredentialModel;
import com.jettra.plugin.autentification.services.CredentialService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = CredentialModel.class,
        controller = CredentialService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "REPORTE DE CREDENCIALES",
        reportHeaderColor = "#007BFF")
public interface CredentialPageDef {
}
