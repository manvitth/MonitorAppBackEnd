# Stage 1: Build the application
FROM openjdk:8-jdk-alpine AS build

WORKDIR /app

COPY target/MonitorApp-0.0.1-SNAPSHOT.war /app/app.jar

# Stage 2: Create the final image
FROM openjdk:8-jre-alpine

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/app.jar .

# Define the default command to run your application
CMD ["java", "-jar", "app.jar"]
