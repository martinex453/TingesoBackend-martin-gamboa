FROM openjdk:22
ARG JAR_FILE=target/backend-martin-gamboa.jar
COPY ${JAR_FILE} backend-martin-gamboa.jar
ENTRYPOINT ["java","-jar", "/backend-martin-gamboa.jar"]