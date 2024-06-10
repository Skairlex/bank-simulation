FROM openjdk:17-oracle

EXPOSE 8080

WORKDIR /api

COPY client-services/build/libs/*.jar /api/app.jar

RUN ls /api/

ENTRYPOINT exec java -jar app.jar
