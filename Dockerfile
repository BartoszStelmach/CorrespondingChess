# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY gradle/ gradle
COPY gradlew build.gradle ./
RUN ./gradlew build

COPY src ./src

CMD ["./gradlew", "run"]