package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.Tarea;
import service.CursoService;
import service.TareaService;
import utils.AppException;
import utils.TareaAlumno;
import base.BaseResource;
import base.ListaResponse;

@Path("/tarea")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TareaResource extends BaseResource<Tarea, TareaService> {

	@Inject
	private TareaService service;
	
	@Inject
	private CursoService cursoService;

	@Override
	public TareaService getService() {
		// TODO Auto-generated method stub
		return service;
	}
	
	
	/**Trae la lista de camino recorrido por el alumno en el test tutor
	 * donde se trae su nivel inicial y final por concepto**/
	@GET
	@Path("/listaTareaAlumno")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<TareaAlumno> listarTareaAlumno(
			@QueryParam("idAlumno") @DefaultValue("63") Long idAlumno,
			@QueryParam("idCurso") @DefaultValue("1") Long idCurso
			) throws NoSuchFieldException, AppException {
		System.out.println("Listar tareas del alumno resource");
		
		return getService().listarTareasAlumno(idAlumno, idCurso, httpRequest);
	
	}

}