FROM openjdk:17

WORKDIR /app

COPY ./target/taxguru-platform-0.0.1-SNAPSHOT.jar /app/taxguru.jar

CMD ["java", "-jar", "/app/taxguru.jar"]