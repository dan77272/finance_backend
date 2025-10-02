# syntax=docker/dockerfile:1.7

# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache deps first
COPY pom.xml .
# Use BuildKit cache mount for faster, repeatable builds
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests dependency:go-offline

# Then copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests package

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the fat jar (works for SNAPSHOT or release)
COPY --from=build /app/target/*.jar /app/app.jar

# Railway provides PORT. We'll bind to 0.0.0.0 and respect PORT.
ENV PORT=8080
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"

# (Optional on Railway, harmless elsewhere)
EXPOSE 8080

# Use sh -c so ${PORT} expands at runtime
CMD ["sh","-c","java -Dserver.address=0.0.0.0 -Dserver.port=${PORT:-8080} -jar /app/app.jar"]




