FROM gcr.io/distroless/java21:nonroot AS rodador
WORKDIR /app
ENV DB_HOST=${DB_HOST} \
    DB_PORT=${DB_PORT} \
    DB_NAME=${DB_NAME} \
    DB_USER=${DB_USER} \
    DB_PASSWORD=${DB_PASSWORD} \
    SERVER_PORT=8080
COPY target/*.jar app.jar
EXPOSE 8080
USER nonroot
ENTRYPOINT ["java", "-jar", "app.jar"]