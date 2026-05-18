FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace

COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY config config
COPY src src

RUN chmod +x gradlew && ./gradlew --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S bidmart && adduser -S bidmart -G bidmart

COPY --from=builder /workspace/build/libs/*SNAPSHOT.jar app.jar

USER bidmart
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
