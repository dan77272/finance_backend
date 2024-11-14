# Use an official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and project files
COPY . .

# Run Maven to install dependencies and build the project
RUN ./mvnw clean install -DskipTests

# Set the command to run the application
CMD ["java", "-Dserver.port=$PORT", "-jar", "target/personal-finance-management-0.0.1-SNAPSHOT.jar"]
