FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareit-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/shareit-gateway.jar"]
