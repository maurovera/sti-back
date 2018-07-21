package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	
	/**
	 * Se encarga de insertar un nuevo registro.
	 * recibe un id_alumno, id_tarea
	 */
	@POST
	@Path("/registrar")
	@Produces(MediaType.APPLICATION_JSON)
	public Sesion registrarSesion(@QueryParam("idAlumno")  Long idAlumno,
			@QueryParam("idTarea")  Long idTarea) {
		try {
			System.out.println("base resource de registrar sesion");
			return getService().registrarSesion(idAlumno, idTarea, httpRequest);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}

	
}
