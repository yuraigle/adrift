FROM openjdk:21-slim
LABEL authors="yuraigle"

EXPOSE 8080

COPY target/adrift.jar /adrift.jar

ENTRYPOINT ["java", "-jar", "/adrift.jar"]
