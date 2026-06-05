# Stage 1: Build the application using Maven with Eclipse Temurin (JDK 17)
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# This builds your JAR inside the cloud container
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image using Eclipse Temurin (JDK 17)
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/backend/target/backend-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]