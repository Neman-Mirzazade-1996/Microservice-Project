FROM openjdk:21-jdk
COPY key-cloak.jar /app/key-cloak.jar
WORKDIR /app
ENTRYPOINT ["java", "-Duser.timezone=Asia/Baku", "-jar", "/app/key-cloak.jar"]