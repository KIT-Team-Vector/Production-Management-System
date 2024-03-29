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

The required requests to fulfill the Order-Management service are listed in **Figure 2**.

![Figure 1](Dokumentation/Required%20Requests.png)
