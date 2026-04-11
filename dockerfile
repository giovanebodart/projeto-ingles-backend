# ── Stage 1: build ──────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

# Copia só o pom primeiro para aproveitar cache de dependências
COPY ingles/pom.xml .
RUN mvn dependency:go-offline -q

# Copia o fonte e compila
COPY src ./src
RUN mvn package -DskipTests -q

# ── Stage 2: runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Usuário não-root por segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Copia o jar gerado
COPY --from=builder /build/target/*.jar app.jar

# Diretório para armazenamento de áudio (montado via volume)
RUN mkdir -p /app/audio && chown spring:spring /app/audio

USER spring

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]