FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

CMD ["java", "-Dserver.port=$PORT", "-jar", "target/personal-finance-management-0.0.1-SNAPSHOT.jar"]

