FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

VOLUME /app/logs

ENTRYPOINT ["java", "-jar", "app.jar"]