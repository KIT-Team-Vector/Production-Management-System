# Use an official Maven image as the base image
FROM maven:3.9.6-eclipse-temurin-21 AS build
MAINTAINER Tim Schilpp
# Set the working directory in the container
WORKDIR /src/pms
# Copy the pom.xml and the project files to the container
COPY . .
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM openjdk:21
# Set the working directory in the container
WORKDIR /src/app
# Copy the built JAR file from the previous stage to the container
COPY --from=build /src/pms/infrastructure/target .
EXPOSE 8080
# Set the command to run the application
CMD ["java", "-jar", "edu.kit.pms.machine-management.infrastructure.jar"]