import os
import re

directories = [
    "/home/avbravo/NetBeansProjects/jettrastack_local/JettraWorkspace/JettraWUI/src/main/java/io/jettra/wui/components",
    "/home/avbravo/NetBeansProjects/jettrastack_local/JettraWorkspace/JettraWUI/src/main/java/io/jettra/wui/complex"
]

def add_fluent_add(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()
    
    content = "".join(lines)
    class_match = re.search(r'public (?:abstract )?class (\w+)', content)
    if not class_match: return
    class_name = class_match.group(1)
    if class_name in ["UIComponent", "JettraValidations"]: return

    if f"public {class_name} add(" in content: return

    # Find last }
    last_brace_index = -1
    for i in range(len(lines) - 1, -1, -1):
        if '}' in lines[i]:
            last_brace_index = i
            break
    
    if last_brace_index != -1:
        method = f"""
    @Override
    public {class_name} add(io.jettra.wui.core.UIComponent child) {{
        super.add(child);
        return this;
    }}
"""
        lines.insert(last_brace_index, method)
        with open(filepath, 'w') as f:
            f.writelines(lines)

for d in directories:
    for filename in os.listdir(d):
        if filename.endswith(".java"):
            add_fluent_add(os.path.join(d, filename))
