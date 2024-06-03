FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareit-server.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/shareit-server.jar"]
