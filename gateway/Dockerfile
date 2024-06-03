FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar gateway.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/gateway.jar"]
