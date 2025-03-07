# Step #1: build
FROM gradle:7.4.0-jdk17 AS build

WORKDIR /app

COPY settings.gradle .
COPY build.gradle .
COPY src ./src

RUN gradle build --no-daemon

# Step #2: execute
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
