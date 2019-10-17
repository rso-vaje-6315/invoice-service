# Invoice service

## Usage

First, package project:

`mvn clean package -P rso`

Then, run JAR file:

`java -jar ./api/v1/target/invoice-service.jar`

## Build

To build docker image, run

`docker build -t invoice-service .`