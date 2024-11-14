# Use an official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pre-built JAR into the container
COPY target/personal-finance-management-0.0.1-SNAPSHOT.jar app.jar

# Set the command to run the application
CMD ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]

