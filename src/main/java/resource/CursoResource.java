package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Curso;
import service.CursoService;
import base.BaseResource;

@Path("/curso")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoResource extends BaseResource<Curso, CursoService> {
	
	@Inject
    private CursoService service;
	
	@Override
	public CursoService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}