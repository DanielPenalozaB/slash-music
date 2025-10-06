FROM eclipse-temurin:21-jdk

ARG JAR_FILE=./music-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 7777

ENTRYPOINT ["java", "-jar", "/app.jar", "--server.address=0.0.0.0", "--spring.profiles.active=prod"]