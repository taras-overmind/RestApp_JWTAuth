FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ./target/contact-app-docker.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]