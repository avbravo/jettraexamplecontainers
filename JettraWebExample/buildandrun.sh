echo "Compilando aplicacion y generando .jar"
mvn clean verify install
echo "Ejecutando la aplicación"

java -jar  -XX:+UseCompactObjectHeaders target/JettraWebExample-1.0-SNAPSHOT.jar
# java -jar  -XX:+UseCompactObjectHeaders target/JettraWebExample-1.0-SNAPSHOT.jar
