import os
import re

files = [
    "src/main/java/com/jettra/example/pages/admin/UsuarioPage.java",
    "src/main/java/com/jettra/example/pages/admin/RolPage.java",
    "src/main/java/com/jettra/example/pages/admin/PermisoPage.java",
    "src/main/java/com/jettra/example/pages/admin/PerfilPage.java",
    "src/main/java/com/jettra/example/pages/cruview/PlanetaPage.java",
    "src/main/java/com/jettra/example/pages/cruview/ReglasViewCrudPage.java",
    "src/main/java/com/jettra/example/pages/library/PublisherPage.java",
    "src/main/java/com/jettra/example/pages/library/AuthorPage.java",
    "src/main/java/com/jettra/example/pages/library/BookPage.java",
    "src/main/java/com/jettra/example/pages/library/ReaderPage.java",
    "src/main/java/com/jettra/example/pages/datatable/DatatableEditableCrudViewPage.java",
    "src/main/java/com/jettra/example/pages/datatable/masterdetails/ViewDataTablePage.java"
]

for f in files:
    with open(f, 'r') as fp:
        content = fp.read()
    
    class_name = os.path.basename(f).replace('.java', '')
    
    # 1. Extract title
    title_match = re.search(r'super\("(.*?)"\);', content)
    title = title_match.group(1) if title_match else ""
    
    # 2. Extract the body inside `protected void initCenter(Center center, String username) { ... }` safely
    init_center_match = re.search(r'@Override\s+protected void initCenter\(Center center, String username\) \{(.*?)\n\}', content, re.DOTALL)
    init_center_content = ""
    if init_center_match:
        init_center_content = init_center_match.group(1).strip()
    
    # 3. Find where the class starts
    class_def_match = re.search(r'public class ' + class_name + r' extends (.*?) \{', content)
    if not class_def_match:
        continue
    
    extends_class = class_def_match.group(1).strip()
    
    # 4. Modify @CrudView(...) to include extendsClass and title
    # It might be across multiple lines
    crud_match = re.search(r'(@CrudView\([\s\S]*?\))(?=\s*public class)', content)
    if crud_match:
        crud = crud_match.group(1)
        # Find the closing parenthesis
        last_paren_idx = crud.rfind(')')
        if last_paren_idx != -1:
            crud_new = crud[:last_paren_idx] + f', extendsClass = com.jettra.example.dashboard.{extends_class}.class, title = "{title}")'
            content = content.replace(crud, crud_new)
            
    # 5. Replace public class X extends Y { ... } with public interface XDef { ... }
    # Everything before public class X extends Y { is kept.
    pre_class = content[:class_def_match.start()]
    
    # We will just write a new body
    new_body = f"public interface {class_name}Def {{\n"
    
    # Only keep afterInitCenter if it had meaningful content
    # For example, AuthorPage has a console
    if init_center_content and "io.jettra.wui.complex.CrudView" not in init_center_content and "new CrudView" not in init_center_content and "processCrudView" not in init_center_content:
        # If there's something else, let's just keep it
        pass
    
    # Specifically for ViewDataTablePage: it had huge code. 
    # But wait! ViewDataTablePage manually instantiates CrudView and has `autoRender = false`. 
    # If `autoRender = false`, our processor generates a class that has `autoRender = false` and DOES NOT call build or add it!
    # Wait, the processor generates the whole page! 
    # If the user sets `autoRender = false`, they expect to build it manually!
    if "autoRender = false" in content:
        # For this one, the user wants full control over the initCenter.
        new_body += f"""
    default void afterInitCenter(io.jettra.wui.complex.Center center, String username) {{
{init_center_content}
    }}
"""
    elif "console" in init_center_content:
        # AuthorPage custom code
        new_body += f"""
    default void afterInitCenter(io.jettra.wui.complex.Center center, String username) {{
        io.jettra.wui.components.Console console = new io.jettra.wui.components.Console("miConsola");
        center.add(console);
    }}
"""

    new_body += "}\n"
    
    new_f = f.replace('.java', 'Def.java')
    with open(new_f, 'w') as fp:
        fp.write(pre_class + new_body)
    
    os.remove(f)

