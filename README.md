# Production-Management-System

The Production-Management-System was developed as a case study for analysis purposes. The system is composed out of three microservices which are used to simulate a production process within an industrial context.

The main goal of the the application to combine multiple communication paradigms such as REST and message-passing.

## Architecture 
The three microservives, inventory-management, machine-management, and order-management each have their own functionality and architecture within the system.

The inventory-management's main goal is to manage and persist resources and provide an easy way to access them. Further information can be found in the corresponding [README](./inventory-management/README.md).

The machine-management is used to manage the machines which consume and produce resources. Further information can be found in the corresponding [README](./machine-management/README.md).

The order-management provides a service to order a specific quantity of a desired resource from the inventory-management. It can also direct the machine-management to produce required resources. Further information can be found in the corresponding [README](./order-management/README.md). 

![overviewArchitecture](./documentation/???.png)

## Deployment
All microservices can be deployed independently from each other. To do so follow the instructions of the individual microservices.

Additionally, the system can be deployed as a whole on a single system using Docker. To do so clone this repository and run the following command within the root directory:

```powershell
  docker compose up
```