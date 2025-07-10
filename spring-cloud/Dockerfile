FROM openjdk:21-jdk

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=Asia/Baku", "-jar", "/app.jar"]