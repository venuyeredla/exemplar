# Docker file for spring boot application
FROM eclipse-temurin:11-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]