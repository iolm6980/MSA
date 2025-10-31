# 🧱 빌드 단계
FROM gradle:8.5-jdk21 AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew :config-server:bootJar --no-daemon
# 🐳 실행 단계
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/config-server/build/libs/config-server.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
