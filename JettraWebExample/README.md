# 🌐 JettraWebExample

Un proyecto de demostración *end-to-end* integrando el marco ligero de inyección y ruteo **JettraServer** y el motor innovador de interfaces de usuario **JettraWUI**. Este proyecto está preparado para compilación Java 25.

---

## 🚀 Requisitos

Asegúrate de haber instalado en tu repositorio local (`.m2`) los proyectos previos como pre-requisito, usando Maven en sus respectivos directorios:
- En `JettraWorkspace/JettraServer`: `mvn clean install`
- En `JettraWorkspace/JettraWUI`: `mvn clean install`

---

## 🛠️ Cómo Compilar y Ejecutar

El proyecto viene provisto con un `pom.xml` pre-configurado para empaquetar el ejecutable sin configuraciones manuales de red.

Para compilar el proyecto de ejemplo, posicionese en la raíz (`JettraWebExample/`) y corre:

```bash
mvn clean install
```
El JAR final (incluyendo el manifest `Main-Class`) se depositará en `/target`.


### Ejecución Directa (Standalone)

Una vez compilado correctamente, arranca el JAR nativamente a través de tu máquina virtual `java`:

```bash
java -jar target/JettraWebExample-1.0-SNAPSHOT.jar
```
Esto inicializará e inyectará la prueba abstracta de tus clases `JettraWUI`, procediendo a incitar a `JettraServer` a crear el *TCP Listener* incrustado según la directiva configurada en `jettra-config.properties`.

---

## 🐳 Despliegue con Docker

El proyecto también incluye un `Dockerfile` base preconfigurado.

1. **Build Local de la Imagen**
```bash
docker build -t jettra-web-example:latest .
```

2. **Ejecutar el contenedor y mapear Tráfico HTTP**
Si has decidido dejar el puerto por defecto en tus *properties*, mapea `8080` de lo contrario modifícalo acoplantemente:
```bash
docker run -d -p 8080:8080 --name mi-jettra-web jettra-web-example:latest
```
