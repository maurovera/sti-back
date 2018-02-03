package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import dao.TemaDAO;
import model.Tema;
import service.AsignaturaService;
import service.TemaService;
import base.BaseResource;

@Path("/tema")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemaResource extends BaseResource<Tema, TemaService> {

	@Inject
	private TemaService service;
	
	@Inject AsignaturaService serviceAsignatura;

	@Override
	public TemaService getService() {
		// TODO Auto-generated method stub
		return service;
	}
	

}
