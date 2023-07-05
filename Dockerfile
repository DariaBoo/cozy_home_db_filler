# Openjdk as the build image
FROM maven:3.8-openjdk-17-slim AS build

# The working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the Maven project
RUN mvn clean install

# Intermediate container for copying the built artifact
FROM openjdk:17-jdk-slim AS final

# Intermediate container for copying the built artifact
#FROM mongo:latest AS final

# Install OpenJDK in the final image
#RUN apt-get update && apt-get install -y openjdk-17-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/product-service-0.0.1-SNAPSHOT.jar .

# Expose the application port
EXPOSE 8080
# Expose the MongoDB default port
#EXPOSE 27017

# Set the entry point to run the application
CMD ["java", "-jar", "product-service-0.0.1-SNAPSHOT.jar"]