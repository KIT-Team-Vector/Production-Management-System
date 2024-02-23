package assets;

import edu.kit.pms.im.common.concepts.ResourceSetImpl;
import edu.kit.pms.im.common.controllers.InventoryController;
import edu.kit.pms.im.common.controllers.InventoryControllerImpl;
import edu.kit.pms.im.domain.ResourceSet;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/resource/set")
public class ResourceSetAsset {

	private InventoryController controller;

	public ResourceSetAsset() {
		controller = InventoryControllerImpl.getInstance();
	}

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id) {
		ResourceSet resourceSet = controller.getResourceSet(id);
		if (resourceSet == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(resourceSet).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response post(ResourceSetImpl resourceSet) {
		ResourceSet createdResourceSet = controller.addResourceSet(resourceSet);
		if (createdResourceSet == null) {
			return Response.status(Status.CONFLICT).build();
		}
		return Response.status(Status.CREATED).entity(createdResourceSet)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") int id) {
		boolean success = controller.removeResourceSet(id);
		if (!success) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).build();
	}
}
