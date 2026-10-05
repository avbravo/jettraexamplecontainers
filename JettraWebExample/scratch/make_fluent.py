import os
import re

directories = [
    "/home/avbravo/NetBeansProjects/jettrastack_local/JettraWorkspace/JettraWUI/src/main/java/io/jettra/wui/components",
    "/home/avbravo/NetBeansProjects/jettrastack_local/JettraWorkspace/JettraWUI/src/main/java/io/jettra/wui/complex"
]

methods_to_add = [
    ("setId", "(String id)", "id"),
    ("setProperty", "(String key, String value)", "key, value"),
    ("setStyle", "(String key, String value)", "key, value"),
    ("addClass", "(String className)", "className"),
    ("removeClass", "(String className)", "className"),
    ("setContent", "(String content)", "content"),
    ("setUpdate", "(String ids)", "ids"),
    ("add", "(io.jettra.wui.core.UIComponent child)", "child"),
    ("addClickListener", "(io.jettra.wui.events.ClickListener listener)", "listener")
]

def make_fluent(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()
    
    content = "".join(lines)
    class_match = re.search(r'public (?:abstract )?class (\w+)', content)
    if not class_match:
        return
    
    class_name = class_match.group(1)
    if class_name == "UIComponent" or class_name == "JettraValidations":
        return

    # Find where to insert (before last closing brace)
    last_brace_index = -1
    for i in range(len(lines) - 1, -1, -1):
        if '}' in lines[i]:
            last_brace_index = i
            break
    
    if last_brace_index == -1:
        return

    new_methods = []
    for mname, params, args in methods_to_add:
        # Check if already present with exact return type (ignoring spaces)
        search_pattern = rf'public\s+{class_name}\s+{mname}'
        if re.search(search_pattern, content):
            continue
        
        # Check if present with UIComponent return type
        found_ui_comp = False
        for i in range(len(lines)):
            if f"public UIComponent {mname}" in lines[i]:
                lines[i] = lines[i].replace("public UIComponent", f"public {class_name}")
                found_ui_comp = True
                break
            # Also check for add(UIComponent child) without io.jettra...
            if mname == "add" and "public UIComponent add(UIComponent" in lines[i]:
                lines[i] = lines[i].replace("public UIComponent", f"public {class_name}")
                found_ui_comp = True
                break
        
        if found_ui_comp:
            continue

        method_template = f"""
    @Override
    public {class_name} {mname}{params} {{
        super.{mname}({args});
        return this;
    }}
"""
        new_methods.append(method_template)

    # Insert new methods before the last closing brace
    for method in reversed(new_methods):
        lines.insert(last_brace_index, method)

    with open(filepath, 'w') as f:
        f.writelines(lines)

for d in directories:
    for filename in os.listdir(d):
        if filename.endswith(".java"):
            make_fluent(os.path.join(d, filename))
