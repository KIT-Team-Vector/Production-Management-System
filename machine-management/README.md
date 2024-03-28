
# Machine-Management

The Machine-Management was developed as a part of the Production-Management-System. Its primary purpose is to manage and organize Machines that can be used to produce Resources from other Resources. To this end the Machine-Management requires a database and an Inventory service to store Machines and Resources respectively.


## Deployment

The easiest way of deploying the Machine-Management is to do so by running the Docker compose file of the Production-Management-System as this will build and run all dependent services. 

If the Machine-Management is to be deployed separately from the other services this can be done by running the Docker compose of the Machine-Management itself. To this end simply run the following command.

```bash
  docker compose up
```

**Note** This command requires Docker to be installed.

**Note** It might be necessary to adjust the environment variables in the Docker compose file.


## Environment Variables

When running the Machine-Management using Docker as described in the previous section the necessary environment variables will already be set. When starting the service using only the Docker image (not the compose file) these environment variables have to be configured.

`DB_SPRING_USER_NAME` The username for Spring to connect to the Machine database.

`DB_SPRING_USER_PW` The password for Spring to connect to the Machine database.

`DB_HOST` The host for the Machine database.

`DB_PORT` The port for the Machine database.

`DB_NAME` The name of the Machine database.

`INVENTORY_HOST` The host for the Inventory service.

`INVENTORY_PORT` The port for the Inventory service.

`KAFKA_BROKER_HOST` The host for the Kafka message broker.

`KAFKA_BROKER_PORT` The port for the Kafka message broker.


## API Reference

#### Check Machine Availability
```http
  GET /pms/mm/checkMachine/{resourceId}
```
| Path Variable | Type  | Description                              |
|:--------------|:------|:-----------------------------------------|
| `resourceId`  | `int` | **Required**. ID of the desired Resource |

Checks, whether a machine exists in the database that can produce the Resource with the provided ID. Iff this is the case, true will be returned.


#### Get Required Resource
```http
  GET /pms/mm/requiredResource/{resourceId}
```
| Path Variable | Type  | Description                              |
|:--------------|:------|:-----------------------------------------|
| `resourceId`  | `int` | **Required**. ID of the desired Resource |

Checks, which Resource is required to produce the Resource with the provided ID and returns it. 
**Warning** If no Machine is available that can produce the desired Resource this might lead to an error, so make sure to check the existence of such a Machine beforehand.


#### Produce Resources
```http
  POST /pms/mm/produce
```
| RequestBody   | Description                           |
|:--------------|:--------------------------------------|
| `ResourceSet` | **Required**. The desired ResourceSet |

Initiates the production of the desired ResourceSet. Returns true iff the production was successful. In this case, the newly produced Resources have already been added to the Inventory as well as the required Resources removed.
**Warning** If insufficient Resources are available or no such Machine exists this might lead to an error.





### Machine Database Configuration
The interfaces in this section are only required to add/remove machines from the service's database. When started using the Docker compose file a number of predefined machines will be already added to the database to simplify the process.

#### Add Machine
```http
  POST /pms/mm/machines/add
```
| Parameter            | Type     | Description                               |
|:---------------------|:---------|:------------------------------------------|
| `machineId`          | `int`    | **Required**. ID of the machine           |
| `inputResourceId`    | `int`    | **Required**. ID of the input Resource    |
| `inputResourceName`  | `String` | **Optional**. Name of the input Resource  |
| `outputResourceId`   | `int`    | **Required**. ID of the output Resource   |
| `outputResourceName` | `String` | **Optional**. Name of the output Resource |


#### Remove Machine
```http
  DELETE /pms/mm/machines/remove/{machineId}
```
| Path Variable | Type  | Description                     |
|:--------------|:------|:--------------------------------|
| `machineId`   | `int` | **Required**. ID of the machine |

