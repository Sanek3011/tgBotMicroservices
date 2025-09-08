FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*


COPY /target/UserService-1.0.jar application.jar

ENTRYPOINT ["java", "-jar", "/application.jar"]