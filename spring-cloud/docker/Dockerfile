FROM openjdk:21-jdk
COPY spring-cloud.jar /app/spring-cloud.jar
WORKDIR /app
ENTRYPOINT ["java", "-Duser.timezone=Asia/Baku", "-jar", "/app/spring-cloud.jar"]