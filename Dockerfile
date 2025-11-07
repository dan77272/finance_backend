# syntax=docker/dockerfile:1.7

# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache deps first
COPY pom.xml .
# BuildKit cache: give it a stable id
RUN --mount=type=cache,target=/root/.m2,id=maven-cache \
    mvn -q -DskipTests dependency:go-offline

# Then copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2,id=maven-cache \
    mvn -q -DskipTests package

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

ENV PORT=8080
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"
EXPOSE 8080

CMD ["sh","-c","java -Dserver.address=0.0.0.0 -Dserver.port=${PORT:-8080} -jar /app/app.jar"]




