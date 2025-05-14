FROM openjdk
RUN echo "Product-ms"
COPY ./build/libs/Order-ms-0.1.jar /app/
WORKDIR /app/


ENTRYPOINT ["java"]
CMD ["-jar", "/app/Order-ms-0.1.jar"]

