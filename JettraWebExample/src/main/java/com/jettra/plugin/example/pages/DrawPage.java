package com.jettra.plugin.example.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.core.UIComponent;

/**
 * Showcase page for the Draw component - Refactored for hierarchical design.
 */
public class DrawPage extends DashboardBasePage {

    public DrawPage() {
        super("Draw Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div mainWrapper = new Div();
        mainWrapper.setProperty("id", "draw-main-wrapper");
        mainWrapper.setStyle("display", "flex").setStyle("height", "calc(100vh - 100px)").setStyle("overflow", "hidden").setStyle("gap", "15px");

        // --- Left Palette ---
        Div sidebar = new Div();
        sidebar.setStyle("width", "300px").setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "15px");

        Div palette = new Div();
        palette.setProperty("id", "tool-palette");
        palette.addClass("j-3d-effect")
               .setStyle("flex", "1")
               .setStyle("background", "var(--jettra-glass)")
               .setStyle("backdrop-filter", "blur(20px)")
               .setStyle("border", "1px solid var(--jettra-border)")
               .setStyle("border-radius", "15px")
               .setStyle("padding", "20px")
               .setStyle("display", "flex")
               .setStyle("flex-direction", "column")
               .setStyle("gap", "15px")
               .setStyle("overflow-y", "auto");

        Header palTitle = new Header(4, "Designer Palette");
        palTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0").setStyle("font-size", "14px").setStyle("text-transform", "uppercase");
        palette.add(palTitle);

        String[][] allTools = {
            {"Container", "📦", "container", "rgba(0,255,255,0.05)"},
            {"Rectangle", "⬜", "rectangle", "rgba(0,255,255,0.2)"},
            {"Circle", "⭕", "circle", "rgba(0,255,255,0.2)"},
            {"Text", "A", "text", "transparent"}
        };

        Div toolsList = new Div();
        toolsList.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "12px");
        
        for (String[] tool : allTools) {
            Div toolItem = new Div();
            toolItem.addClass("palette-tool")
                    .setProperty("draggable", "true")
                    .setProperty("ondragstart", "event.dataTransfer.setData('toolType', '" + tool[2] + "')")
                    .setStyle("padding", "12px")
                    .setStyle("background", "rgba(255,255,255,0.03)")
                    .setStyle("border-radius", "10px")
                    .setStyle("cursor", "move")
                    .setStyle("display", "flex")
                    .setStyle("align-items", "center")
                    .setStyle("gap", "15px")
                    .setStyle("border", "1px solid rgba(0,255,255,0.1)");
            
            UIComponent icon = new UIComponent("span") {};
            icon.setContent(tool[1]);
            icon.setStyle("font-size", "18px");
            
            UIComponent name = new UIComponent("span") {};
            name.setContent(tool[0]);
            name.setStyle("font-size", "14px").setStyle("color", "#fff");
            
            toolItem.add(icon).add(name);
            toolsList.add(toolItem);
        }
        palette.add(toolsList);

        // --- Property Inspector ---
        Div inspector = new Div();
        inspector.setProperty("id", "property-inspector");
        inspector.addClass("j-3d-effect")
                 .setStyle("height", "300px")
                 .setStyle("background", "rgba(10,20,30,0.9)")
                 .setStyle("border", "1px solid var(--jettra-accent)")
                 .setStyle("border-radius", "15px")
                 .setStyle("padding", "20px")
                 .setStyle("display", "flex")
                 .setStyle("flex-direction", "column")
                 .setStyle("gap", "10px");

        Header insTitle = new Header(5, "Properties");
        insTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        inspector.add(insTitle);

