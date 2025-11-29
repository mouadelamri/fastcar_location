FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests clean package -e -B

FROM eclipse-temurin:21-jdk
WORKDIR /app
ARG JAR_FILE=/workspace/target/fastcar-location-1.0.0.jar
COPY --from=build ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
