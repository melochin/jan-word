FROM openjdk:8-alpine
ENV spring.profiles.active pro
WORKDIR /app
COPY ./target/jan-word-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","jan-word-0.0.1-SNAPSHOT.jar"]


