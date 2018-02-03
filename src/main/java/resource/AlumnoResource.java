package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Alumno;
import service.AlumnoService;
import base.BaseResource;

@Path("/alumno")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlumnoResource extends BaseResource<Alumno, AlumnoService> {

	@Inject
	private AlumnoService service;

	@Override
	public AlumnoService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}
