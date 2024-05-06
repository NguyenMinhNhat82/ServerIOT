
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:11-windowsservercore
COPY --from=build /target/ServerIOT-1.0-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","app.jar"]