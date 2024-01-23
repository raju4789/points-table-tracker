#Use the official OpenJDK base image
FROM openjdk:17-slim as builder

# Set the working directory in the Docker image
WORKDIR application

# Copy the layered JAR file to the working directory
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar

# Unpack the layered JAR file
RUN java -Djarmode=layertools -jar application.jar extract

# Use the OpenJDK image for the final image
FROM openjdk:17-slim

# Set the working directory in the Docker image
WORKDIR application

# Copy the unpacked layers from the builder stage
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

# Specify the entrypoint
ENTRYPOINT ["java", "com.tournament.pointstabletracker.PointsTableTrackerApplication.java"]
