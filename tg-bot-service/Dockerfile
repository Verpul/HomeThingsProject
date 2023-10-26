FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY tg-bot-service/pom.xml ./tg-bot-service/
COPY tg-bot-service/src ./tg-bot-service/src/
RUN mvn -B -f tg-bot-service/pom.xml clean package -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.20.1_1-alpine
EXPOSE 9000
COPY --from=build /app/tg-bot-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]