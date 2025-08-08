FROM openjdk:21-jdk
COPY key-cloak.jar /app/key-cloak.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
WORKDIR /app
ENTRYPOINT ["java"]
CMD ["-Duser.timezone=Asia/Baku", "-jar", "/app/key-cloak.jar"]
