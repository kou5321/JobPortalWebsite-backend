# Use the Eclipse Temurin runtime as a parent image with Java 21
FROM eclipse-temurin:21-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/jobPortalWebsite-0.0.1-SNAPSHOT.jar /app/app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
