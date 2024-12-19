# Step 1: Use an official Gradle image to build the application
FROM gradle:jdk21-corretto AS builder
WORKDIR /app

# Copy only necessary files to leverage Docker cache efficiently
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew clean build

# Copy the entire project
COPY . .

# Build the Spring Boot application
RUN ./gradlew bootJar

# Step 2: Use a minimal base image to run the application
FROM amazoncorretto:21-alpine AS runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_AI_OPENAI_API_KEY=""

# Expose the application's port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]