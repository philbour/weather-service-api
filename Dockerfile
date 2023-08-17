FROM eclipse-temurin:11-jre-alpine

ARG JAR_FILE

COPY ${JAR_FILE} weather-service-api.jar

EXPOSE 8080

CMD ["java", "-jar", "weather-service-api.jar"]
