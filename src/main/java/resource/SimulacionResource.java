package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Simulacion;
import seguridad.SessionController;
import service.SimulacionService;
import shiro.Credenciales;
import utils.AppException;
import base.BaseResource;

@Path("/simulacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacionResource extends
		BaseResource<Simulacion, SimulacionService> {

	@Inject
	private SimulacionService service;

	@Inject
	private SessionController controladorService;

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
	@Path("/simulacion04/{idAsig}/{inicio}/{fin}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion04(@PathParam("idAsig") Long idAsig,
			@PathParam("inicio") Integer inicio, @PathParam("fin") Integer fin)
			throws NoSuchFieldException, AppException {
		System.out.println("simulacion de carga de alumnos y primer test");
		return service
				.simulacionAlumnosPrueba(httpRequest, idAsig, inicio, fin);
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
	 * Solo de nivel medio. siempre es inicio el numero que necesitamos y el
	 * final la cantidad Cambio fecha 11/09/2019 Se cambia a curso 2, asigntura
	 * 2 y estilo de aprendizaje lector para pruebas
	 * **/
	@GET
	@Path("/simulacion06/{inicio}/{fin}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion06(@PathParam("inicio") Integer inicio,
			@PathParam("fin") Integer fin) throws NoSuchFieldException,
			AppException {

		Integer aluInteger = new Integer(229);
		Credenciales credenciales = new Credenciales();
		credenciales.setPassword("marzo");
		credenciales.setUsername("4250090" + aluInteger.toString());
		controladorService.session(credenciales);

		System.out.println("simulacion de carga de alumnos y sin primer test");
		String resul = service.simulacionAlumnosSinTest(httpRequest, inicio,
				fin);

		controladorService.cerrarSesion();
		return "finalizo la carga de alumnos " + resul;
	}

	/**
	 * Generar arbol bayesianos de todo los alumnos y de la asignatura
	 * **/
	@GET
	@Path("/simulacion07")
	@Produces(MediaType.APPLICATION_JSON)
	public void simulacion07() throws NoSuchFieldException, AppException {
		System.out
				.println("simulacion de generacion de arboles bayesianos para asignatura y alumnos.");
		service.simulacionGeneracionDeArbolesBayesianos(httpRequest);
		System.out.println("Fin");
	}

	/**
	 * Generar password de los usuarios
	 * **/
	@GET
	@Path("/simulacion08")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion08() throws NoSuchFieldException, AppException {

		System.out.println("simulacion de generacion de pass para usuarios");
		return service.simulacionGeneracionpass(httpRequest);

	}

	// nivel de asignatura
	@GET
	@Path("/simulacion09/{asignatura}/{alumno}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion09(@PathParam("asignatura") Long asignatura,
			@PathParam("alumno") Long alumno) throws NoSuchFieldException,
			AppException {

		System.out.println("simulacion alumno");
		return service.simulacionValoresAsignatura(asignatura, alumno,
				httpRequest);

	}

	// comparacion entre el test inicial y el test final
	@GET
	@Path("/comparacion")
	@Produces(MediaType.APPLICATION_JSON)
	public String comparacion() throws NoSuchFieldException, AppException {

		System.out.println("comparacion de test inicial y test final");
		return service.comparacion(httpRequest);

	}

	// prueba con lista de ejercicios y materiales útiles
	@GET
	@Path("/materialesUtiles")
	@Produces(MediaType.APPLICATION_JSON)
	public String materialesUtiles() throws NoSuchFieldException, AppException {

		System.out.println("Materiales utiles y ejercicios");
		return service.materialesUtilesEjercicios(httpRequest);

	}

	/**
	 * crea la asignatura con los materiales hasta creacion de curso.
	 * 
	 * @param idAlumno
	 * @throws AppException
	 * **/
	@GET
	@Path("/simulacion10")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion10() throws NoSuchFieldException, AppException {
		System.out.println("simulacion de creacion de asignatura hasta curso"
				+ "curso de bigdata");
		return service.simulacionAsignaturaCompleta(httpRequest);
	}

	// comparacion entre el inicio y fin de test tutor para quitar resultados.
	@GET
	@Path("/comparacionTutor/{inicio}/{fin}")
	@Produces(MediaType.APPLICATION_JSON)
	public String comparacionTutor(@PathParam("inicio") Long inicio,
			@PathParam("fin") Long fin) throws NoSuchFieldException,
			AppException {

		System.out.println("comparacion de test tutor para quitar resultados");
		return service.comparacionTutor(httpRequest, inicio, fin);

	}

	// comparacion entre el inicio y fin de test tutor para quitar resultados.
	@GET
	@Path("/longitud/{inicio}/{fin}")
	@Produces(MediaType.APPLICATION_JSON)
	public String longitudCaminoMaterial(@PathParam("inicio") Long inicio,
			@PathParam("fin") Long fin) throws NoSuchFieldException,
			AppException {

		System.out.println("Longitud Camino Material: ");
		return service.longitudCaminoMateriales(inicio, fin, httpRequest);

	}

	
	
	
	
	
	
	
	
	
	
	
	


}
