FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY gateway-service/pom.xml ./gateway-service/
COPY gateway-service/src ./gateway-service/src/
RUN mvn -B -f gateway-service/pom.xml clean package -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.20.1_1-alpine
EXPOSE 9000
COPY --from=build /app/gateway-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]