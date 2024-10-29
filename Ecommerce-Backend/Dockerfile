    FROM maven:3.8.7-openjdk-18-slim as builder

    WORKDIR /app
    COPY pom.xml .
    COPY src ./src

    RUN mvn clean package -DskipTests

    FROM openjdk:18-slim
    WORKDIR /app

    EXPOSE 9002

    COPY --from=builder /app/target/Ecommerce-0.0.1-SNAPSHOT.jar    /app/app.jar
    CMD ["java", "-jar", "app.jar"]
