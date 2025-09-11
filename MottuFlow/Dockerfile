FROM maven:3.9.11-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw package -DskipTests -B

FROM gcr.io/distroless/java21:nonroot AS rodador

WORKDIR /app

ENV DB_HOST=localhost \
    DB_PORT=3306 \
    DB_NAME=mottuflow \
    DB_USER=usuario \
    DB_PASSWORD=usuario123 \
    SERVER_PORT=8080

COPY --from=builder /app/target/MottuFlow-*.jar mottuflow.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mottuflow.jar"]