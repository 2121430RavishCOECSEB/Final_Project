# Use official OpenJDK with Maven
FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Expose the port
EXPOSE 8082

# Run the JAR file
CMD ["java", "-jar", "target/fitness-tracker-0.0.1-SNAPSHOT.jar"]

