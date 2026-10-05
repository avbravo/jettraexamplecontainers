package com.jettra.plugin.example.library.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import com.jettra.plugin.example.library.model.BookModel;
import com.jettra.plugin.example.library.services.BookService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(extendsClass = DashboardBasePage.class, model = BookModel.class, controller = BookService.class, editable = true, report = true)
public interface BookPageDef {
}
