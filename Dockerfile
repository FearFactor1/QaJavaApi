FROM maven:3.8.4-openjdk-17
WORKDIR /tests
COPY . .
CMD mvn clean test
