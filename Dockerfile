
# Use uma imagem Maven para construir o projeto
FROM eclipse-temurin:17-jdk-alpine

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo pom.xml e o diretório src para o contêiner
COPY target/modelo-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta em que a aplicação Spring Boot está configurada para rodar
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]