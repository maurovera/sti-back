package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Asignatura;
import service.AsignaturaService;
import base.BaseResource;

@Path("/asignatura")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AsignaturaResource extends BaseResource<Asignatura, AsignaturaService>{

	@Inject
    private AsignaturaService service;
	
	@Override
	public AsignaturaService getService() {
		// TODO Auto-generated method stub
		return service;
	}
}
