package resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Curso;
import model.Tarea;
import service.CursoService;
import utils.AppException;
import utils.CursoView;
import base.BaseResource;
import base.ListaResponse;

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

	/**Borrar. Reemplazado por listaCursoDisponible**/
	@GET
	@Path("/listaCurso")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<CursoView> listarCurso(
			@QueryParam("idAlumno") @DefaultValue("1") Long idAlumno) throws NoSuchFieldException {
		System.out.println("Listar curso disponible resource");
		return getService().listarCurso(idAlumno);
	}
	
	/**Trae los curso en el cual el alumno con idAlumno no esta inscripto**/
	@GET
	@Path("listaCursoDisponible")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<CursoView> listarCursoDisponible(
			@QueryParam("idAlumno") @DefaultValue("74") Long idAlumno) throws NoSuchFieldException, AppException {
		System.out.println("Listar curso disponible resource");
		
		return getService().listarCursoDisponible(idAlumno);
	}
	

	
	/**Trae los curso en el cual el alumno con idAlumno esta inscripto**/
	@GET
	@Path("listaCursoInscripto")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<CursoView> listarCursoInscriptos(
			@QueryParam("idAlumno") @DefaultValue("45") Long idAlumno) throws NoSuchFieldException, AppException {
		System.out.println("Listar curso que el alumno esta inscripto resource");
		return getService().listarCursoInscriptos(idAlumno);
	}
	
	
	
	
	
	/**
	 * Prueba lista curso por alumno inscriptos al curso. 
	 * @param idAlumno
	 * **/
	@GET
	@Path("/listaCursoAlumno")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<CursoView> listarCursoPorAlumno(
	@QueryParam("idAlumno") @DefaultValue("1") Long idAlumno) throws NoSuchFieldException {
		System.out.println("Listar curso alumno resource");
		return service.listarCursoPorAlumno(idAlumno);
	}
	
	
	
	/**
	 * Se encarga de actualizar un recurso ya existente.
	 *
	 * @param id
	 * @param dto
	 *            el DTO que se desea actualizar en la base de datos.
	 * @return
	 *
	 */
	@PUT
	@Path("/agregarAlumno/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Curso agregarAlumno(@PathParam("id") Long id,Curso dto) {
		try {
			System.out.println("Curso resource agregarAlumno");
			getService().agregarAlumno(id, dto, httpRequest);
			return dto;
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	/**
	 * lista de tareas de un curso 
	 * @param idCurso
	 * @throws AppException 
	 * **/
	@GET
	@Path("/listaTarea")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tarea> listarTareas(
	@QueryParam("idCurso") @DefaultValue("1") Long idCurso) throws NoSuchFieldException, AppException {
		System.out.println("Lista de tareas resource de un curso");
		return service.listaTarea(idCurso);
	}
	
	

}