    // Designer GLOBALS
    function drag(ev) {
        ev.dataTransfer.setData("type", ev.currentTarget.getAttribute("data-type"));
        ev.dataTransfer.effectAllowed = "move";
    };
    function allowDrop(ev) {
        ev.preventDefault();
    };
    function drop(ev) {
        ev.preventDefault();
        const type = ev.dataTransfer.getData("type");
        const canvas = document.getElementById('canvas-drop-area');
        if (canvas) {
            window.addComponentToCanvas(type, canvas);
        }
    };

    // Expose to window for backwards compatibility with inline attributes
    window.drag = drag;
    window.allowDrop = allowDrop;
    window.drop = drop;

    var selectedItem = null;
    var currentModel = null;
    var modelFields = [];
    var availableModels = [];
    var viewModelName = "BaseViewModel";
    var activeEventProperty = null;
    var projectFilesMap = {};
    window.isSyncing = false;

    setInterval(() => {
        document.querySelectorAll('.live-clock').forEach(el => {
            el.innerText = new Date().toLocaleTimeString();
        });
    }, 1000);

    window.toggleCodeView = function() {
        const cv = document.getElementById('code-view-container');
        if(cv.style.display === 'none') {
            cv.style.display = 'flex';
        } else {
            cv.style.display = 'none';
        }
    };

    function setupCanvasHandlers() {
        const canvas = document.getElementById('canvas-drop-area');
        if (!canvas) return;

        // Ensure event listeners are attached correctly for capturing drop inside ANY container
        document.body.ondragover = function(ev) {
            if (ev.target.closest('#canvas-drop-area')) {
                ev.preventDefault();
                ev.dataTransfer.dropEffect = "move";
            }
        };

        document.body.ondrop = function(ev) {
            const canvasSection = ev.target.closest('#canvas-drop-area');
            if (!canvasSection) return;

            ev.preventDefault();
            ev.stopPropagation();

            const type = ev.dataTransfer.getData("type");
            const moveId = ev.dataTransfer.getData("move-id");
            const target = ev.target.closest('.canvas-container') || canvasSection;

            if (target) {
                const targetWrapper = target.closest('.canvas-item');
                if (targetWrapper && targetWrapper.getAttribute('data-type') === 'RadioGroupButton') {
                    let drpType = type;
                    if (moveId) {
                        const el = document.getElementById(moveId);
                        if (el) drpType = el.getAttribute('data-type');
                    }
                    if (drpType && drpType !== 'RadioButton') {
                        window.show3DMessage("Invalid Action", "Only RadioButton components can be nested inside a RadioGroupButton.");
                        return;
                    }
                }
                if (targetWrapper && targetWrapper.getAttribute('data-type') === 'CheckBoxGroup') {
                    let drpType = type;
                    if (moveId) {
                        const el = document.getElementById(moveId);
                        if (el) drpType = el.getAttribute('data-type');
                    }
                    if (drpType && drpType !== 'CheckBox') {
                        window.show3DMessage("Invalid Action", "Only CheckBox components can be nested inside a CheckBoxGroup.");
                        return;
                    }
                }
            }

            if (moveId) {
                const el = document.getElementById(moveId);
                if (el && target !== el && !el.contains(target)) {
                    target.appendChild(el);
                    window.updateGeneratedCode();
                }
            } else if (type) {
                window.addComponentToCanvas(type, target);
            }
        };
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            setupCanvasHandlers();
            window.restoreFilesFromCache();
        });
    } else {
        setupCanvasHandlers();
        window.restoreFilesFromCache();
    }

    window.addComponentToCanvas = function(type, parent, externalProps) {
        const canvas = document.getElementById('canvas-drop-area');
        const targetParent = parent || canvas;
        if (!targetParent) return;

        const placeholder = document.querySelector('.canvas-placeholder');
        if (placeholder) placeholder.style.display = 'none';

        if (type === 'Modal' && targetParent.id !== 'modal-list-container' && !targetParent.closest('#modal-list-container')) {
            const modalIdGen = 'modal_' + Math.floor(Math.random()*10000);
            window.addComponentToCanvas('Button', targetParent, { text: "Open Modal", columns: 2, events: {onClick: `e -> { \n    document.getElementById("${modalIdGen}").style.display = "block"; \n}`}, binding: "" });
            const mt = document.getElementById('modal-list-container');
            if(mt) window.addComponentToCanvas('Modal', mt, Object.assign({idGen: modalIdGen}, externalProps || {}));
            return;
        }

        const wrapper = document.createElement('div');
        wrapper.className = 'canvas-item';
        wrapper.id = 'ci_' + Date.now() + Math.floor(Math.random()*1000); // Important for inner dnd
        wrapper.setAttribute('data-type', type);

        let defaultProps = externalProps || {text: type, columns: 2, events: {}, binding: ""};
        wrapper.setAttribute('data-props', JSON.stringify(defaultProps));
        wrapper.setAttribute('draggable', 'true');

        wrapper.ondragstart = function(ev) {
            ev.dataTransfer.setData("move-id", ev.target.id);
            ev.dataTransfer.effectAllowed = "move";
            ev.stopPropagation(); // Stop parent from reacting
        };

        wrapper.onclick = (e) => { e.stopPropagation(); selectElement(wrapper); };
        wrapper.oncontextmenu = (e) => { e.preventDefault(); e.stopPropagation(); selectElement(wrapper); showInspector(); };

        let content = '';
        switch(type) {
            case 'Clock': content = '<div class="j-component live-clock" style="font-size: 20px; font-weight: bold; color: #0f0; background: #000; padding: 10px; border-radius: 8px; border: 2px solid #333; display:inline-block; font-family: monospace;">' + new Date().toLocaleTimeString() + '</div>'; break;
            case 'Header': content = '<h2 style="margin:0; color:var(--jettra-text);">New Header</h2>'; break;
            case 'Paragraph': content = '<p style="margin:0; color:var(--jettra-text);">Sample text...</p>'; break;
            case 'Span': content = '<span style="color:var(--jettra-text);">Span Text</span>'; break;
            case 'Label': content = '<label style="color:var(--jettra-text); font-weight:bold;">Label Text</label>'; break;
            case 'Divide': content = '<div style="border-top:1px solid var(--jettra-border); margin:15px 0; width:100%"></div>'; break;
            case 'Separator': content = '<hr style="border: 0; border-top: 1px dashed var(--jettra-border); margin:15px 0; width:100%;">'; break;
            case 'Button': content = '<button class="j-btn j-btn-primary" type="button">Interactive Button</button>'; break;
            case 'TextBox': content = '<input type="text" class="j-input" placeholder="TextBox..." onfocus="this.blur()">'; break;
            case 'TextArea': content = '<textarea class="j-input" placeholder="TextArea..." rows="3" onfocus="this.blur()"></textarea>'; break;
            case 'CheckBox': content = '<div style="display:flex; align-items:center; gap:8px;"><input type="checkbox" checked onfocus="this.blur()"/><label>CheckBox</label></div>'; break;
            case 'RadioButton': content = '<div style="display:flex; align-items:center; gap:8px;"><input type="radio" checked onfocus="this.blur()"/><label>RadioButton</label></div>'; break;
            case 'RadioGroupButton': content = '<div class="canvas-container" style="padding:10px; border:1px dashed var(--jettra-accent); min-height:60px;"><label style="font-weight:bold; color:var(--jettra-accent); display:block; margin-bottom:10px;">Radio Group</label></div>'; break;
            case 'CheckBoxGroup': content = '<div class="canvas-container" style="padding:10px; border:1px dashed var(--jettra-accent); min-height:60px;"><label style="font-weight:bold; color:var(--jettra-accent); display:block; margin-bottom:10px;">CheckBox Group</label></div>'; break;
            case 'ScheduleControl': content = '<input type="datetime-local" class="j-input" onfocus="this.blur()"/>'; break;
            case 'SelectOne': content = '<div class="select-wrapper"><label style="display:block; font-size:11px; margin-bottom:4px; color:var(--jettra-accent)">Select</label><select class="j-input" onfocus="this.blur()"><option>Option 1...</option></select></div><span style="display:none">SelectOne</span>'; break;
            case 'SelectMany': content = '<div class="select-wrapper"><label style="display:block; font-size:11px; margin-bottom:4px; color:var(--jettra-accent)">Select Multiple</label><select class="j-input" multiple="multiple" style="height:80px" onfocus="this.blur()"><option>Option 1...</option></select></div><span style="display:none">SelectMany</span>'; break;
            case 'SelectOneIcon': content = '<select class="j-input" onfocus="this.blur()"><option>⭐ Option 1...</option></select><span style="display:none">SelectOneIcon</span>'; break;
            case 'Spinner': content = '<div class="j-spinner-wrapper" style="display:inline-flex; align-items:center; border:1px solid var(--jettra-border); border-radius:8px; background:rgba(0,0,0,0.3); overflow:hidden;"><button type="button" class="j-spinner-btn j-spinner-minus" style="width:40px;height:40px;background:rgba(255,255,255,0.05);border:none;color:var(--jettra-accent);font-size:1.2rem;font-weight:bold;">-</button><div class="j-spinner-display" style="min-width:60px;text-align:center;font-family:monospace;font-size:1.1rem;color:var(--jettra-text);">0</div><button type="button" class="j-spinner-btn j-spinner-plus" style="width:40px;height:40px;background:rgba(255,255,255,0.05);border:none;color:var(--jettra-accent);font-size:1.2rem;font-weight:bold;">+</button></div>'; break;
            case 'ToggleSwitch': content = '<div style="display:flex; align-items:center; gap:8px;"><div style="width:40px;height:20px;background:var(--jettra-accent);border-radius:10px;position:relative;"><div style="width:16px;height:16px;background:#fff;border-radius:50%;position:absolute;top:2px;right:2px;"></div></div><label>ToggleSwitch</label></div>'; break;
            case 'FileUpload': content = '<div style="border:1px dashed var(--jettra-accent); padding:20px; text-align:center; border-radius:8px; color:var(--jettra-text);"><div style="font-size:24px; margin-bottom:10px;">☁️</div><span>Click or drag files here to upload</span></div>'; break;
            case 'FolderSelector': content = '<div style="border:1px dashed var(--jettra-accent); padding:20px; text-align:center; border-radius:8px; color:var(--jettra-text);"><div style="font-size:24px; margin-bottom:10px;">📁</div><span>Select Directory</span></div>'; break;
            case 'OTPValidator': content = '<div class="j-component" style="display:flex; justify-content:center; gap:5px; padding:10px;"><input disabled style="width:30px; height:40px; text-align:center;" value="*"/><input disabled style="width:30px; height:40px; text-align:center;" value="*"/><input disabled style="width:30px; height:40px; text-align:center;" value="*"/><input disabled style="width:30px; height:40px; text-align:center;" value="*"/></div>'; break;
            case 'Catcha': content = '<div class="j-component" style="padding:10px; border:1px solid #aaa; border-radius:4px; display:inline-flex; align-items:center; gap:10px; background:#f9f9f9;"><input type="checkbox" disabled/> <span style="color:#333; font-family:sans-serif">I&#39;m not a robot</span></div>'; break;
            case 'CreditCard': content = '<div class="j-component" style="padding:15px; border:1px solid rgba(0,255,255,0.2); border-radius:12px; background:linear-gradient(145deg, #1e293b, #0f172a); min-height:100px; display:flex; flex-direction:column; gap:10px; width:280px;"><div style="font-family:monospace; font-size:16px; color:#fff; letter-spacing:2px; margin-top:20px;">•••• •••• •••• ••••</div><div style="display:flex; justify-content:space-between; color:#94a3b8; font-size:10px;"><span class="cc-name-mock">NAME SURNAME</span><span>MM/YY</span></div><button class="j-btn j-btn-primary" style="margin-top:10px; width:100%; border-radius:8px; padding:10px;" disabled>Pay Now</button><div class="canvas-container" style="min-height:30px; border:1px dashed rgba(255,255,255,0.1); margin-top:5px; padding:5px;"></div></div>'; break;
            case 'Link': content = '<a href="javascript:void(0)" style="color:var(--jettra-accent); text-decoration:underline;"><span>Link Text</span></a>'; break;
            case 'Menu': content = '<div style="background:rgba(0,0,0,0.4); padding:10px; border-radius:4px; display:inline-block;"><div style="padding:8px 15px; cursor:pointer;"><span>Menu Item</span></div></div>'; break;
            case 'MenuBar': content = '<div class="canvas-container" style="background:rgba(0,0,0,0.4); padding:10px; border-radius:4px; display:flex; gap:15px; min-height:45px; border:1px dashed rgba(255,255,255,0.2);"></div>'; break;
            case 'MenuItem': content = '<div class="j-component" style="padding:10px; cursor:pointer; border-bottom:1px solid rgba(255,255,255,0.1); background:rgba(0,0,0,0.2);"><span>Menu Item</span></div>'; break;
            case 'Loading': content = '<div class="j-loading" style="display:inline-flex; align-items:center; justify-content:center; padding:10px;"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="var(--jettra-accent)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83" /></svg></div>'; break;
            case 'Alert': content = '<div style="background:rgba(255,0,0,0.1); border-left:4px solid #ff4444; padding:15px; border-radius:4px; color:#ff4444; display:flex; align-items:center; gap:10px;"><b>\u26A0\uFE0F</b><span>Alert Message</span></div>'; break;
            case 'Notification': content = '<div style="background:rgba(0,255,255,0.1); border:1px solid var(--jettra-accent); padding:15px; border-radius:8px; color:var(--jettra-text); max-width:300px; box-shadow:0 4px 12px rgba(0,0,0,0.3);"><span>Notification Message</span></div>'; break;
            case 'Downloader': content = '<div style="display:inline-flex; align-items:center; gap:8px; padding:6px 12px; background:rgba(255,255,255,0.1); border-radius:4px; cursor:pointer;"><b>💾</b><span>Download File</span></div>'; break;
            case 'PDFViewer': content = '<div style="border:1px solid #aaa; background:#eee; color:#333; height:200px; display:flex; align-items:center; justify-content:center; border-radius:4px;"><span>PDF Document Preview</span></div>'; break;
            case 'ViewMedia': content = '<div style="background:#000; color:#fff; height:200px; display:flex; align-items:center; justify-content:center; border-radius:4px;"><span>▶ Media Player</span></div>'; break;
            case 'BarCode': content = '<div class="j-component" style="padding:10px; text-align:center; border:1px dashed #fff;"><span style="font-family:monospace; font-size:24px; letter-spacing:2px">||| | || |||</span><br><span>BarCode</span></div>'; break;
            case 'Carousel': content = '<div style="display:flex; gap:10px; overflow:hidden; padding:10px; background:rgba(0,0,0,0.2); border-radius:8px;"><div style="width:150px; height:80px; background:rgba(0,255,255,0.1); border:1px solid rgba(0,255,255,0.3); border-radius:8px; display:flex; align-items:center; justify-content:center;"><span>Slide 1</span></div></div>'; break;
            case 'TabView': content = '<div style="border:1px solid rgba(0,255,255,0.2); border-radius:8px; overflow:hidden;"><div style="display:flex; background:rgba(0,0,0,0.3); border-bottom:1px solid rgba(0,255,255,0.2);" class="tab-headers"><span style="padding:10px; color:#aaa; font-size:11px;">[Tab Headers]</span></div><div class="canvas-container" style="padding:10px; min-height:100px; background:rgba(0,255,255,0.02);"></div></div>'; break;
            case 'Tab': content = '<div class="canvas-container" style="border:1px dashed var(--jettra-accent); min-height:80px; padding:10px; position:relative; background:rgba(0,0,0,0.4); margin-bottom:10px;"><span style="position:absolute; top:-12px; left:10px; background:var(--jettra-accent); color:#000; padding:2px 8px; border-radius:4px; font-size:10px; font-weight:bold;">Tab Title</span></div>'; break;
            case 'Tree': content = '<div class="canvas-container" style="padding:10px; border:1px solid rgba(255,255,255,0.1); border-radius:4px; background:rgba(0,0,0,0.2); min-height:50px;"></div>'; break;
            case 'TreeItem': content = '<div class="canvas-container" style="padding-left:15px; border-left:1px dashed rgba(0,255,255,0.2); min-height:30px; margin-top:5px; position:relative;"><span style="position:absolute; left:-10px; top:5px; font-size:10px; color:var(--jettra-accent);">▶</span><span style="display:inline-block; margin-bottom:5px; color:#fff;">Tree Node</span></div>'; break;
            case 'Div': content = '<div class="canvas-container" style="border:1px dashed rgba(255,255,255,0.2); min-height:50px; padding:10px; border-radius:4px; position:relative;"><span style="position:absolute;top:2px;left:5px;font-size:9px;color:rgba(255,255,255,0.3)">Div Container</span></div>'; break;
            case 'LayoutDisplay': content = '<div class="canvas-container" style="border:2px solid rgba(0,255,255,0.2); min-height:100px; padding:15px; border-radius:8px; position:relative;"><span style="position:absolute;top:5px;left:10px;font-size:10px;color:rgba(0,255,255,0.5);font-weight:bold;">LayoutDisplay</span></div>'; break;
            case 'DatePicker': content = '<div style="display:flex; flex-direction:column; gap:5px;"><label style="font-weight:500; font-size:0.9rem;">Date Selection</label><input type="datetime-local" class="j-input" onfocus="this.blur()"/></div>'; break;
            case 'Time': content = '<div style="display:flex; flex-direction:column; gap:5px;"><label style="font-weight:500; font-size:0.9rem;">Time Selection</label><input type="time" class="j-input" onfocus="this.blur()"/></div>'; break;
            case 'Map': content = '<div style="width:100%; height:200px; background:rgba(0,255,0,0.1); border:1px dashed #0f0; border-radius:8px; display:flex; align-items:center; justify-content:center; color:#0f0;"><span>🗺 Leaflet Map Area</span></div>'; break;
            case 'Calendar': content = '<div style="width:100%; min-height:150px; background:rgba(0,0,0,0.3); border:1px solid var(--jettra-border); border-radius:8px; padding:10px;"><div style="text-align:center; color:var(--jettra-accent); margin-bottom:5px;">[Month Calendar Placeholder]</div><div style="display:grid; grid-template-columns:repeat(7, 1fr); gap:2px; opacity:0.5;"><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div></div></div>'; break;
            case 'Schedule': content = '<div style="width:100%; min-height:100px; background:rgba(0,0,0,0.3); border:1px solid var(--jettra-border); border-radius:8px; padding:10px;"><div style="text-align:center; color:var(--jettra-accent); margin-bottom:5px;">[Weekly Schedule Placeholder]</div><div style="border-left:2px solid var(--jettra-border); padding-left:10px;"><div style="background:var(--jettra-accent); color:#000; font-size:10px; width:80px; padding:2px; margin-bottom:5px;">Event</div></div></div>'; break;
            case 'Timeline': content = '<div style="border-left:2px solid var(--jettra-border); padding-left:20px; position:relative; min-height:100px;"><div style="position:absolute; left:-6px; top:10px; width:10px; height:10px; border-radius:50%; background:var(--jettra-accent);"></div><div style="background:rgba(255,255,255,0.05); padding:10px; border-radius:4px; margin-top:5px;">Timeline Node</div></div>'; break;
            case 'Organigram': content = '<div style="text-align:center; padding:10px;"><div style="display:inline-block; padding:10px; border:1px solid var(--jettra-accent); border-radius:6px; background:rgba(0,255,255,0.05);">Root Node</div><div style="border-left:1px dashed var(--jettra-border); height:20px; margin:0 auto; width:1px;"></div><div style="display:inline-block; padding:10px; border:1px solid var(--jettra-border); border-radius:6px; opacity:0.8;">Child Node</div></div>'; break;
            case 'Panel':
                content = '<div class="j-component j-panel" style="padding:0; overflow:hidden;"><div class="j-panel-header" style="background:rgba(0,255,255,0.05); padding:10px 15px; border-bottom:1px solid var(--jettra-border); font-weight:bold; color:var(--jettra-accent)">Panel Title</div><div class="canvas-container j-panel-body" style="padding:15px; display:flex; flex-direction:column; gap:10px; min-height:50px;"></div></div>';
                break;
            case 'Grid':
                content = '<div class="canvas-container" style="display:grid; grid-template-columns:1fr 1fr; gap:15px; min-height:50px;"></div>';
                break;
            case 'Modal':
                content = '<div class="canvas-container modal-container-mock j-component" style="padding:20px; border-radius:12px; min-height:100px; transform:translateZ(10px); box-shadow:0 15px 40px rgba(0,0,0,0.5);"><h3 style="margin-top:0; color:var(--jettra-accent); border-bottom:1px solid rgba(0,255,255,0.2); padding-bottom:10px;">Modal Component</h3></div>';
                break;
            case 'Board':
                content = '<div class="canvas-container board-container-mock j-component" style="padding:20px; background:rgba(15,23,42,0.4);"><h2 style="margin-top:0; color:var(--jettra-accent); font-size:18px; margin-bottom:15px;">New Board</h2><div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:15px; min-height:100px;"><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div></div></div>';
                break;
            case 'Card':
                content = '<div class="j-component j-card" style="padding:0; border:1px solid var(--jettra-accent); border-radius:8px; background:rgba(0,0,0,0.4); display:flex; flex-direction:column; gap:10px;"><div style="padding:15px; border-bottom:1px solid rgba(255,255,255,0.1);"><h3 style="margin:0;color:var(--jettra-accent);" class="card-title-mock">Card Title</h3><p style="margin:0;font-size:12px;color:#aaa" class="card-subtitle-mock"></p><p style="margin:5px 0 0;font-size:13px;color:rgba(255,255,255,0.8)" class="card-content-mock"></p></div><div class="canvas-container" style="flex:1; border:0; min-height:50px; padding:15px; background:transparent;"></div></div>';
                break;
            case 'Table':
                content = '<div class="j-table-container j-component" style="padding:0; border-radius:12px; overflow:hidden;"><table style="width:100%; border-collapse:collapse; text-align:left; font-size:0.9rem;"><thead style="background:rgba(0,255,255,0.05); border-bottom:1px solid var(--jettra-accent);"><tr><th style="padding:12px; color:var(--jettra-text);">Col 1</th><th style="padding:12px; color:var(--jettra-text);">Col 2</th></tr></thead><tbody><tr style="border-bottom:1px solid var(--jettra-border);"><td style="padding:10px; color:var(--jettra-text);">Data 1</td><td style="padding:10px; color:var(--jettra-text);">Data 2</td></tr></tbody></table></div>';
                break;
            case 'Image':
                content = '<div class="j-component" style="padding:0; overflow:hidden; display:flex; align-items:center; justify-content:center; background:rgba(0,0,0,0.2); min-height:100px; border-radius:8px;"><span style="color:#666;">[Image Placeholder]</span></div>';
                break;
            case 'Avatar':
                content = '<div class="j-avatar j-avatar-circle j-avatar-md" style="background:var(--jettra-accent); color:#000; display:flex; align-items:center; justify-content:center; font-weight:bold">AV</div>';
                break;
            case 'ProgressBar':
                content = '<div class="j-progressbar-container" style="width:100%; height:12px; background:rgba(255,255,255,0.05); border-radius:6px; overflow:hidden; border:1px solid var(--jettra-border)">' +
                          '<div class="j-progressbar-fill" style="width:60%; height:100%; background:var(--jettra-accent); box-shadow:0 0 10px var(--jettra-accent)"></div>' +
                          '</div>';
                break;
            case 'ChartsBar': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #36a2eb"><span style="color:#36a2eb;font-size:2rem;">📊 Bar Chart</span></div>'; break;
            case 'ChartsDoughnut': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:50%;border:2px dashed #ff6384;width:100px;margin:auto;"><span style="color:#ff6384;font-size:2rem;">🍩</span></div>'; break;
            case 'ChartsLine': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #4bc0c0"><span style="color:#4bc0c0;font-size:2rem;">📈 Line Chart</span></div>'; break;
            case 'ChartsPie': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:50%;border:1px solid #ffcd56;width:100px;margin:auto;"><span style="color:#ffcd56;font-size:2rem;">🦷</span></div>'; break;
            case 'ChartsRadar': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #9966ff"><span style="color:#9966ff;font-size:2rem;">🕸️ Radar Chart</span></div>'; break;
            default: content = `<div class="canvas-container j-component" style="padding:10px; text-align:center; color:#888;">${type} Component</div>`;
        }

        wrapper.innerHTML = content + '<div class="delete-tool" onclick="this.parentElement.remove(); window.updateGeneratedCode();">×</div>';
        targetParent.appendChild(wrapper);
        window.updateGeneratedCode();
    };

    window.selectElement = function(el) {
        if (selectedItem) selectedItem.classList.remove('selected');
        selectedItem = el;
        selectedItem.classList.add('selected');
        window.updateInspector();
    };

    window.showInspector = function() {
        const inspector = document.getElementById('property-inspector');
        if (inspector) inspector.style.display = 'flex';
    };

    window.updateInspector = function() {
        if (!selectedItem) return;
        const type = selectedItem.getAttribute('data-type');
        const props = JSON.parse(selectedItem.getAttribute('data-props'));
        const inspector = document.getElementById('inspector-properties');
        if (!inspector) return;

        let html = `
            <div class="view-model-row">
                <span class="inspector-label">Active View Model</span>
                <select class="inspector-input" onchange="selectModel(this.value)">
                    <option value="">None</option>
                    ${availableModels.map(m => `<option value="${m.name}" ${viewModelName === m.name ? 'selected' : ''}>${m.name}</option>`).join('')}
                </select>
            </div>
        `;

        html += `
            <div class="inspector-row">
                <span class="inspector-label">Component Type</span>
                <div style="color:var(--jettra-accent); font-weight:bold">${type}</div>
            </div>
            <div class="inspector-row">
                <span class="inspector-label">ID (Variables)</span>
                <input type="text" class="inspector-input" value="${props.id || ""}" placeholder="Comp ID" onchange="updateProp('id', this.value)">
            </div>
            <div class="inspector-row">
                <span class="inspector-label">Text Content</span>
                <input type="text" class="inspector-input" value="${props.text || ""}" onchange="updateProp('text', this.value)">
            </div>
        `;

        if (type === 'SelectOne' || type === 'SelectMany') {
            const labelEl = selectedItem.querySelector('label');
            const selectEl = selectedItem.querySelector('select');
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Label</span>
                    <input type="text" class="inspector-input" value="${labelEl ? labelEl.innerText : 'Select'}" onchange="updateProp('label', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Options (val:lab,...)</span>
                    <input type="text" class="inspector-input" value="${Array.from(selectEl.querySelectorAll('option')).map(o => o.value + ':' + o.innerText).join(',')}" onchange="updateProp('options', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Default Value</span>
                    <input type="text" class="inspector-input" value="${selectEl.dataset.default || ''}" onchange="updateProp('default', this.value)">
                </div>
            `;
        }

        // Global icon picker
        const iconComponents = ['Button', 'SelectOneIcon', 'Menu', 'MenuItem', 'Avatar', 'Alert', 'Notification', 'Link', 'Tab', 'TreeItem'];
        if (iconComponents.includes(type)) {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Icon</span>
                    <div style="display:grid; grid-template-columns: repeat(5, 1fr); gap:5px; margin-top:5px;">
                        ${['👤','👥','🏠','☙','🔔','📧','📁','📊','🚀','🌙','☀️','➕','✏️','🗑️','✅'].map(icon =>
                            `<div class="icon-preset" style="cursor:pointer; padding:5px; text-align:center; border:1px solid ${props.icon === icon ? 'var(--jettra-accent)' : 'rgba(255,255,255,0.1)'}" onclick="updateProp('icon', '${icon}')">${icon}</div>`
                        ).join('')}
                    </div>
                    <input type="text" class="inspector-input" style="margin-top:5px" placeholder="Custom icon/unicode" value="${props.icon || ""}" onchange="updateProp('icon', this.value)">
                </div>
            `;
        }

        if (type === 'Button') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Button Style</span>
                    <select class="inspector-input" onchange="updateProp('btnStyle', this.value)">
                        <option value="PRIMARY" ${props.btnStyle === 'PRIMARY' ? 'selected' : ''}>PRIMARY</option>
                        <option value="SECONDARY" ${props.btnStyle === 'SECONDARY' ? 'selected' : ''}>SECONDARY</option>
                        <option value="HELP" ${props.btnStyle === 'HELP' ? 'selected' : ''}>HELP</option>
                        <option value="DANGER" ${props.btnStyle === 'DANGER' ? 'selected' : ''}>DANGER</option>
                        <option value="INFO" ${props.btnStyle === 'INFO' ? 'selected' : ''}>INFO</option>
                    </select>
                </div>
            `;
        }

        if (type === 'TextBox' || type === 'Label') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Bind to Model Field</span>
                    <select class="inspector-input" onchange="updateProp('binding', this.value)">
                        <option value="">No Binding</option>
                        ${modelFields.map(f => `<option value="${f}" ${props.binding === f ? 'selected' : ''}>${f}</option>`).join('')}
                    </select>
                </div>
            `;
        }

        if (type === 'Panel' || type === 'Grid') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Columns</span>
                    <input type="number" class="inspector-input" value="${props.columns || 2}" min="1" max="12" onchange="updateProp('columns', this.value)">
                </div>
            `;
        }

        if (type === 'Map') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Initial Latitude (lat)</span>
                    <input type="number" step="0.0001" class="inspector-input" value="${props.lat || 40.7128}" placeholder="e.g. 40.7128" onchange="updateProp('lat', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Initial Longitude (lng)</span>
                    <input type="number" step="0.0001" class="inspector-input" value="${props.lng || -74.0060}" placeholder="e.g. -74.0060" onchange="updateProp('lng', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Zoom Level</span>
                    <input type="number" class="inspector-input" value="${props.zoom || 13}" min="1" max="25" onchange="updateProp('zoom', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Marker Title</span>
                    <input type="text" class="inspector-input" value="${props.marker || ''}" placeholder="Main Location" onchange="updateProp('marker', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Enable Search</span>
                    <select class="inspector-input" onchange="updateProp('enableSearch', this.value === 'true')">
                        <option value="false" ${props.enableSearch ? '' : 'selected'}>False</option>
                        <option value="true" ${props.enableSearch ? 'selected' : ''}>True</option>
                    </select>
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Enable Relief</span>
                    <select class="inspector-input" onchange="updateProp('enableRelief', this.value === 'true')">
                        <option value="false" ${props.enableRelief ? '' : 'selected'}>False</option>
                        <option value="true" ${props.enableRelief ? 'selected' : ''}>True</option>
                    </select>
                </div>
            `;
        }

        if (type === 'Spinner') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Value</span>
                    <input type="number" step="0.1" class="inspector-input" value="${props.value || 0}" onchange="updateProp('value', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Min</span>
                    <input type="number" step="0.1" class="inspector-input" value="${props.min || 0}" onchange="updateProp('min', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Max</span>
                    <input type="number" step="0.1" class="inspector-input" value="${props.max || 100}" onchange="updateProp('max', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Step</span>
                    <input type="number" step="0.1" class="inspector-input" value="${props.step || 1}" onchange="updateProp('step', this.value)">
                </div>
            `;
        }

        if (type === 'RadioGroupButton' || type === 'CheckBoxGroup') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Group Name</span>
                    <input type="text" class="inspector-input" value="${props.name || (type === "CheckBoxGroup" ? "checkboxGroup1" : "radioGroup1")}" onchange="updateProp('name', this.value)">
                </div>
            `;
        }

        if (type.startsWith('Chart')) {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Labels (comma sep.)</span>
                    <input type="text" class="inspector-input" value="${props.labels || "Label 1, Label 2"}" onchange="updateProp('labels', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Dataset Label</span>
                    <input type="text" class="inspector-input" value="${props.datasetLabel || "Data"}" onchange="updateProp('datasetLabel', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Data (comma sep. numbers)</span>
                    <input type="text" class="inspector-input" value="${props.data || "10, 20"}" onchange="updateProp('data', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Bg Color</span>
                    <input type="color" class="inspector-input" value="${props.bgColor || "#00ffff"}" onchange="updateProp('bgColor', this.value)">
                </div>
            `;
        }

        if (type === 'ScheduleControl') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Initial View</span>
                    <select class="inspector-input" onchange="updateProp('view', this.value)">
                        <option value="day" ${props.view === 'day' ? 'selected' : ''}>Day</option>
                        <option value="week" ${props.view === 'week' ? 'selected' : ''}>Week</option>
                        <option value="month" ${props.view === 'month' ? 'selected' : ''}>Month</option>
                    </select>
                </div>
            `;
        }

        if (type === 'Card') {
            html += `
                <div class="inspector-row"><span class="inspector-label">Title</span><input type="text" class="inspector-input" value="${props.title || ''}" onchange="updateProp('title', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Subtitle</span><input type="text" class="inspector-input" value="${props.subtitle || ''}" onchange="updateProp('subtitle', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Content</span><textarea class="inspector-input" onchange="updateProp('content', this.value)">${props.content || ''}</textarea></div>
                <div class="inspector-row"><span class="inspector-label">Image URL</span><input type="text" class="inspector-input" value="${props.imageUrl || ''}" onchange="updateProp('imageUrl', this.value)"></div>
            `;
        }

        if (type === 'QR') {
            html += `
                <div class="inspector-row"><span class="inspector-label">Text</span><input type="text" class="inspector-input" value="${props.text || 'https://jettra.io'}" onchange="updateProp('text', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Width</span><input type="number" class="inspector-input" value="${props.width || 128}" onchange="updateProp('width', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Height</span><input type="number" class="inspector-input" value="${props.height || 128}" onchange="updateProp('height', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Dark Color</span><input type="color" class="inspector-input" value="${props.colorDark || '#000000'}" onchange="updateProp('colorDark', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Light Color</span><input type="color" class="inspector-input" value="${props.colorLight || '#ffffff'}" onchange="updateProp('colorLight', this.value)"></div>
            `;
        }

        if (type === 'BarCode') {
            html += `
                <div class="inspector-row"><span class="inspector-label">Text</span><input type="text" class="inspector-input" value="${props.text || '123456789012'}" onchange="updateProp('text', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Format</span><input type="text" class="inspector-input" value="${props.format || 'CODE128'}" onchange="updateProp('format', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Line Color</span><input type="color" class="inspector-input" value="${props.lineColor || '#000000'}" onchange="updateProp('lineColor', this.value)"></div>
            `;
        }

        if (type === 'OTPValidator') {
            html += `
                <div class="inspector-row"><span class="inspector-label">Digits</span><input type="number" class="inspector-input" value="${props.amountOfDigits || 6}" onchange="updateProp('amountOfDigits', this.value)"></div>
            `;
        }

        if (type === 'Catcha') {
            html += `
                <div class="inspector-row"><span class="inspector-label">Images To Validate</span><input type="number" class="inspector-input" value="${props.amountOfImagesToValidate || 3}" onchange="updateProp('amountOfImagesToValidate', this.value)"></div>
            `;
        }

        if (type === 'CreditCard') {
            html += `
                <div class="inspector-row"><span class="inspector-label">Form Action</span><input type="text" class="inspector-input" value="${props.formAction || ''}" onchange="updateProp('formAction', this.value)"></div>
                <div class="inspector-row"><span class="inspector-label">Submit Text</span><input type="text" class="inspector-input" value="${props.submitText || 'Pay Now'}" onchange="updateProp('submitText', this.value)"></div>
            `;
        }

        if (type === 'Clock') {
            html += `
                <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                    <span class="inspector-label">Show Remaining Time</span>
                    <input type="checkbox" ${props.showTimeRemaining ? 'checked' : ''} onchange="updateProp('showTimeRemaining', this.checked)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">On Time Reached (JS)</span>
                    <input type="text" class="inspector-input" value="${props.onTimeReached || ""}" placeholder="alert('Time is up!');" onchange="updateProp('onTimeReached', this.value)">
                </div>
            `;
        }

        // Common properties
        html += `
            <div style="margin-top:15px; border-top:1px solid rgba(255,255,255,0.1); padding-top:10px">
                <div class="inspector-row">
                    <span class="inspector-label">Update (IDs)</span>
                    <input type="text" class="inspector-input" value="${props.update || ""}" onchange="updateProp('update', this.value)">
                </div>
            </div>
        `;

        if (type === 'Avatar') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Avatar Text</span>
                    <input type="text" class="inspector-input" value="${props.text || ""}" onchange="updateProp('text', this.value)">
                </div>
            `;
        }

        if (type === 'ProgressBar') {
            html += `
                <div class="inspector-row">
                    <span class="inspector-label">Value</span>
                    <input type="number" class="inspector-input" value="${props.value || 60}" onchange="updateProp('value', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Max</span>
                    <input type="number" class="inspector-input" value="${props.max || 100}" onchange="updateProp('max', this.value)">
                </div>
                <div class="inspector-row">
                    <span class="inspector-label">Color</span>
                    <input type="color" class="inspector-input" value="${props.color || '#00ffff'}" onchange="updateProp('color', this.value)">
                </div>
                <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                    <span class="inspector-label">Indeterminate</span>
                    <input type="checkbox" ${props.indeterminate ? 'checked' : ''} onchange="updateProp('indeterminate', this.checked)">
                </div>
                <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                    <span class="inspector-label">Show %</span>
                    <input type="checkbox" ${props.showPercent !== false ? 'checked' : ''} onchange="updateProp('showPercent', this.checked)">
                </div>
            `;
        }

        const supportedEvents = ['onClick', 'onChange', 'onAction'];
        html += `
            <div style="margin-top:20px; border-top:1px solid rgba(0,255,255,0.1); padding-top:15px">
                <span class="inspector-label" style="color:var(--jettra-accent); font-weight:bold">⚡ Event Handlers</span>
                <div style="display:flex; flex-direction:column; gap:8px; margin-top:10px">
                    ${supportedEvents.map(ev => `
                        <div class="inspector-row" style="cursor:pointer; display:flex; justify-content:space-between; align-items:center" onclick="window.openEventEditor('${ev}')">
                            <span style="font-size:11px; color:#ccc">${ev}</span>
                            <span style="font-size:10px; color:var(--jettra-accent)">Configure ⇢</span>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;

        inspector.innerHTML = html;
    };

    window.openEventEditor = function(evName) {
        if (!selectedItem) return;
        activeEventProperty = evName;
        const props = JSON.parse(selectedItem.getAttribute('data-props'));
        const currentCode = props.events[evName] || "e -> { \n    // Enter code here \n}";

        document.getElementById('event-code-input').value = currentCode.replace(/\\n/g, '\n');
        const modal = document.getElementById('event-editor-modal');
        modal.style.display = 'block';
        window.apply3DTracking(modal);
    };

    window.saveEventHandler = function() {
        if (!selectedItem || !activeEventProperty) return;
        const props = JSON.parse(selectedItem.getAttribute('data-props'));
        const newCode = document.getElementById('event-code-input').value;
        props.events[activeEventProperty] = newCode.replace(/\n/g, '\\n');
        selectedItem.setAttribute('data-props', JSON.stringify(props));

        document.getElementById('event-editor-modal').style.display = 'none';
        window.updateGeneratedCode();
        window.updateInspector();
    };

    window.parseModelFieldsContent = function(content) {
        const fieldRegex = /(?:private|protected|public)?\s+([\w<>]+)\s+(\w+);/g;
        let match;
        modelFields = [];
        while ((match = fieldRegex.exec(content)) !== null) {
            modelFields.push({ type: match[1], name: match[2] });
        }
        window.updateInspector();
        window.updateGeneratedCode();
    };

    window.selectModel = function(name) {
        viewModelName = name;
        const model = availableModels.find(m => m.name === name);
        if (model) {
            currentModel = model;
            window.parseModelFieldsContent(model.content);
            window.show3DMessage("Model Attached", "Se ha vinculado el modelo " + name + " a la vista.");
        } else {
            modelFields = [];
            window.updateInspector();
            window.updateGeneratedCode();
        }
    };

    window.updateProp = function(key, value) {
        if (!selectedItem) return;
        const props = JSON.parse(selectedItem.getAttribute('data-props'));
        props[key] = value;
        selectedItem.setAttribute('data-props', JSON.stringify(props));

        const type = selectedItem.getAttribute('data-type');
        if (key === 'text') {
            if (type === 'TextBox' || type === 'TextArea') {
                const inEl = selectedItem.querySelector('input, textarea');
                if (inEl) {
                    inEl.placeholder = value;
                    inEl.value = value;
                }
            } else if (type === 'Button') {
                const el = selectedItem.querySelector('button');
                if (el) el.innerHTML = (props.icon ? props.icon + ' ' : '') + value;
            } else {
                const el = selectedItem.querySelector('h3, h2, h1, p, label, .j-avatar, .j-panel-header, span');
                if (el) el.innerText = value;
                if (type === 'Avatar') {
                    props.icon = "";
                    selectedItem.setAttribute('data-props', JSON.stringify(props));
                    setTimeout(window.updateInspector, 10);
                }
            }
        }

        if (key === 'label') {
            const label = selectedItem.querySelector('label');
            if (label) label.innerText = value;
        }
        if (key === 'options') {
            const select = selectedItem.querySelector('select');
            if (select) {
                select.innerHTML = value.split(',').map(pair => {
                    const [v, l] = pair.split(':');
                    return `<option value="${v||''}">${l||v||''}</option>`;
                }).join('');
            }
        }
        if (key === 'default') {
            const select = selectedItem.querySelector('select');
            if (select) {
                select.dataset.default = value;
                Array.from(select.options).forEach(opt => {
                    if (opt.value === value) opt.selected = true;
                    else opt.selected = false;
                });
            }
        }

        if (key === 'icon') {
            if (type === 'Avatar') {
                const el = selectedItem.querySelector('.j-avatar');
                if (el) el.innerHTML = value;
                props.text = "";
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                setTimeout(window.updateInspector, 10);
            } else if (type === 'Button') {
                const el = selectedItem.querySelector('button');
                if (el) el.innerHTML = (value ? value + ' ' : '') + (props.text || 'Interactive Button');
            }
        }

        if (key === 'btnStyle' && type === 'Button') {
            const el = selectedItem.querySelector('button');
            if (el) {
                el.className = 'j-btn'; // reset
                el.classList.add('j-btn-' + value.toLowerCase());
            }
        }

        if (type === 'Card') {
            if (key === 'title') { const tEl = selectedItem.querySelector('.card-title-mock'); if (tEl) tEl.innerText = value; }
            if (key === 'subtitle') { const sEl = selectedItem.querySelector('.card-subtitle-mock'); if (sEl) sEl.innerText = value; }
            if (key === 'content') { const cEl = selectedItem.querySelector('.card-content-mock'); if (cEl) cEl.innerText = value; }
        }

        if (type === 'CreditCard') {
            if (key === 'submitText') {
                const btnEl = selectedItem.querySelector('button');
                if (btnEl) btnEl.innerText = value;
            }
        }

        if (type === 'ProgressBar') {
            const fill = selectedItem.querySelector('.j-progressbar-fill');
            if (key === 'value' || key === 'max') {
                const val = parseInt(selectedItem.getAttribute('data-props-value') || props.value || 60);
                const mx = parseInt(selectedItem.getAttribute('data-props-max') || props.max || 100);
                const pct = (key === 'value' ? value : val) / (key === 'max' ? value : mx) * 100;
                fill.style.width = pct + '%';
            }
            if (key === 'color') {
                fill.style.background = value;
                fill.style.boxShadow = `0 0 10px ${value}`;
            }
            if (key === 'indeterminate') {
                if (value) fill.classList.add('j-progress-indeterminate');
                else fill.classList.remove('j-progress-indeterminate');
            }
            if (key === 'showPercent') {
                const lbl = selectedItem.querySelector('.j-progressbar-label');
                if (lbl) lbl.style.display = value ? 'block' : 'none';
            }
        }
        if (key === 'columns' && (type === 'Panel' || type === 'Grid')) {
            const container = selectedItem.querySelector('.canvas-container');
            if (container) container.style.gridTemplateColumns = `repeat(${value}, 1fr)`;
        }
        if (type === 'Spinner' && key === 'value') {
            const disp = selectedItem.querySelector('.j-spinner-display');
            if (disp) disp.innerText = value;
        }
        if (type.startsWith('Chart') && key === 'bgColor') {
            selectedItem.style.borderColor = value;
            const span = selectedItem.querySelector('span');
            if (span) span.style.color = value;
        }
        window.updateGeneratedCode();
    };

    window.jettraFileCache = {};

    window.restoreFilesFromCache = function() {
        const cachedTree = localStorage.getItem('jettra_designer_tree');
        const cachedFilesStr = localStorage.getItem('jettra_designer_files');

        if (cachedTree && cachedFilesStr) {
            const viewer = document.getElementById('explorer-tree-view');
            if (viewer) viewer.innerHTML = cachedTree;
            try {
                window.jettraFileCache = JSON.parse(cachedFilesStr);
                availableModels = [];
                Object.keys(window.jettraFileCache).forEach(path => {
                    if (path.endsWith('Model.java')) {
                        const name = path.split('/').pop().replace('.java','');
                        availableModels.push({ name: name, content: window.jettraFileCache[path] });
                    }
                });
                window.updateModelSelect();
            } catch(e) {}
        }
    };

    window.clearProjectCache = function() {
        localStorage.removeItem('jettra_designer_tree');
        localStorage.removeItem('jettra_designer_files');
        window.jettraFileCache = {};
        availableModels = [];
        const viewer = document.getElementById('explorer-tree-view');
        if (viewer) viewer.innerHTML = '';
    window.updateGeneratedCode();
    };


    window.loadFiles = function(input) {
        const viewer = document.getElementById('explorer-tree-view');
        if (!viewer) return;

        viewer.innerHTML = '<div style="padding:15px; color:var(--jettra-accent);"><i class="jt-icon-loading"></i> Procesando Proyecto...</div>';

        const files = (input.tagName === 'INPUT' ? input : input.querySelector('input')).files;
        console.log("Loading files:", files ? files.length : 0);
        if (!files || files.length === 0) {
            viewer.innerHTML = '<div style="color:#666; padding:10px;">Selecci\u00F3n vac\u00EDa.</div>';
            return;
        }

        const tree = { _children: {} };
        const readPromises = [];
        window.jettraFileCache = {};
        availableModels = [];

        for (let i = 0; i < files.length; i++) {
            const f = files[i];
            const relPath = f.webkitRelativePath || f.name;
            if (relPath.includes('/target/') || relPath.includes('/.git/')) continue;

            // Use a more robust regex for path separator (matches \ and /)
            const parts = relPath.split(/[\\/]/);
            let curr = tree;
            for (let j = 0; j < parts.length; j++) {
                const p = parts[j];
                if (!curr._children[p]) {
                    curr._children[p] = { _children: {}, _path: relPath, _isFile: (j === parts.length - 1) };
                }
                curr = curr._children[p];
            }

            if (f.name.endsWith('.java') || f.name.endsWith('.md') || f.name.endsWith('.css') || f.name.endsWith('.html')) {
                readPromises.push(new Promise((resolve) => {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        const content = e.target.result;
                        window.jettraFileCache[relPath] = content;
                        if (f.name.endsWith('Model.java')) {
                            const mName = f.name.replace('.java','');
                            if (!availableModels.find(m => m.name === mName)) {
                                availableModels.push({ name: mName, content: content });
                            }
                        }
                        resolve();
                    };
                    reader.onerror = () => resolve();
                    reader.readAsText(f);
                }));
            }
        }

        function renderTree(node, depth = 0) {
            try {
                const keys = Object.keys(node._children).sort((a,b) => a.localeCompare(b));
                let html = "";
                keys.forEach(key => {
                    const child = node._children[key];
                    const icon = child._isFile ? (key.endsWith('.java') ? '\u2615' : '\uD83D\uDCC4') : '\uD83D\uDCC1';
                    const color = child._isFile ? '#88ccff' : 'var(--jettra-accent)';

                    html += `<div class="tree-node" style="padding-left:${depth * 12}px; cursor:pointer; color:${color}; font-size:11px; margin-bottom:4px; display:flex; align-items:center; gap:5px;"`;
                    if (child._isFile) {
                        html += ` onclick="window.loadFileContent('${child._path.replace(/\\/g, '/')}')"`;
                    }
                    html += `><span style="font-size:14px">${icon}</span> <span>${key}</span></div>`;

                    if (!child._isFile) {
                        html += renderTree(child, depth + 1);
                    }
                });
                return html;
            } catch(e) {
                console.error("Error rendering tree:", e);
                return "Error: " + e.message;
            }
        }

        Promise.all(readPromises).then(() => {
            console.log("Files cached. Available models:", availableModels.length);
            const finalHtml = renderTree(tree);
            viewer.innerHTML = finalHtml || '<div style="color:#666; padding:10px;">No se encontraron archivos visualizables.</div>';
            window.updateModelSelect();
            try {
                localStorage.setItem('jettra_designer_tree', finalHtml);
                localStorage.setItem('jettra_designer_files', JSON.stringify(window.jettraFileCache));
            } catch(e) {}
        }).catch(e => {
            console.error("Promise error:", e);
            viewer.innerHTML = '<div style="color:red; padding:10px;">Error al cargar el proyecto.</div>';
        });
    };

    window.openClass = function(name) {
        const isPage = name.endsWith('Page.java');
        const isModel = name.endsWith('Model.java');

        if (isModel) {
            window.selectModel(name.replace('.java', ''));
            return;
        }

        if (isPage) {
            const canvas = document.getElementById('canvas-drop-area');
            const fullPathKey = Object.keys(window.jettraFileCache).find(k => k.endsWith('/' + name) || k === name);
            const content = fullPathKey ? window.jettraFileCache[fullPathKey] : undefined;

            const doOpen = () => {
                if (content) window.loadFileContent(fullPathKey);
                else window.show3DMessage("Aviso", "No se encontró contenido para " + name);
            };

            if (canvas.querySelectorAll('.canvas-item').length > 0) {
                window.show3DConfirm("Abrir Clase", "¿Cerrar diseño actual y abrir " + name + "?", doOpen);
            } else {
                doOpen();
            }
        }
    };

    window.updateModelSelect = function() {
        const container = document.getElementById('model-select-container');
        if (!container) return;

        if (availableModels.length === 0) {
            container.innerHTML = '<span style="color:#666; font-size:11px">No models found in project</span>';
            return;
        }

        let html = '<select class="j-input" style="padding:4px; font-size:11px" onchange="window.selectModel(this.value)">';
        html += '<option value="">-- Select Model --</option>';
        availableModels.forEach(m => {
            const selected = viewModelName === m.name ? 'selected' : '';
            html += `<option value="${m.name}" ${selected}>${m.name}</option>`;
        });
        html += '</select>';
        container.innerHTML = html;
    };

    window.generateCRUD = function() {
        const canvas = document.getElementById('canvas-drop-area');
        if (!canvas) return;
        if (!currentModel) {
            window.show3DMessage("Error", "Por favor, seleccione un modelo para analizar.");
            return;
        }

        const mName = currentModel.name;
        const baseName = mName.replace("Model", "");
        const repoName = baseName + "Repository";

        // Detailed analysis of model fields
        let tblHeaders = [];
        let tblRows = [];
        let modalFieldsCode = "";
        let imports = new Set(["com.jettra.example.dashboard.DashboardBasePage", "com.jettra.example.model." + mName, "com.jettra.example.repository." + repoName, "io.jettra.wui.complex.*", "io.jettra.wui.components.*", "io.jettra.wui.core.annotations.*", "io.jettra.wui.sync.*", "java.util.*"]);

        modelFields.forEach(field => {
            const cName = field.name.charAt(0).toUpperCase() + field.name.slice(1);
            const vName = field.name;
            const isList = field.type.includes('List');
            const isCustom = /^[A-Z]/.test(field.type) && !['String','Integer','Double','Boolean','Long','Date'].includes(field.type);

            tblHeaders.push(`new TD("${cName}")`);
            tblRows.push(`new TD(String.valueOf(p.get${cName}()))`);

            modalFieldsCode += `        Div g_${vName} = new Div(); g_${vName}.addClass("form-group");
`;
            modalFieldsCode += `        g_${vName}.add(new Label("${vName}", "${cName}"));
`;

            if (isList) {
                modalFieldsCode += `        SelectMany sel_${vName} = new SelectMany("${vName}").setInline(true);
`;
                modalFieldsCode += `        sel_${vName}.setId("input_${vName}").addClass("j-input");
`;
                modalFieldsCode += `        g_${vName}.add(sel_${vName});
`;
            } else if (isCustom) {
                modalFieldsCode += `        SelectOne sel_${vName} = new SelectOne("${vName}", "");
`;
                modalFieldsCode += `        sel_${vName}.setId("input_${vName}").addClass("j-input");
`;
                modalFieldsCode += `        g_${vName}.add(sel_${vName});
`;
            } else if (field.type === 'Date') {
                modalFieldsCode += `        DatePicker dp_${vName} = new DatePicker("${vName}", "${cName}");
`;
                modalFieldsCode += `        dp_${vName}.setId("input_${vName}").addClass("j-input");
`;
                modalFieldsCode += `        g_${vName}.add(dp_${vName});
`;
            } else {
                modalFieldsCode += `        TextBox tb_${vName} = new TextBox("text", "${vName}");
`;
                modalFieldsCode += `        tb_${vName}.setId("input_${vName}").addClass("j-input");
`;
                modalFieldsCode += `        g_${vName}.add(tb_${vName});
`;
            }
            modalFieldsCode += `        form.add(g_${vName});

`;
        });

        let code = `package com.jettra.example.pages;

`;
        Array.from(imports).sort().forEach(imp => code += `import ${imp};
`);
        code += `
@JettraPageSincronized(SyncType.ALL)
`;
        code += `public class ${baseName}Page extends DashboardBasePage {

`;
        code += `    @InjectViewModel
    private ${mName} model;

`;
        code += `    private Div crudModal;
    private Header modalTitle;
    private TextBox modalAction;
    private Button modalSubmitBtn;

`;
        code += `    public ${baseName}Page() {
        super("${baseName} Maintenance");
    }

`;
        code += `    @Override
    protected void initCenter(Center center, String username) {
`;
        code += `        Div main = new Div(); main.setStyle("padding", "20px");

`;
        code += `        Header title = new Header(2, "Mantenimiento de ${baseName}");
`;
        code += `        title.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "20px");
`;
        code += `        main.add(title);

`;
        code += `        Button addBtn = new Button("\\u2795 A\u00F1adir ${baseName}");\n`;
        code += `        addBtn.addClass("j-btn-primary").addClickListener(() -> {\n`;
        code += `            this.model = new ${mName}();\n`;
        code += `            showModal("Nuevo ${baseName}", "save");\n`;
        code += `        });\n`;
        code += `        main.add(addBtn);\n\n`;
        code += `        Datatable table = new Datatable();\n`;
        code += `        table.addHeaderRow(new Row(\n            ${tblHeaders.join(', ')},\n            new TD("Acciones")\n        ));\n\n`;
        code += `        List<${mName}> all = ${repoName}.findAll();
`;
        code += `        for (${mName} p : all) {
`;
        code += `            TD actionsTd = new TD(); actionsTd.setStyle("display", "flex").setStyle("gap", "10px");
`;
        code += `            Button editBtn = new Button("\u270F\uFE0F"); editBtn.addClass("j-btn").addClickListener(() -> {
`;
        code += `                this.model = p; showModal("Editar ${baseName}", "save");
`;
        code += `            });
`;
        code += `            actionsTd.add(editBtn);\n`;
        code += `            table.addRow(new Row(\n                ${tblRows.join(', ')},\n                actionsTd\n            ));\n`;
        code += `        }
`;
        code += `        main.add(table);
        center.add(main);
        setupModal();
    }

`;
        code += `    private void showModal(String title, String action) {
`;
        code += `        this.crudModal.setStyle("display", "flex");
`;
        code += `        this.modalTitle.setContent(title);
`;
        code += `        this.modalAction.setProperty("value", action);
    }

`;

        code += `    @Override
    protected void onPost(Map<String, String> params) {
`;
        code += `        String action = params.get("action");
`;
        code += `        // logic to handle save/delete based on action
`;
        code += `        // boolean result = ${repoName}.handleAction(action, params);
`;
        code += `        // if (result) {
`;
        code += `        //    JettraSyncManager.notifyChange("${mName}", SyncType.UPDATE, getLoggedUser(currentExchange));
`;
        code += `        //    try { redirect(currentExchange, JettraServer.resolvePath("/${baseName.toLowerCase()}")); } catch (Exception e) {}
`;
        code += `        // }
`;
        code += `    }

`;

        code += `    private void setupModal() {
`;
        code += `        this.crudModal = new Div(); this.crudModal.setId("crudModal").addClass("j-modal-overlay");
`;
        code += `        this.crudModal.setStyle("display","none").setStyle("position","fixed").setStyle("z-index","9999").setStyle("background","rgba(0,0,0,0.8)");

`;
        code += `        Div content = new Div(); content.addClass("j-modal-content");\n`;
        code += `        this.modalTitle = new Header(3, "Operaci\u00F3n");\n`;
        code += `        Form form = new Form("${baseName.toLowerCase()}Form", "");
`;
        code += `        this.modalAction = new TextBox("hidden", "action");

`;
        code += modalFieldsCode;
        code += `        Button cancelBtn = new Button("CANCELAR"); cancelBtn.addClass("j-btn");
`;
        code += `        cancelBtn.setProperty("onclick", "document.getElementById('crudModal').style.display='none'; return false;");
`;
        code += `        this.modalSubmitBtn = new Button("GUARDAR"); this.modalSubmitBtn.addClass("j-btn-primary");

`;
        code += `        Div footer = new Div(); footer.setStyle("display","flex").setStyle("justify-content","flex-end").setStyle("gap","10px");\n`;
        code += `        footer.add(cancelBtn).add(this.modalSubmitBtn);\n`;
        code += `        form.add(this.modalAction).add(footer);\n`;
        code += `        content.add(this.modalTitle).add(form);\n`;
        code += `        this.crudModal.add(content);\n        this.add(this.crudModal);\n    }\n}\n`;

        document.getElementById('generated-code-display').value = code;
        document.getElementById('generated-code-hidden').value = code;

        // Switch tab to code
        const tab = document.querySelector('[data-tab="code"]');
        if (tab) tab.click();

        window.show3DMessage("CRUD Generado", "Se ha generado la arquitectura MVC completa para " + baseName);
    };

    window.clearDesigner = function() {
        window.show3DConfirm("Limpiar", "¿Borrar elementos?", () => {
            document.getElementById('canvas-drop-area').innerHTML = '<div class="canvas-placeholder">Start dragging...</div>';
            selectedItem = null;
            window.updateInspector();
        });
    };

    window.updateGeneratedCode = function() {
        if (window.isSyncing) return;
        const display = document.getElementById('generated-code-display');
        const hidden = document.getElementById('generated-code-hidden');
        if (!display || !hidden) return;

        const canvasItems = document.querySelectorAll('#canvas-drop-area > .canvas-item, .canvas-container > .canvas-item');
        if (canvasItems.length === 0) {
            display.innerText = "// No components added yet";
            return;
        }

        let code = `package com.jettra.example.pages;\n\nimport io.jettra.wui.complex.*;\nimport io.jettra.wui.components.*;\n\n`;
        code += `public class GeneratedPage extends DashboardBasePage {\n`;
        code += `    private ${viewModelName} model = ${viewModelName}.getInstance();\n\n`;
        code += `    protected void initCenter(Center center, String username) {\n`;

        function walk(items, container) {
            let out = "";
            items.forEach(it => {
                const type = it.getAttribute('data-type');
                const props = JSON.parse(it.getAttribute('data-props'));
                const v = props.id && props.id.trim() !== "" ? props.id : (type.toLowerCase() + "_" + Math.floor(Math.random()*10000));

                const handleCommon = (compVar) => {
                    let out = "";
                    if (props.update) out += `        ${compVar}.setUpdate("${props.update}");\n`;
                    return out;
                };

                const handleEvents = (compVar) => {
                    let evOut = "";
                    Object.keys(props.events).forEach(ev => {
                        const eventCode = props.events[ev].replace(/\\n/g, '\n');
                        evOut += `        ${compVar}.${ev}(${eventCode});\n`;
                    });
                    return evOut;
                };

                switch(type) {
                    case 'Divide': out += `        ${container}.add(new Divide());\n`; break;
                    case 'Separator': out += `        ${container}.add(new Separator());\n`; break;
                    case 'Header': out += `        ${container}.add(new Header(1, "${props.text}"));\n`; break;
                    case 'Paragraph': out += `        ${container}.add(new Paragraph("${props.text}"));\n`; break;
                    case 'Span': out += `        ${container}.add(new Span("${props.text}"));\n`; break;
                    case 'Label': out += `        ${container}.add(new Label("${props.text}"));\n`; break;
                    case 'Button':
                        out += `        ${type} ${v} = new ${type}("${props.text}");\n`;
                        if (props.btnStyle) out += `        ${v}.setStyle(${type}.Style.${props.btnStyle});\n`;
                        if (props.icon) out += `        ${v}.setIcon("${props.icon}");\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'TextBox':
                    case 'TextArea':
                        out += `        ${type} ${v} = new ${type}("${props.text || type}", "${props.text || type}");\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'Clock':
                        out += `        Clock ${v} = new Clock("${props.text || 'Clock'}");\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'Avatar':
                        out += `        Avatar ${v} = new Avatar();\n`;
                        if (props.text) out += `        ${v}.setText("${props.text}");\n`;
                        if (props.icon) out += `        ${v}.setIcon("${props.icon}");\n`;
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'ProgressBar':
                        out += `        ProgressBar ${v} = new ProgressBar(${props.value || 0}, ${props.max || 100});\n`;
                        if (props.color) out += `        ${v}.setColor("${props.color}");\n`;
                        if (props.indeterminate) out += `        ${v}.setIndeterminate(true);\n`;
                        if (props.showPercent === false) out += `        ${v}.setShowPercent(false);\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'Spinner':
                        out += `        Spinner ${v} = new Spinner("${v}", ${props.value || 0});\n`;
                        if (props.min !== undefined && props.min !== "") out += `        ${v}.setMin(${props.min});\n`;
                        if (props.max !== undefined && props.max !== "") out += `        ${v}.setMax(${props.max});\n`;
                        if (props.step !== undefined && props.step !== "") out += `        ${v}.setStep(${props.step});\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'ScheduleControl': {
                        out += `        ScheduleControl ${v} = new ScheduleControl("${v}");\n`;
                        if (props.binding) out += `        ${v}.setValue(model.get${props.binding.charAt(0).toUpperCase() + props.binding.slice(1)}());\n`;
                        if (props.showTimeRemaining) out += `        ${v}.setShowTimeRemaining(true);\n`;
                        if (props.onTimeReached) out += `        ${v}.setOnTimeReached("${props.onTimeReached}");\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    }
                    case 'ChartsBar':
                    case 'ChartsDoughnut':
                    case 'ChartsLine':
                    case 'ChartsPie':
                    case 'ChartsRadar': {
                        out += `        ${type} ${v} = new ${type}("${v}");\n`;
                        const lArg = props.labels ? `"${props.labels.split(',').map(s => s.trim()).join('", "')}"` : '"Label 1", "Label 2"';
                        const dLabel = props.datasetLabel ? props.datasetLabel : "Data";
                        const dData = props.data ? props.data : "10, 20";
                        const dBg = props.bgColor ? props.bgColor : "#00ffff";
                        out += `        ${v}.setLabels(${lArg});\n`;
                        out += `        ${v}.addDataset("${dLabel}", new Number[]{${dData}}, new String[]{"${dBg}"}, new String[]{"${dBg}"});\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    }
                    case 'Loading':
                        out += `        Loading ${v} = new Loading();\n`;
                        if (props.size) out += `        ${v}.setSize(Loading.Size.${props.size});\n`;
                        if (props.color) out += `        ${v}.setColor("${props.color}");\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'Modal': {
                        const mId = props.idGen || "modal_" + Math.floor(Math.random()*1000);
                        out += `        Modal ${v} = new Modal("${mId}");\n`;
                        const kidsM = it.querySelectorAll(':scope > .canvas-container > .canvas-item');
                        if (kidsM.length > 0) out += walk(kidsM, v);
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    }
                    case 'Card': {
                        out += `        Card ${v} = new Card();\n`;
                        if (props.title) out += `        ${v}.setTitle("${props.title}");\n`;
                        if (props.subtitle) out += `        ${v}.setSubtitle("${props.subtitle}");\n`;
                        if (props.content) out += `        ${v}.setContentText("${props.content}");\n`;
                        if (props.imageUrl) out += `        ${v}.setImageUrl("${props.imageUrl}");\n`;
                        const kidsCard = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                        if (kidsCard.length > 0) out += walk(kidsCard, v);
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    }
                    case 'MenuItem':
                    case 'TreeItem':
                    case 'Tab':
                        out += `        ${type} ${v} = new ${type}("${props.text || type}");\n`;
                        const kidsNested = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item, :scope > span > .canvas-container > .canvas-item');
                        if (kidsNested.length > 0) out += walk(kidsNested, v);
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'SelectOne':
                    case 'SelectMany':
                    case 'SelectOneIcon':
                        out += `        ${type} ${v} = new ${type}("${v}");\n`;
                        if (props.default) out += `        ${v}.setDefault("${props.default}");\n`;
                        if (props.options) {
                            const opts = props.options.split(',');
                            opts.forEach(o => {
                                const parts = o.split(':');
                                const optVal = parts[0] ? parts[0].trim() : 'val';
                                const optLabel = parts[1] ? parts[1].trim() : optVal;
                                if (type === 'SelectOneIcon') {
                                   out += `        ${v}.addOption("${optVal}", "${optLabel}", "${props.icon || '⭐'}");\n`;
                                } else {
                                   out += `        ${v}.addOption("${optVal}", "${optLabel}");\n`;
                                }
                            });
                        }
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'Panel':
                    case 'Grid':
                    case 'Board':
                    case 'MenuBar':
                    case 'RadioGroupButton':
                    case 'CheckBoxGroup':
                    case 'Tree':
                    case 'TabView':
                    case 'Div':
                    case 'LayoutDisplay':
                        if (type === 'RadioGroupButton' || type === 'CheckBoxGroup') {
                            out += `        ${type} ${v} = new ${type}("${props.name || v}");\n`;
                        } else {
                            out += `        ${type} ${v} = new ${type}("${v}");\n`;
                        }
                        if (props.columns && (type === 'Grid' || type === 'Panel')) out += `        ${v}.setColumns(${props.columns});\n`;
                        const kids = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                        if (kids.length > 0) out += walk(kids, v);
                        if (props.icon) out += `        // Try using icon property if supported for ${type}\n        try { ${v}.getClass().getMethod("setIcon", String.class).invoke(${v}, "${props.icon}"); } catch(Exception ignored) {}\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'DatePicker':
                    case 'Time':
                        out += `        ${type} ${v} = new ${type}("${v}", "${props.text || type}");\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'Calendar':
                    case 'Schedule':
                    case 'Timeline':
                    case 'Organigram':
                        out += `        ${type} ${v} = new ${type}();\n`;
                        out += handleCommon(v);
                        out += handleEvents(v);
                        out += `        ${container}.add(${v});\n`;
                        break;
                    case 'Map':
                         out += `        Map ${v} = new Map("${v}");\n`;
                         out += `        ${v}.setCenter(${props.lat || 0.0}, ${props.lng || 0.0}, ${props.zoom || 13});\n`;
                         if (props.markerTitle) out += `        ${v}.setMarker("${props.markerTitle}");\n`;
                         if (props.enableSearch) out += `        ${v}.setEnableSearch(true);\n`;
                         if (props.enableRelief) out += `        ${v}.setEnableRelief(true);\n`;
                         out += handleCommon(v);
                         out += handleEvents(v);
                         out += `        ${container}.add(${v});\n`;
                         break;
                     case 'QR':
                         out += `        QR ${v} = new QR("${v}");\n`;
                         if (props.text) out += `        ${v}.setText("${props.text}");\n`;
                         if (props.width) out += `        ${v}.setWidth(${props.width});\n`;
                         if (props.height) out += `        ${v}.setHeight(${props.height});\n`;
                         if (props.colorDark) out += `        ${v}.setColorDark("${props.colorDark}");\n`;
                         if (props.colorLight) out += `        ${v}.setColorLight("${props.colorLight}");\n`;
                         out += handleCommon(v);
                         out += handleEvents(v);
                         out += `        ${container}.add(${v});\n`;
                         break;
                     case 'BarCode':
                         out += `        BarCode ${v} = new BarCode("${v}");\n`;
                         if (props.text) out += `        ${v}.setText("${props.text}");\n`;
                         if (props.format) out += `        ${v}.setFormat("${props.format}");\n`;
                         if (props.lineColor) out += `        ${v}.setLineColor("${props.lineColor}");\n`;
                         out += handleCommon(v);
                         out += handleEvents(v);
                         out += `        ${container}.add(${v});\n`;
                         break;
                    case 'OTPValidator': {
                         out += `        OTPValidator ${v} = new OTPValidator("${v}");\n`;
                         if (props.amountOfDigits) out += `        ${v}.setAmountOfDigits(${props.amountOfDigits});\n`;
                         out += handleCommon(v);
                         out += handleEvents(v);
                         out += `        ${container}.add(${v});\n`;
                         break;
                     }
                     case 'Catcha': {
                         out += `        Catcha ${v} = new Catcha("${v}");\n`;
                         if (props.amountOfImagesToValidate) out += `        ${v}.setAmountOfImagesToValidate(${props.amountOfImagesToValidate});\n`;
                         out += handleCommon(v);
                         out += handleEvents(v);
                         out += `        ${container}.add(${v});\n`;
                         break;
                     }
                     case 'CreditCard': {
                         out += `        CreditCard ${v} = new CreditCard("${v}");\n`;
                         if (props.formAction) out += `        ${v}.setFormAction("${props.formAction}");\n`;
                         if (props.submitText) out += `        ${v}.setSubmitText("${props.submitText}");\n`;
                         const kidsCC = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                         if (kidsCC.length > 0) out += walk(kidsCC, v);
                         out += handleCommon(v);
                         out += handleEvents(v);
                         out += `        ${container}.add(${v});\n`;
                         break;
                     }
                     default: {
                         if (props.text && props.text !== type && type !== 'Spinner') {
                             out += `        ${type} ${v} = new ${type}("${props.text}");\n`;
                         } else {
                             out += `        ${type} ${v} = new ${type}("${v}");\n`;
                         }
                         out += handleCommon(v);
                         out += handleEvents(v);
                         out += `        ${container}.add(${v});\n`;
                     }
                }
            });
            return out;
        }
        code += walk(document.querySelectorAll('#canvas-drop-area > .canvas-item'), "center");
        code += walk(document.querySelectorAll('#modal-list-container > .canvas-item'), "center");
        code += `    }\n}`;

        display.value = code;
        hidden.value = code;
    };

    window.syncCodeToCanvas = function() {
        window.isSyncing = true;
        const display = document.getElementById('generated-code-display');
        if (!display) {
            window.isSyncing = false;
            return;
        }
        const code = display.value;
        const canvas = document.getElementById('canvas-drop-area');

        canvas.innerHTML = '';
        const mtArea = document.getElementById('modal-list-container');
        if (mtArea) mtArea.innerHTML = '';

        let foundAny = false;
        let varMap = {};
        const lines = code.split(/\r?\n/);

        let currentModalArea = mtArea;

        lines.forEach(line => {
            let cleanLine = line.trim();
            if (cleanLine.startsWith("//") || cleanLine.length === 0) return;

            let currentVar = null;

            // Match object instantiation (e.g., io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("clock-code-modal");)
            // Group 1: Type name, Group 2: Variable name, Group 3: Constructor args
            let mInst = /(?:[a-zA-Z0-9_.]+\.)?([A-Z][a-zA-Z0-9_]*)\s+([a-zA-Z0-9_]+)\s*=\s*new\s+(?:[a-zA-Z0-9_.]+\.)?[A-Z][a-zA-Z0-9_]*\s*\((.*?)\)/.exec(cleanLine);

            if (mInst) {
                foundAny = true;
                let type = mInst[1];
                let vname = mInst[2];
                let args = mInst[3];

                currentVar = vname;

                // We skip non-UI things if necessary, but window.addComponentToCanvas handles known types
                let tempParent = document.createElement('div');
                window.addComponentToCanvas(type, tempParent);
                let el = tempParent.lastElementChild;
                if (el) {
                    el.setAttribute('data-var', vname);
                    varMap[vname] = el;

                    let props = JSON.parse(el.getAttribute('data-props') || "{}");
                    if (args && args.trim().length > 0) {
                        let cleanParams = args.replace(/"/g, '').split(',');
                        if (cleanParams.length > 0) {
                            if (['Header','Paragraph','Button','Clock','Span','Label','Alert','Notification','Link','Downloader','CheckBox','RadioButton','ToggleSwitch','MenuItem','TreeItem','Tab','Card'].includes(type)) {
                                props.text = cleanParams.length > 1 ? cleanParams[1].trim() : cleanParams[0].trim();
                            }
                            if (type === 'Modal') {
                                let mName = cleanParams.length > 1 ? cleanParams[1].trim() : (cleanParams.length > 0 ? cleanParams[0].trim() : 'Modal Component');
                                props.text = mName;
                                props.idGen = cleanParams[0].trim();
                            }
                            if (type === 'Board') props.text = cleanParams.length > 1 ? cleanParams[1].trim() : (cleanParams.length > 0 ? cleanParams[0].trim() : 'New Board');
                            if (['Panel','Grid','Div','LayoutDisplay','TabView','Tree','MenuBar'].includes(type)){
                                props.columns = parseInt(cleanParams[0].trim()) || 2;
                            }
                            if (type === 'ProgressBar') { props.value = parseInt(cleanParams[0])||0; props.max = parseInt(cleanParams[1])||100; }
                        }
                    }
                    el.setAttribute('data-props', JSON.stringify(props));
                    const inner = el.querySelector('h3, h2, h1, p, button, label, .j-avatar, .j-panel-header, span');
                    if (props.text && inner) inner.innerText = props.text;
                }
            } else {
                // Extract starting variable from chains (e.g., container.add(...))
                let firstVarMatch = /^([a-zA-Z0-9_]+)\./.exec(cleanLine);
                if (firstVarMatch) currentVar = firstVarMatch[1];
            }

            if (!currentVar && !cleanLine.includes(".add")) return;

            let parentEl = varMap[currentVar];
            if (currentVar === 'center' || currentVar === 'container') parentEl = canvas;
            if (!parentEl && currentVar === 'center') parentEl = canvas;
            if (!parentEl && currentVar === 'codeModal') parentEl = canvas;

            // Match all .add() calls
            let addMatches = [...cleanLine.matchAll(/\.add\s*\((.*?)\)/g)];
            addMatches.forEach(mAdd => {
                let childArg = mAdd[1].trim();
                if (parentEl) {
                   if (childArg.startsWith("new ")) {
                       let inlineTypeMatch = /new\s+(?:[a-zA-Z0-9_.]+\.)?([A-Z][a-zA-Z0-9_]*)\s*\((.*?)\)/.exec(childArg);
                       if (inlineTypeMatch) {
                           let type = inlineTypeMatch[1];
                           let args = inlineTypeMatch[2];
                           let targetContainer = parentEl.querySelector('.canvas-container') || parentEl;

                           window.addComponentToCanvas(type, targetContainer);
                           let el = targetContainer.lastElementChild;
                           if(el) {
                               let props = JSON.parse(el.getAttribute('data-props') || "{}");
                               if (args && args.trim().length > 0) {
                                  let cleanParams = args.replace(/"/g, '').split(',');
                                  if (['Header','Paragraph','Button','Clock','Span','Label','Card'].includes(type)) {
                                     props.text = cleanParams.length > 1 ? cleanParams[1].trim() : cleanParams[0].trim();
                                  }
                               }
                               el.setAttribute('data-props', JSON.stringify(props));
                               const inner = el.querySelector('h3, h2, h1, p, button, label, span');
                               if (props.text && inner) inner.innerText = props.text;
                           }
                       }
                   } else if(!childArg.includes(".")) {
                       let childEl = varMap[childArg];
                       if (childEl) {
                           let targetContainer = parentEl;
                           if (parentEl !== canvas && parentEl.classList.contains('canvas-item')) {
                               let innerCont = parentEl.querySelector('.canvas-container');
                               if (!innerCont) {
                                   let possibleContainers = parentEl.querySelectorAll('div');
                                   if (possibleContainers.length > 0) targetContainer = possibleContainers[possibleContainers.length-1];
                               } else {
                                   targetContainer = innerCont;
                               }
                           }
                           targetContainer.appendChild(childEl);
                       }
                   }
                }
            });

            // Match all .setXxx() calls
            let setMatches = [...cleanLine.matchAll(/\.set([A-Z][a-zA-Z0-9_]*)\s*\((.*?)\)/g)];
            setMatches.forEach(mSet => {
               let method = mSet[1];
               let args = mSet[2].replace(/"/g, '').trim();
               let el = varMap[currentVar];
               if (el) {
                   let props = JSON.parse(el.getAttribute('data-props') || "{}");
                   if (method === 'Text') {
                       props.text = args;
                       const inner = el.querySelector('h3, h2, h1, p, button, label, span');
                       if (inner) inner.innerText = props.text;
                   }
                   el.setAttribute('data-props', JSON.stringify(props));
               }
            });
        });

        Object.values(varMap).forEach(el => {
            if (!canvas.contains(el) && el.parentElement && !el.parentElement.classList.contains('canvas-container')) {
                if (el.getAttribute('data-type') === 'Modal' && mtArea) mtArea.appendChild(el);
                else canvas.appendChild(el);
            }
        });

        if (Object.keys(varMap).length === 0 && !foundAny) {
            canvas.innerHTML = '<div class="canvas-placeholder">Start dragging components here to design</div>';
        }

        document.getElementById('generated-code-hidden').value = code;
        display.value = code;
        window.show3DMessage("Sync Complete", "Canvas has been rebuilt exactly from the structured Java source code.");
        setTimeout(() => { window.isSyncing = false; }, 100);
    };

    window.previewInterface = function() {
        const canvas = document.getElementById('canvas-drop-area');
        const pcontent = document.getElementById('preview-content-area');
        if (!canvas || !pcontent) return;

        pcontent.innerHTML = canvas.innerHTML;

        const draggables = pcontent.querySelectorAll('[draggable]');
        draggables.forEach(d => {
            d.removeAttribute('draggable');
            d.oncontextmenu = null;
            d.onclick = null;
            d.classList.remove('selected');
        });

        const placeholders = pcontent.querySelectorAll('.canvas-placeholder');
        placeholders.forEach(p => p.remove());

        const deleteTools = pcontent.querySelectorAll('.delete-tool');
        deleteTools.forEach(d => d.remove());

        const items = pcontent.querySelectorAll('.canvas-item, .canvas-container, .board-container-mock, .modal-container-mock');
        items.forEach(c => {
            c.classList.remove('canvas-item');
            c.classList.remove('canvas-container');
            c.classList.remove('selected');
            if (c.style.border && c.style.border.includes('dashed')) {
                c.style.border = 'none';
            }
        });

        const header = pcontent.querySelector('#canvas-header');
        if (header) header.style.display = 'none';

        document.getElementById('preview-modal').style.display = 'block';
    };


</div>
</div>
<div id="global-3d-message-modal" class="j-component" style="display:none; position:fixed; z-index:10000; left:0; top:0; width:100%; height:100%; background-color:rgba(0,0,0,0.5); backdrop-filter:blur(10px); "><style>  @keyframes dashModalAppear {    from { opacity:0; transform:translate(-50%,-45%); }    to { opacity:1; transform:translate(-50%,-50%); }  }  .dash-modal-3d {    animation: dashModalAppear 0.3s ease-out;    background: #1e293b;    border: 1px solid var(--jettra-accent);    border-radius: 12px;    box-shadow: 0 25px 50px -12px rgba(0,0,0,0.5);  }  .dash-btn-3d {    background: var(--jettra-accent);    color: #000; border: none; padding: 8px 20px; border-radius: 6px; font-weight: bold; cursor: pointer;    transition: background 0.2s;  }  .dash-btn-3d:hover { background: #0891b2; }  .dash-btn-3d:active { transform: translateY(1px); }</style><div class='dash-modal-3d' style='position:absolute; top:50%; left:50%; transform:translate(-50%, -50%); padding:25px; min-width:320px; color:#fff; text-align:center;'><h3 id='global-3d-title' style='margin-top:0; color:var(--jettra-accent); font-weight:700;'>Message</h3><p id='global-3d-body' style='margin-bottom:25px; font-size:15px; color:#cbd5e1;'></p><div><button class='dash-btn-3d' onclick='document.getElementById("global-3d-message-modal").style.display="none"'>Aceptar</button></div></div> window.show3DMessage = function(title, body) {   document.getElementById('global-3d-title').innerText = title;   document.getElementById('global-3d-body').innerText = body;   document.getElementById('global-3d-message-modal').style.display = 'block'; };
</div>
<div id='j-sync-popup-container'></div>
</body>
</html>
