FROM eclipse-temurin:21-jre-alpine

WORKDIR ./

COPY target/favoritos-0.0.1-SNAPSHOT.jar app.jar # Para Maven

# Expõe a porta que sua aplicação Spring Boot usa (padrão é 8080)
EXPOSE 8080

# Comando para executar a aplicação JAR quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]

