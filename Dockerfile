FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/backOfficeInvestor-0.0.1-SNAPSHOT.jar /app
EXPOSE 80
CMD ["java","-jar","backOfficeInvestor-0.0.1-SNAPSHOT.jar"]
