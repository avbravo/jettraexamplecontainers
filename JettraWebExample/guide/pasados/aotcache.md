# JettraWebExample - Optimización con AOT Cache (AppCDS)

Esta guía documenta la implementación de AOT Cache (Ahead-of-Time Cache / Application Class Data Sharing) en el proyecto **JettraWebExample**. El uso de AOT Cache reduce significativamente el tiempo de inicio (startup time) y el consumo de memoria (RAM footprint) del contenedor al pre-procesar y compartir la metadata de las clases de Java.

## 1. ¿Qué es AOT Cache?

AOT Cache (introducido como evolución de AppCDS en versiones recientes de Java 24/25) permite que la JVM "recuerde" las clases que cargó durante una ejecución previa. En lugar de leer los archivos `.jar`, analizar el bytecode y verificar las clases cada vez que se inicia la aplicación, la JVM vuelca este estado interno en un archivo de caché persistente (`app.aot` o `app.jsa`).

En las siguientes ejecuciones, la JVM mapea este archivo directamente en memoria (mediante `mmap`), acelerando drásticamente el proceso de inicialización.

## 2. Implementación en el Dockerfile

Debido a que utilizamos una imagen base ultra-ligera y optimizada (**Alpaquita Linux + BellSoft Liberica JRE 25**), el soporte para AOT Cache se ha integrado directamente en el proceso de construcción del contenedor (`Dockerfile`).

La estrategia consta de **dos pasos principales** que ocurren durante el `docker build`:

### A. Ejecución de Entrenamiento (Training Run)

Para saber qué clases cachear, necesitamos ejecutar la aplicación brevemente durante el `build`. Como `JettraServer` es un servidor web continuo, usamos el comando `timeout` de Linux para detenerlo después de unos segundos, forzando a la JVM a guardar el estado justo al salir.

```dockerfile
# Se ejecuta la aplicación durante 10 segundos y se guarda la caché al finalizar
RUN timeout 10s java -XX:ArchiveClassesAtExit=app.jsa -jar JettraWebExample.jar || true
```
*(Nota: El `|| true` asegura que el código de salida de `timeout` no interrumpa la construcción de la imagen Docker).*

### B. Ejecución en Producción (Runtime)

En el `ENTRYPOINT` del Dockerfile, configuramos a la JVM para que inicie utilizando el archivo de caché generado:

```dockerfile
# Se activa SharedArchiveFile para aprovechar el AOT Cache
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -Xmx512m -XX:MaxRAMPercentage=75.0 -XX:SharedArchiveFile=app.jsa"
```

## 3. Beneficios Obtenidos

Al correr **JettraWebExample** con AOT Cache activado bajo Alpaquita Linux, los beneficios incluyen:
- **Arranque Ultrarrápido:** El servidor embebido JettraServer estará listo para aceptar conexiones HTTP en una fracción del tiempo original.
- **Reducción de Memoria:** Al compartir la metadata de las clases, contenedores múltiples corriendo la misma imagen pueden compartir páginas de memoria del sistema operativo.
- **Menor consumo de CPU:** Se elimina el alto pico de uso de CPU que normalmente ocurre durante los primeros segundos de vida de una aplicación Java (warm-up phase).

## 4. Troubleshooting

- **Caché Inválida:** Si recompilas tu código o cambias alguna dependencia (modificando el Fat JAR), la firma del archivo `.jar` cambiará. El AOT Cache se invalidará y deberás reconstruir la imagen Docker para regenerarlo.
- **Warnings de SharedArchiveFile:** Si al arrancar el contenedor ves advertencias sobre "Shared archive file not found", verifica que el comando `timeout` durante el `docker build` haya tenido tiempo suficiente para inicializar el framework.
