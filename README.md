## Reading List CRUD API

This project is a set of AWS Lambda functions that handle one each of the functions needed in a CRUD API.
It uses Maven to build. 
Adapted from [this sample](https://github.com/aws-samples/java-crud-microservice-template).

### Setup

`mvn install`

### Build

`mvn compile`

### Package a JAR for use as an AWS Lambda function

`mvn package shade:shade`
