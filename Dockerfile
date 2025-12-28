# ============================================
# Stage 1: Build
# ============================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar apenas pom.xml primeiro (cache de dependências)
COPY pom.xml .

# Baixar dependências (essa camada será cacheada)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build da aplicação (pular testes aqui, vamos rodar separado)
RUN mvn clean package -DskipTests -B

# ============================================
# Stage 2: Runtime
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Mudar ownership
RUN chown -R spring:spring /app

# Trocar para usuário não-root
USER spring:spring

# Expor porta
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Executar aplicação
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"]