# Order-Management

The Order-Management provides a service to order a specific quantity of a desired resource from an inventory.
If there are enough units of the desired resource available, it will be booked from the inventory.\
If the desired resource is not available, a production order is sent to the Machine-Service, provided that a machine and enough units of the auxiliary resource to produce the desired resource are available.\
To fulfill this service  it requires a Inventory- and Machine-Service.
Communication with the other services is done via **REST** and **message-based** protocols.


## Provided Service

The provided service can be accessed via a **REST Get-Request**, as illustrated in **Figure 1**. 
Three query parameters must be provided: 
1. The ID of the desired resource 
2. The name of the desired resource
3. The desired quantity.

The response contains a ResourceSet consisting of the desired resource (id, name) and its amount.

![Figure 1](Dokumentation/Provided%20Request.png)

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
To deploy the service with Docker follow these steps:

1. Clone this repository
2. Install [Docker](https://docs.docker.com/engine/install/)
3. Go to the order-management directory
4. Run this command in the directory:

```bash
  docker compose up
```