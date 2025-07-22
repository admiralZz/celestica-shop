FROM openjdk:17-jdk-slim
COPY build/libs/admin-back-module-1.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]