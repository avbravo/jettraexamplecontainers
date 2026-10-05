package com.jettra.plugin.example.pages.kanban;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Board;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;
import io.jettra.wui.core.UIComponent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import io.jettra.wui.sync.JettraSyncManager;

@JettraPageSincronized(value = SyncType.ALL, entity = "KanbanCard")
public class KanbanPage extends DashboardBasePage {
    private static final String STORAGE_DIR = "kanban_data";
    private static final String[] COLUMNS = {"PENDIENTE", "PROGRESO", "FINALIZADO"};
    private static final String[] FILES = {"pendiente.md", "progreso.md", "finalizado.md"};

    public KanbanPage() {
        super("Kanban Board");
        ensureStorageExists();
    }

    private void ensureStorageExists() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
            for (String file : FILES) {
                Path p = Paths.get(STORAGE_DIR, file);
                if (!Files.exists(p)) {
                    Files.write(p, "| ID | Titulo | Descripcion | Grupo | Estimacion | Creador |\n| --- | --- | --- | --- | --- | --- |\n".getBytes());
                }
            }
        } catch (IOException e) {
            System.err.println("Error ensuring storage: " + e.getMessage());
        }
    }

    @Override
    protected void onPost(Map<String, String> params) {
        String action = params.get("action");
        boolean changed = false;
        String loggedUser = getLoggedUser(currentExchange);
        SyncType syncType = SyncType.ALL;
        
        if (action != null) {
            switch (action) {
                case "add": addCard(params, loggedUser); changed = true; syncType = SyncType.CREATE; break;
                case "edit": editCard(params); changed = true; syncType = SyncType.UPDATE; break;
                case "remove": removeCard(params); changed = true; syncType = SyncType.DELETE; break;
                case "move": moveCard(params); changed = true; syncType = SyncType.MOVE; break;
            }
        }
        
        if (changed) {
            JettraSyncManager.notifyChange("KanbanCard", syncType, loggedUser);
            try {
                redirect(currentExchange, io.jettra.server.JettraServer.resolvePath("/kanban"));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onPost(params);
    }

    private void addCard(Map<String, String> params, String loggedUser) {
        String title = params.get("title");
        String desc = params.get("description");
        String group = params.get("group");
        String est = params.get("estimation");
        String colName = params.get("column");

        int idx = Arrays.asList(COLUMNS).indexOf(colName);
        if (idx != -1) {
            List<CardData> cards = loadCards(FILES[idx]);
            CardData newCard = new CardData(UUID.randomUUID().toString(), title, desc, group, est, loggedUser);
            cards.add(newCard);
            saveCards(FILES[idx], cards);
        }
    }

    private void editCard(Map<String, String> params) {
        String id = params.get("cardId");
        String title = params.get("title");
        String desc = params.get("description");
        String group = params.get("group");
        String est = params.get("estimation");

        for (String file : FILES) {
            List<CardData> cards = loadCards(file);
            boolean found = false;
            for (CardData c : cards) {
                if (c.id.equals(id)) {
                    c.title = title;
                    c.description = desc;
                    c.group = group;
                    c.estimation = est;
                    found = true;
                    break;
                }
            }
            if (found) {
                saveCards(file, cards);
                break;
            }
        }
    }

    private void removeCard(Map<String, String> params) {
        String id = params.get("cardId");
        for (String file : FILES) {
            List<CardData> cards = loadCards(file);
            int initialSize = cards.size();
            cards.removeIf(c -> c.id.equals(id));
            if (cards.size() < initialSize) {
                saveCards(file, cards);
                break;
            }
        }
    }

    private void moveCard(Map<String, String> params) {
        String id = params.get("cardId");
        String targetColName = params.get("targetCol");
        int targetIdx = Arrays.asList(COLUMNS).indexOf(targetColName);
        
        CardData movingCard = null;
        for (int i = 0; i < FILES.length; i++) {
            List<CardData> cards = loadCards(FILES[i]);
            Iterator<CardData> it = cards.iterator();
            while (it.hasNext()) {
                CardData c = it.next();
                if (c.id.equals(id)) {
                    movingCard = c;
                    it.remove();
                    saveCards(FILES[i], cards);
                    break;
                }
            }
            if (movingCard != null) break;
        }

        if (movingCard != null && targetIdx != -1) {
            List<CardData> targetCards = loadCards(FILES[targetIdx]);
            targetCards.add(movingCard);
            saveCards(FILES[targetIdx], targetCards);
        }
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "20px");

        Header h = new Header(1, "Kanban Project Management");
        h.setStyle("margin-bottom", "20px");
        container.add(h);

        Board board = new Board("Global Workflow", 3);
        board.setId("kanban-board");

        for (int i = 0; i < COLUMNS.length; i++) {
            final String colName = COLUMNS[i];
            Div colHeader = new Div();
            colHeader.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
            
            Header ch = new Header(3, colName);
            ch.setStyle("margin", "0").setStyle("color", "var(--jettra-accent)");
            
            Button addBtn = new Button("+");
            addBtn.addClass("j-btn-primary").setStyle("padding", "2px 8px").setStyle("font-size", "14px");
            addBtn.setProperty("onclick", "openAddModal('" + colName + "')");
            
            colHeader.add(ch).add(addBtn);
            board.getColumn(i).add(colHeader);
            
            // Set drop handlers on column containers
            board.getColumn(i).setProperty("ondragover", "allowDrop(event)");
            board.getColumn(i).setProperty("ondrop", "drop(event, '" + colName + "')");
            board.getColumn(i).addClass("kanban-col-container");

            List<CardData> cards = loadCards(FILES[i]);
            for (CardData data : cards) {
                board.addComponent(i, createCardUI(data));
            }
        }

        container.add(board);
        center.add(container);

        setupKanbanModals(center);
        setupKanbanScripts(center);
    }

    private Div createCardUI(CardData data) {
        Div card = new Div();
        card.setId("card-" + data.id);
        card.addClass("kanban-card");
        card.setProperty("draggable", "true");
        card.setProperty("ondragstart", "drag(event)");
        card.setProperty("data-id", data.id);
        
        card.setStyle("background", "rgba(255,255,255,0.08)").setStyle("padding", "15px").setStyle("border-radius", "10px")
            .setStyle("border", "1px solid rgba(255,255,255,0.1)").setStyle("margin-bottom", "12px")
            .setStyle("cursor", "move").setStyle("transition", "all 0.2s");
        
        Div header = new Div();
        header.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "flex-start");
        
        Header t = new Header(5, data.title);
        t.setStyle("margin", "0 0 10px 0").setStyle("color", "#fff");
        
        Div actions = new Div();
        actions.setStyle("display", "flex").setStyle("gap", "5px");
        
        Button editBtn = new Button("✎");
        editBtn.setStyle("padding", "0 4px").setStyle("background", "transparent").setStyle("border", "none").setStyle("color", "#888").setStyle("cursor", "pointer");
        editBtn.setProperty("onclick", String.format("openEditModal('%s', '%s', '%s', '%s', '%s')", 
            data.id, data.title.replace("'","\\'"), data.description.replace("'","\\'"), data.group.replace("'","\\'"), data.estimation.replace("'","\\'")));
        
        Button delBtn = new Button("×");
        delBtn.setStyle("padding", "0 4px").setStyle("background", "transparent").setStyle("border", "none").setStyle("color", "#f44").setStyle("cursor", "pointer");
        delBtn.setProperty("onclick", "removeCard('" + data.id + "')");
        
        actions.add(editBtn).add(delBtn);
        header.add(t).add(actions);
        card.add(header);

        Paragraph p = new Paragraph(data.description);
        p.setStyle("font-size", "0.85rem").setStyle("margin-bottom", "10px").setStyle("opacity", "0.7");
        card.add(p);

        Div footer = new Div();
        footer.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("font-size", "0.7rem");
        
        Span g = new Span(data.group);
        g.setStyle("background", "rgba(0,255,255,0.1)").setStyle("color", "var(--jettra-accent)").setStyle("padding", "2px 6px").setStyle("border-radius", "4px");
        
        Div extraInfo = new Div();
        extraInfo.setStyle("display", "flex").setStyle("gap", "8px").setStyle("align-items", "center");
        
        Span usr = new Span("👤 " + (data.creator != null ? data.creator : "Unknown"));
        usr.setStyle("opacity", "0.6").setStyle("font-size", "0.65rem");
        
        Span e = new Span("⏱ " + data.estimation);
        e.setStyle("opacity", "0.5");
        
        extraInfo.add(usr).add(e);
        
        footer.add(g).add(extraInfo);
        card.add(footer);

        return card;
    }

    private void setupKanbanModals(Center center) {
        // Add/Edit Modal
        Modal modal = new Modal("kanban-modal");
        modal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)").setStyle("backdrop-filter", "blur(20px)")
             .setStyle("padding", "30px").setStyle("border-radius", "20px").setStyle("width", "400px").setStyle("border", "1px solid var(--jettra-border)")
             .setStyle("position", "fixed").setStyle("top", "15%").setStyle("left", "50%").setStyle("transform", "translate(-50%, 0)").setStyle("z-index", "1000")
             .setStyle("max-height", "80vh").setStyle("overflow-y", "auto");
        
        Header mh = new Header(3, "Task details");
        mh.setId("modal-title");
        mh.setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)");
        modal.add(mh);

        Form form = new Form("kanban-form", io.jettra.server.JettraServer.resolvePath("/kanban"));
        form.add(new TextBox("hidden", "action").setProperty("id", "form-action"));
        form.add(new TextBox("hidden", "cardId").setProperty("id", "form-card-id"));
        form.add(new TextBox("hidden", "column").setProperty("id", "form-column"));
        
        form.add(new Label("Title")).add(new TextBox("text", "title").setProperty("id", "form-title").addClass("j-input"));
        form.add(new Label("Description")).add(new TextArea("form-description", "").setProperty("name", "description").addClass("j-input").setStyle("height", "80px"));
        form.add(new Label("Group")).add(new TextBox("text", "group").setProperty("id", "form-group").addClass("j-input"));
        form.add(new Label("Estimation")).add(new TextBox("text", "estimation").setProperty("id", "form-estimation").addClass("j-input"));

        Div actions = new Div();
        actions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px").setStyle("margin-top", "10px");
        
        Button cancel = new Button("Cancel");
        cancel.addClass("j-btn").setProperty("type", "button").setProperty("onclick", "document.getElementById('kanban-modal').style.display='none'");
        
        Button save = new Button("Save Task");
        save.addClass("j-btn-primary").setProperty("type", "submit");
        
        actions.add(cancel).add(save);
        form.add(actions);
        modal.add(form);
        center.add(modal);

        // Hidden move form
        Form moveForm = new Form("move-form", io.jettra.server.JettraServer.resolvePath("/kanban"));
        moveForm.add(new TextBox("hidden", "action").setProperty("value", "move"));
        moveForm.add(new TextBox("hidden", "cardId").setProperty("id", "move-card-id"));
        moveForm.add(new TextBox("hidden", "targetCol").setProperty("id", "move-target-col"));
        center.add(moveForm);
        
        // Hidden remove form
        Form removeForm = new Form("remove-form", io.jettra.server.JettraServer.resolvePath("/kanban"));
        removeForm.add(new TextBox("hidden", "action").setProperty("value", "remove"));
        removeForm.add(new TextBox("hidden", "cardId").setProperty("id", "remove-card-id"));
        center.add(removeForm);
    }

    private void setupKanbanScripts(Center center) {
        Script script = new Script("""
            function openAddModal(col) {
                document.getElementById('modal-title').innerText = 'New Task in ' + col;
                document.getElementById('form-action').value = 'add';
                document.getElementById('form-column').value = col;
                document.getElementById('form-card-id').value = '';
                document.getElementById('form-title').value = '';
                document.getElementById('form-description').value = '';
                document.getElementById('form-group').value = '';
                document.getElementById('form-estimation').value = '';
                document.getElementById('kanban-modal').style.display = 'block';
            }
            function openEditModal(id, title, desc, group, est) {
                document.getElementById('modal-title').innerText = 'Edit Task';
                document.getElementById('form-action').value = 'edit';
                document.getElementById('form-card-id').value = id;
                document.getElementById('form-title').value = title;
                document.getElementById('form-description').value = desc;
                document.getElementById('form-group').value = group;
                document.getElementById('form-estimation').value = est;
                document.getElementById('kanban-modal').style.display = 'block';
            }
            function removeCard(id) {
                if (confirm('Remove this task?')) {
                    document.getElementById('remove-card-id').value = id;
                    document.getElementById('remove-form').submit();
                }
            }
            function allowDrop(ev) {
                ev.preventDefault();
            }
            function drag(ev) {
                ev.dataTransfer.setData("cardId", ev.target.closest('.kanban-card').getAttribute('data-id'));
            }
            function drop(ev, colName) {
                ev.preventDefault();
                const cardId = ev.dataTransfer.getData("cardId");
                document.getElementById('move-card-id').value = cardId;
                document.getElementById('move-target-col').value = colName;
                document.getElementById('move-form').submit();
            }
        """);
        center.add(script);
    }

    private List<CardData> loadCards(String file) {
        List<CardData> list = new ArrayList<>();
        try {
            Path path = Paths.get(STORAGE_DIR, file);
            if (!Files.exists(path)) return list;
            List<String> lines = Files.readAllLines(path);
            for (int i = 2; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.startsWith("|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 7) {
                        list.add(new CardData(parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim(), parts[6].trim()));
                    } else if (parts.length >= 6) {
                        list.add(new CardData(parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim(), "Unknown"));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading cards: " + e.getMessage());
        }
        return list;
    }

    private void saveCards(String file, List<CardData> cards) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("| ID | Titulo | Descripcion | Grupo | Estimacion | Creador |\n");
            sb.append("| --- | --- | --- | --- | --- | --- |\n");
            for (CardData c : cards) {
                sb.append(String.format("| %s | %s | %s | %s | %s | %s |\n", c.id, c.title, c.description, c.group, c.estimation, c.creator));
            }
            Files.write(Paths.get(STORAGE_DIR, file), sb.toString().getBytes());
        } catch (IOException e) {
            System.err.println("Error saving cards: " + e.getMessage());
        }
    }

    private static class CardData {
        String id, title, description, group, estimation, creator;
        CardData(String id, String title, String description, String group, String estimation, String creator) {
            this.id = id; this.title = title; this.description = description; this.group = group; this.estimation = estimation; this.creator = creator;
        }
    }
}
