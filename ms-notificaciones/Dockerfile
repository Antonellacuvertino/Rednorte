FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S rednorte && adduser -S rednorte -G rednorte && mkdir -p /app/data && chown -R rednorte:rednorte /app
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8087
USER rednorte
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
