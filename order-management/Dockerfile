# Use an official Maven image as a build stage
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the parent directory of your Maven module into the container
COPY . .

# Build your Maven project
RUN mvn clean package

# Use an official OpenJDK runtime as a parent image for the final image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the entire target folder from the build stage
COPY --from=build /usr/src/app/rest-service/target .

# Expose port 8080
EXPOSE 8080

# Run the necessary files from the target directory
CMD ["java", "-jar", "rest-service-0.0.1-SNAPSHOT.jar"]