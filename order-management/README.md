# Order-Management

The Order-Management provides a service to order a specific quantity of a desired resource from an inventory.
If there are enough units of the desired resource available, it will be booked from the inventory.\
If the desired resource is not available, a production order is sent to the Machine-Service, provided that a machine and enough units of the auxiliary resource to produce the desired resource are available.\
To fulfill this service  it requires a Inventory- and Machine-Service.
Communication with the other services is done via **REST** and **message-based** protocols.

## Content
  - [Provided Service](#provided-service)
  - [Required Service](#required-services)
  - [Architecture](#architecture-)
  - [Deployment](#deployment)
    - [Environment Variables](#environment-variables-)



## Provided Service

The provided service can be accessed via a **REST Get-Request**, as illustrated below. 
Three query parameters must be provided: 
1. The ID of the desired resource 
2. The name of the desired resource
3. The desired quantity.

The response contains a ResourceSet consisting of the desired resource (id, name) and its amount.

![Figure 1](Dokumentation/Provided%20Request.png)

If the order-management is deployed as a **standalone** the **REST Request**

## Required Services

The required requests to fulfill the Order-Management service are listed here.

![Figure 2](Dokumentation/Required%20Requests.png)

## Architecture 
The architecture of the microservice follows the principles of the clean-architecture.\
This enables independence from the underlying framework and the technologies. \
The following diagram illustrates the various packages and their relationships within the architecture of the service.
A detailed depiction can be found [here](Dokumentation/Class-diagram.png).

![Figure 3](Dokumentation/Architecture.png)

## Deployment
The microservice can be deployed as a standalone docker container or as a docker compose with
a Kafka and Zookeeper container to run the Kafka-Server. 

To deploy the service with Kafka and Zookeeper follow these steps.

1. Clone this repository
2. Install [Docker](https://docs.docker.com/engine/install/)
3. Go to the order-management directory
4. Run this command in the directory:

```bash
  docker compose -f compose.yaml -f compose-with-kafka.yaml up
```
To deploy the service as a standalone run this command instead:

```bash
  docker compose up
```

- **Hint:** With the standalone you need to setup Kafka on your local system as described [here](https://medium.com/@shyamal.jadav/apache-kafka-with-spring-boot-application-e34d47c7b3e4).
### Environment Variables 

The environment variables for the Docker Compose can be changed in the compose.yaml file. 