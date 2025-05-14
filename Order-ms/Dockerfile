FROM openjdk
RUN echo "order-ms"
COPY ./build/libs/Order-ms-0.1.jar /app/
WORKDIR /app/


ENTRYPOINT ["java"]
CMD ["-jar", "/app/Order-ms-0.1.jar"]

