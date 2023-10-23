FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY eureka-service/pom.xml ./eureka-service/
COPY eureka-service/src ./eureka-service/src/
RUN mvn -B -f eureka-service/pom.xml clean package -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.20.1_1-alpine
EXPOSE 8761
COPY --from=build /app/eureka-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]