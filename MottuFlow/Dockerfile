FROM eclipse-temurin:21-jdk AS jlink

WORKDIR /app

RUN apt-get update && apt-get install -y zip unzip && rm -rf /var/lib/apt/lists/*

COPY target/MottuFlow-1.2.jar app.jar

RUN jlink \
    --output /jre \
    --add-modules java.base,java.compiler,java.datatransfer,java.desktop,java.instrument,java.logging,java.management,java.management.rmi,java.naming,java.rmi,java.scripting,java.security.jgss,java.sql,java.transaction.xa,java.xml,java.xml.crypto,jdk.crypto.cryptoki,jdk.crypto.ec,jdk.httpserver,jdk.management,jdk.management.agent,jdk.unsupported \
    --compress 2 \
    --no-header-files \
    --no-man-pages

FROM debian:bookworm-slim

WORKDIR /app

COPY --from=jlink /jre /opt/jre

COPY --from=jlink /app/app.jar /app/app.jar

ENV PATH="/opt/jre/bin:$PATH"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
