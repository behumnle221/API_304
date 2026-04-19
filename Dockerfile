FROM eclipse-temurin:11-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]