FROM openjdk:17-jdk-slim
COPY build/libs/client-back-module-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]