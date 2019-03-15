package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Simulacion;
import service.SimulacionService;
import utils.AppException;
import base.BaseResource;

@Path("/simulacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacionResource extends
		BaseResource<Simulacion, SimulacionService> {

	@Inject
	private SimulacionService service;

	@Override
	public SimulacionService getService() {

		return service;
	}

	/**
	 * Prueba lista curso por alumno inscriptos al curso. y hace el primer test
	 * que evalua su condicion actual
	 * 
	 * @param idAlumno
	 * @throws AppException
	 * **/
	@GET
	@Path("/simulacion01")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion() throws NoSuchFieldException, AppException {
		System.out.println("simulacion ");
		return service.simulacion(httpRequest);
	}

	/**
	 * Simula el tutor.
	 * 
	 * @param idAlumno
	 * @throws AppException
	 * **/
	@GET
	@Path("/simulacion02Tutor/{idAsig}/{idAlu}/{idTarea}/{idArchivo}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacionTutor(@PathParam("idAsig") Long idAsig,
			@PathParam("idAlu") Long idAlu, @PathParam("idTarea") Long idTarea,
			@PathParam("idArchivo") Long idArchivo)
			throws NoSuchFieldException, AppException {
		System.out.println("simulacion tutor ");
		return service.simulacionTutor(httpRequest, idAsig, idAlu, idTarea,
				idArchivo);
	}

	/**
	 * crea la asignatura con los materiales hasta creacion de curso.
	 * 
	 * @param idAlumno
	 * @throws AppException
	 * **/
	@GET
	@Path("/simulacion03")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion03() throws NoSuchFieldException, AppException {
		System.out.println("simulacion de creacion de asignatura hasta curso");
		return service.simulacionAsignaturaCompleta(httpRequest);
	}

	/**
	 * crea alumnos con la asignatura 1 y curso 1 y hace el primer test
	 * **/
	@GET
	@Path("/simulacion04/{inicio}/{fin}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion04(@PathParam("inicio") Integer inicio, @PathParam("fin") Integer fin) throws NoSuchFieldException, AppException {
		System.out.println("simulacion de carga de alumnos y primer test");
		return service.simulacionAlumnosPrueba(httpRequest,inicio,fin);
	}

	/**
	 * Simula el tutor con un rango de id de alumnos.
	 * 
	 * @param idAlumno
	 * @throws AppException
	 * **/
	@GET
	@Path("/simulacion05/{idAsig}/{idTarea}/{idArchivo}/{inicio}/{fin}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacionTutorConAlu(@PathParam("idAsig") Long idAsig,
			@PathParam("idTarea") Long idTarea,
			@PathParam("idArchivo") Long idArchivo,
			@PathParam("inicio") Integer inicio, @PathParam("fin") Integer fin)
			throws NoSuchFieldException, AppException {
		System.out.println("simulacion tutor con varios alumnos");
		return service.simulacionTutorVarios(httpRequest, idAsig, idTarea,
				idArchivo, inicio, fin);
	}
	
	
	/**
	 * crea alumnos con la asignatura 1 y curso 1 y sin hacer el primer test
	 * **/
	@GET
	@Path("/simulacion06/{inicio}/{fin}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion06(@PathParam("inicio") Integer inicio, @PathParam("fin") Integer fin) throws NoSuchFieldException, AppException {
		System.out.println("simulacion de carga de alumnos y sin primer test");
		return service.simulacionAlumnosSinTest(httpRequest,inicio,fin);
	}
	
}
