# Use official OpenJDK image as base
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy Maven project files
COPY . .

# Package the Spring Boot app (this generates the jar)
RUN ./mvnw clean package -DskipTests

# Expose port (same as your application.properties server.port)
EXPOSE 8082

# Run the application
CMD ["java", "-jar", "target/*.jar"]
