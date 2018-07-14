package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Sesion;
import service.SesionService;
import base.BaseResource;

@Path("/sesion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SesionResource extends BaseResource<Sesion, SesionService> {

	@Inject
	private SesionService service;

	@Override
	public SesionService getService() {
		
		return service;
	}


}
