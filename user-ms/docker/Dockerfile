FROM openjdk:21-jdk
COPY user-ms.jar /app/user-ms.jar
WORKDIR /app
ENTRYPOINT ["java"]
CMD ["-Duser.timezone=Asia/Baku", "-jar", "/app/user-ms.jar"]
