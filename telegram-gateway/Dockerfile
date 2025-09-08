FROM openjdk:21-jdk-slim

COPY /target/telegram-gateway-1.0.jar application.jar

ENTRYPOINT ["java", "-jar", "/application.jar"]