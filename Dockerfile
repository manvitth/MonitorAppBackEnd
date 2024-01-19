# Stage 1: Build the application
FROM openjdk:8-jdk-alpine AS build

WORKDIR /app

COPY target/MonitorApp-0.0.1-SNAPSHOT.war /app/app.jar

# Stage 2: Create the final image
FROM openjdk:8-jre-alpine

WORKDIR /app

# Install MySQL client
RUN apk --no-cache add mysql-client

# Expose the port that the application will run on
EXPOSE 9090

# Define environment variables for MySQL connection
ENV DB_URL=jdbc:mysql://mysql-container:3306/monitor_app
ENV DB_USERNAME=admin
ENV DB_PASSWORD=Admin@12345$

# Use a non-root user
RUN adduser -D nonrootuser
USER nonrootuser

# Copy the JAR file from the build stage
COPY --from=build /app/app.jar .

# Define the default command to run your application
CMD ["java", "-jar", "app.jar"]
