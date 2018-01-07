package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Profesor;
import service.ProfesorService;
import base.BaseResource;

@Path("/profesor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfesorResource extends BaseResource<Profesor, ProfesorService>{

	@Inject
    private ProfesorService service;
	
	@Override
	public ProfesorService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}
