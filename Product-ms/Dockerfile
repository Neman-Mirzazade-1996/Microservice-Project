FROM openjdk
RUN echo "order-ms"
COPY ./build/libs/Product-ms-0.1.jar /app/
WORKDIR /app/


ENTRYPOINT ["java"]
CMD ["-jar", "/app/Product-ms-0.1.jar"]