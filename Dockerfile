# Stage 1: Build the application
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Build the project inside the backend directory
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Copy the built JAR from the backend target folder
COPY --from=build /app/backend/target/backend-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]