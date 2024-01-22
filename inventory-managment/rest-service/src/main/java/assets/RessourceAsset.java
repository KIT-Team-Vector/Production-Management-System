package assets;

import domain.Resource;
import domain.ResourceSet;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/resource")
public class RessourceAsset {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResourceSet get(@PathParam("id") String id) {
        //return new ResourceSet(null, 2);
    	return new ResourceSet(new Resource(1, "test"), 4);
    }
}
