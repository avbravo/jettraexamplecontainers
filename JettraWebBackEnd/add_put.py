import os
import glob

files = [
    "src/main/java/com/jettra/plugin/company/controller/CompanyController.java",
    "src/main/java/com/jettra/plugin/company/controller/HeadquartersController.java",
    "src/main/java/com/jettra/plugin/company/controller/DepartmentController.java",
    "src/main/java/com/jettra/plugin/acreditation/controller/UserController.java",
    "src/main/java/com/jettra/plugin/acreditation/controller/RoleController.java",
    "src/main/java/com/jettra/plugin/acreditation/controller/PermissionController.java",
    "src/main/java/com/jettra/plugin/acreditation/controller/FeatureController.java"
]

for filepath in files:
    if not os.path.exists(filepath):
        continue
    with open(filepath, "r") as f:
        content = f.read()
    
    # Skip if PUT is already imported
    if "import io.jettra.rest.annotations.PUT;" in content:
        continue

    # Add PUT import next to POST import
    content = content.replace("import io.jettra.rest.annotations.POST;", "import io.jettra.rest.annotations.POST;\nimport io.jettra.rest.annotations.PUT;")

    # Find the entity class name from the POST method
    import re
    # public Response save(Company company) {
    match = re.search(r'public Response save\(([A-Za-z]+)\s+([a-zA-Z]+)\)', content)
    if not match:
        continue
    entity_class = match.group(1)
    entity_var = match.group(2)
    
    repository_var = entity_var + "Repository"
    
    # We'll just replace the POST method block by adding PUT after it.
    post_block = f"""    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save({entity_class} {entity_var}) {{
       {repository_var}.save({entity_var});
        return Response.ok("Saved successfully").build();
    }}"""
    
    if post_block not in content:
        # try another regex or simpler replacement
        pass
    
    put_block = f"""
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response update({entity_class} {entity_var}) {{
       {repository_var}.save({entity_var});
        return Response.ok("Updated successfully").build();
    }}"""

    content = content.replace(post_block, post_block + "\n" + put_block)
    
    with open(filepath, "w") as f:
        f.write(content)

print("Done")
