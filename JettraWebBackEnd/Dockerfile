# Empleando una imagen ligera orientada a un runtime óptimo y moderno
# Usando BellSoft Liberica JRE con Alpaquita Linux para menor tamaño y mayor rendimiento

#FROM bellsoft/alpaquita-linux-base:stream-musl

FROM bellsoft/liberica-runtime-container:jre-25-alpaquita

LABEL maintainer="Jettra Development Team"
LABEL description="Contenedor de Demostración JettraWeb Example optimizado con Alpaquita"

WORKDIR /app

# Copia el JAR compilado desde target de su entorno en Host
COPY target/JettraBackEnd-1.0-SNAPSHOT.jar /app/JettraWebBackEnd.jar

# Entrenar la JVM para generar el AOT Cache (AppCDS)
# Ejecutamos la app por 10 segundos para cargar las clases y luego forzamos la salida guardando el archivo .jsa
RUN timeout 10s java -XX:ArchiveClassesAtExit=app.jsa -jar JettraWebBackEnd.jar || true

# Exponer el puerto por defecto (configurable via -e variables de entorno o property config)
EXPOSE 8080

# Opciones de JVM para optimización de rendimiento en contenedores y habilitación de AOT Cache
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -Xmx512m -XX:MaxRAMPercentage=75.0 -XX:SharedArchiveFile=app.jsa"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar JettraWebBackEnd.jar"]
