FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY local-service/pom.xml ./local-service/
COPY local-service/src ./local-service/src/
RUN mvn -B -f local-service/pom.xml clean package -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.20.1_1-alpine
EXPOSE 8081
COPY --from=build /app/local-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]