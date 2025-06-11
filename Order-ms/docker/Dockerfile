FROM openjdk:21-jdk
COPY order-ms.jar /app/order-ms.jar
WORKDIR /app
ENTRYPOINT ["java"]
CMD ["-Duser.timezone=Asia/Baku", "-jar", "/app/order-ms.jar"]
