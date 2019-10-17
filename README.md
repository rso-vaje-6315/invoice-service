# Invoice service
![build](https://travis-ci.org/rso-vaje-6315/invoice-service.svg)

## Usage

First, package project:

`mvn clean package -P rso`

Then, run JAR file:

`java -jar ./api/v1/target/invoice-service.jar`

## Build

To build docker image, run

`docker build -t invoice-service .`