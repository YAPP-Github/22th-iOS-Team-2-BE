FROM openjdk:11
COPY build/libs/pyonsnalcolor-1.0.jar api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/api.jar"]

RUN echo "pyonsnalcolor server start"