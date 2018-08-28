package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Material;
import service.MaterialService;
import base.BaseResource;

@Path("/material")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialResource extends BaseResource<Material, MaterialService> {

	@Inject
	private MaterialService service;

	@Override
	public MaterialService getService() {
		// TODO Auto-generated method stub
		return service;
	}
}
