package assets;

import edu.kit.pms.im.domain.Resource;
import edu.kit.pms.im.domain.ResourceSet;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/resource/set")
public class RessourceSetAsset {
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        ///return new ResourceSet(null, 2);
    	ResourceSet a = new ResourceSet(new Resource(1, "test"), 4);
    	Response s = Response.ok(a).build();
    	return s;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void post(ResourceSet ressourceSet) {
        //return new ResourceSet(null, 2);
    	//return new ResourceSet(new Resource(1, "test"), ressourceSet.getAmount());
    }
}
