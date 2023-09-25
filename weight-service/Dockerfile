FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY weight-service/pom.xml ./weight-service/
COPY weight-service/src ./weight-service/src/
RUN mvn -B -f weight-service/pom.xml clean package -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.20.1_1-alpine
EXPOSE 8081
COPY --from=build /app/weight-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]