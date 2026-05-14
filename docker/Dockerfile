FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

EXPOSE 8081

COPY target/ecommerce-*.jar app.jar

RUN mkdir -p /app/images

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]