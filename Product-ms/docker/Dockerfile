FROM openjdk:21-jdk
COPY product-ms.jar /app/product-ms.jar
WORKDIR /app
ENTRYPOINT ["java"]
CMD ["-Duser.timezone=Asia/Baku", "-jar", "/app/product-ms.jar"]
