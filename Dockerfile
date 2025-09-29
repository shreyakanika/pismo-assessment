# Use a minimal Java Runtime Environment image
FROM eclipse-temurin:17-jre-focal

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from the Maven build (assuming 'mvn package' was run)
# The JAR name should match your project's artifactId and version
# e.g., pismo-assessment-0.0.1-SNAPSHOT.jar
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
