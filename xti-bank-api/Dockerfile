# Use Eclipse Temurin Java 25 JRE Alpine image (lightweight)
FROM eclipse-temurin:25-jre-alpine

# Set environment variables (optional)
ENV JAVA_OPTS=""

# Set working directory
WORKDIR /app

# Copy the Spring Boot fat jar into the container
COPY target/xti-bank-api.jar app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8080

# Entry point to start the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
