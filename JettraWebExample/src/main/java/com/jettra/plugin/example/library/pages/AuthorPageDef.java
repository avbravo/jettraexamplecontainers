package com.jettra.plugin.example.library.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.library.model.AuthorModel;
import com.jettra.plugin.example.library.repository.AuthorRepository;
import com.jettra.plugin.example.library.services.AuthorService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = AuthorModel.class, 
        controller = AuthorService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "INFORME GLOBAL DE PLANETAS",
        reportHeaderColor = "#007BFF")
public interface AuthorPageDef {

    default void afterInitCenter(io.jettra.wui.complex.Center center, String username) {
        io.jettra.wui.components.Console console = new io.jettra.wui.components.Console("miConsola");
        center.add(console);
    }
}
