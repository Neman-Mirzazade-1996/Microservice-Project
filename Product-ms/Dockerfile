FROM openjdk
RUN echo "orders-ms"
COPY ./build/libs/Product-ms-0.1.jar /app/
WORKDIR /app/


ENTRYPOINT ["java"]
CMD ["-jar", "/app/Product-ms-0.1.jar"]