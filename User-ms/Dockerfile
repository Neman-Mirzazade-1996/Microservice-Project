FROM openjdk
RUN echo "user-ms"
COPY ./build/libs/User-ms-0.1.jar /app/
WORKDIR /app/


ENTRYPOINT ["java"]
CMD ["-jar", "/app/User-ms-0.1.jar"]