        Div propForm = new Div();
        propForm.setProperty("id", "inspector-fields");
        propForm.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "10px");
        inspector.add(propForm);

        sidebar.add(palette).add(inspector);

        // --- Main Canvas Area ---
        Div canvasArea = new Div();
        canvasArea.setStyle("flex", "1").setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("position", "relative");
        
        Div dropTarget = new Div();
        dropTarget.setProperty("id", "draw-canvas");
        dropTarget.setStyle("flex", "1").setStyle("position", "relative").setStyle("overflow", "auto").setStyle("border-radius", "15px").setStyle("border", "2px dashed var(--jettra-accent)").setStyle("background", "rgba(0,0,0,0.2)");
        dropTarget.setProperty("ondragover", "event.preventDefault()");
        dropTarget.setProperty("ondrop", "handleCanvasDrop(event)");
        dropTarget.setProperty("onclick", "deselectAll()");

        canvasArea.add(dropTarget);

        mainWrapper.add(sidebar).add(canvasArea);

        // --- JS Support ---
        UIComponent script = new UIComponent("script") {};
        script.setContent("""
            var selectedId = null;

            function handleCanvasDrop(event) {
                event.preventDefault();
                event.stopPropagation();
                
                const type = event.dataTransfer.getData('toolType');
                const moveId = event.dataTransfer.getData('moveId');
                const canvas = document.getElementById('draw-canvas');
                
                // Target: the element dropped onto, or the canvas
                let target = event.target.closest('.draw-item') || canvas;
                
                if (moveId) {
                    // Moving existing element
                    const el = document.getElementById(moveId);
                    if (el && target !== el && !el.contains(target)) {
                        target.appendChild(el);
                        // Update position relative to new target
                        const rect = target.getBoundingClientRect();
                        el.style.left = (event.clientX - rect.left - 50) + 'px';
                        el.style.top = (event.clientY - rect.top - 25) + 'px';
                    }
                } else if (type) {
                    // Creating new element
                    const el = document.createElement('div');
                    el.id = 'draw_' + Date.now();
                    el.className = 'draw-item ' + type;
                    el.draggable = true;
                    el.style.position = 'absolute';
                    el.style.width = type === 'text' ? 'auto' : '100px';
                    el.style.height = type === 'text' ? 'auto' : '50px';
                    el.style.padding = '10px';
                    el.style.border = '1px solid rgba(0,255,255,0.4)';
                    el.style.borderRadius = type === 'circle' ? '50%' : '8px';
                    el.style.background = type === 'text' ? 'transparent' : 'rgba(0,255,255,0.1)';
                    el.style.color = '#fff';
                    el.style.cursor = 'move';
                    el.style.boxSizing = 'border-box';
                    el.style.display = 'flex';
                    el.style.alignItems = 'center';
                    el.style.justifyContent = 'center';
                    el.style.minWidth = '20px';
                    el.style.minHeight = '20px';
                    
                    if (type === 'text') {
                        el.innerText = 'New Text';
                        el.style.border = '1px dashed rgba(255,255,255,0.2)';
                    } else if (type === 'container') {
                        el.style.width = '200px';
                        el.style.height = '150px';
                        el.style.border = '2px solid var(--jettra-accent)';
                        el.style.alignItems = 'flex-start';
                        el.style.justifyContent = 'flex-start';
                    }

                    const rect = target.getBoundingClientRect();
                    el.style.left = (event.clientX - rect.left - (type==='text'?0:50)) + 'px';
                    el.style.top = (event.clientY - rect.top - (type==='text'?0:25)) + 'px';

                    el.ondragstart = (e) => {
                        e.dataTransfer.setData('moveId', el.id);
                        e.stopPropagation();
                    };
                    
                    el.onclick = (e) => {
                        e.stopPropagation();
                        selectItem(el);
                    };

                    target.appendChild(el);
                    selectItem(el);
                }
            }

            function selectItem(el) {
                deselectAll();
                selectedId = el.id;
                el.style.boxShadow = '0 0 15px var(--jettra-glow)';
                el.style.borderColor = 'var(--jettra-accent)';
                updateInspector();
            }

            function deselectAll() {
                selectedId = null;
                document.querySelectorAll('.draw-item').forEach(it => {
                    it.style.boxShadow = 'none';
                    it.style.borderColor = it.classList.contains('container') ? 'var(--jettra-accent)' : 'rgba(0,255,255,0.4)';
                });
                updateInspector();
            }

            function updateInspector() {
                const fields = document.getElementById('inspector-fields');
                if (!selectedId) {
                    fields.innerHTML = '<span style="color:#666; font-size:12px;">Select an item to edit</span>';
                    return;
                }
                const el = document.getElementById(selectedId);
                const isText = el.classList.contains('text');
                
                let html = '';
                
                // Color de Fondo
                if (!isText) {
                    html += `
                        <label style="color:#aaa; font-size:11px;">Background Color</label>
                        <input type="color" value="${rgbToHex(el.style.backgroundColor)}" onchange="updateProp('background', this.value)" style="width:100%; height:30px; border:none; background:transparent; cursor:pointer;">
                    `;
                }

                // Color de Letra
                html += `
                    <label style="color:#aaa; font-size:11px;">Text Color</label>
                    <input type="color" value="${rgbToHex(el.style.color)}" onchange="updateProp('color', this.value)" style="width:100%; height:30px; border:none; background:transparent; cursor:pointer;">
                `;

                if (isText) {
                    html += `
                        <label style="color:#aaa; font-size:11px;">Text Content</label>
                        <input type="text" value="${el.innerText}" oninput="updateProp('text', this.value)" class="j-input" style="font-size:12px;">
                    `;
                }
                
                html += `
                    <button onclick="document.getElementById('${selectedId}').remove(); deselectAll();" class="j-btn-danger" style="margin-top:20px; width:100%; font-size:11px;">Delete Item</button>
                `;

                fields.innerHTML = html;
            }

            function updateProp(prop, value) {
                if (!selectedId) return;
                const el = document.getElementById(selectedId);
                if (prop === 'background') el.style.backgroundColor = value;
                if (prop === 'color') el.style.color = value;
                if (prop === 'text') el.innerText = value;
            }

            function rgbToHex(rgb) {
                if (!rgb || rgb === 'transparent') return '#000000';
                if (rgb.startsWith('#')) return rgb;
                const match = rgb.match(/\\\\d+/g);
                if (!match) return '#000000';
                const r = parseInt(match[0]);
                const g = parseInt(match[1]);
                const b = parseInt(match[2]);
                return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
            }
        """);

        center.add(mainWrapper).add(script);
    }
}
