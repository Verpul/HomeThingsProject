FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY api-service/pom.xml ./api-service/
COPY api-service/src ./api-service/src/
RUN mvn -B -f api-service/pom.xml clean package -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.20.1_1-alpine
EXPOSE 8082
COPY --from=build /app/api-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]