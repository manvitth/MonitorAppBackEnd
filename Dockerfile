# Use a base image with OpenJDK 8
FROM openjdk:8-jdk-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/MonitorApp-0.0.1-SNAPSHOT.war /app/app.jar

# Install MySQL client
RUN apk --no-cache add mysql-client

# Expose the port that the application will run on
EXPOSE 9090

# Define environment variables for MySQL connection
ENV DB_URL=jdbc:mysql://localhost:3306/monitor_app
ENV DB_USERNAME=admin
ENV DB_PASSWORD=Admin@12345$

# Use a non-root user
RUN adduser -D nonrootuser
USER nonrootuser

# Define the default command to run your application
CMD ["java", "-jar", "app.jar"]
