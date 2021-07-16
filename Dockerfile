FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/backofficeinvestor-0.0.1-SNAPSHOT.jar /app
EXPOSE 80
CMD ["java","-jar","backofficeinvestor-0.0.1-SNAPSHOT.jar"]
