package com.jettra.plugin.example.library.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.library.model.PublisherModel;
import com.jettra.plugin.example.library.services.PublisherService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(extendsClass = DashboardBasePage.class, model = PublisherModel.class, controller = PublisherService.class, editable = true, report = true)
public interface PublisherPageDef {
}
