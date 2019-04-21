package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Camino;
import service.CaminoService;
import utils.AppException;
import base.BaseResource;

@Path("/camino")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CaminoResource extends BaseResource<Camino, CaminoService> {

	@Inject
	private CaminoService service;

	@Override
	public CaminoService getService() {
		// TODO Auto-generated method stub
		return service;
	}

	/**
	 * Se encarga de insertar un nuevo registro. recibe un id_alumno, id_tarea
	 * @throws AppException 
	 */
	@GET
	@Path("/caminoAnterior")
	@Produces(MediaType.APPLICATION_JSON)
	public Camino CaminoAnterior() throws AppException {
		
		Long idTarea = new Long(1);
		Long idAlumno = new Long(44);
		Long idAsig = new Long(1);
		Long idConcepto = new Long(5);
		Camino camino = new Camino(); 
		camino = service.caminoAnterior(idAlumno, idTarea, idConcepto, idAsig, httpRequest);
		System.out.println("camino numero: "+camino.getId());	
	return camino;
	
	}

}
