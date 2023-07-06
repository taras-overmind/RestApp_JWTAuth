FROM openjdk:19
EXPOSE 8080
ADD target/contact-app-docker.jar
ENTRYPOINT ["java", "-jar", "contact-app-docker.jar"]