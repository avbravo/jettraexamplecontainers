package com.jettra.plugin.example.library.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.library.model.ReaderModel;
import com.jettra.plugin.example.library.services.ReaderService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(extendsClass = DashboardBasePage.class, model = ReaderModel.class, controller = ReaderService.class, editable = true, report = true)
public interface ReaderPageDef {
}
