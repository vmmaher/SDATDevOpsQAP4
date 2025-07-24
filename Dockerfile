FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy Maven files for dependency caching
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn clean package -DskipTests

# Run the application
EXPOSE 8080
CMD ["java", "-jar", "target/golf-api-0.0.1-SNAPSHOT.jar"]