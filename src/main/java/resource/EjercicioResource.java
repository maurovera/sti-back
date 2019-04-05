package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Ejercicio;
import model.Log;
import model.Resuelto;
import service.EjercicioService;
import service.LogService;
import service.ResueltoService;
import utils.AppException;
import utils.EjercicioView;
import utils.RespuestaCriterio;
import utils.RespuestaEjercicio;
import base.BaseResource;
import base.ListaResponse;

@Path("/ejercicio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EjercicioResource extends
		BaseResource<Ejercicio, EjercicioService> {

	@Inject
	private EjercicioService service;

	@Inject
	private LogService logService;
	
	@Inject
	private ResueltoService resueltoService;

	@Override
	public EjercicioService getService() {
		// TODO Auto-generated method stub
		return service;
	}

	/**
	 * Se encarga de insertar un nuevo registro.
	 *
	 * @param dto
	 *            el DTO que se desea insertar en la base de datos.
	 * @return el dto del recurso en formato json. Este DTO ya cuenta con el id
	 *         asignado por la secuencia.
	 */
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Ejercicio insertar(Ejercicio dto) {
		try {
			System.out.println("Ejercicio resource insertar");
			System.out.println(dto.getListaConceptos().get(0));
			System.out.println("Ejercicio resource. Lista de respuesta");
			System.out.println(dto.getListaRespuesta().get(0));
			return getService().insertar(dto, httpRequest);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
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
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ejercicio modificar(@PathParam("id") Long id, Ejercicio dto) {
		try {
			System.out.println("Ejercicio resource modificar");
			System.out.println(dto.getListaConceptos().get(0));
			getService().modificar(id, dto, httpRequest);
			return dto;
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Se encarga de traer una lista de ejercicios En un futuro proximo.
	 * Pasandole el id de curso trae todo los ejercicios relacionados a la
	 * asignatura ligada al curso.
	 **/
	@GET
	@Path("/listaEjercicio")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<EjercicioView> listarEjercicio()
			throws NoSuchFieldException {

		return getService().listarEjercicio();
	}

	/** Recurso para traer el siguiente ejercicio **/
	@GET
	@Path("/siguienteEjercicio")
	@Produces(MediaType.APPLICATION_JSON)
	public Ejercicio siguienteEjercicio(
			@QueryParam("idTarea") @DefaultValue("1") Long idTarea,
			@QueryParam("idAlumno") @DefaultValue("1") Long idAlumno,
			@QueryParam("idAsignatura") @DefaultValue("1") Long idAsignatura,
			@QueryParam("respuesta") @DefaultValue("r1") String respuesta,
			@QueryParam("idEjercicioAnterior") @DefaultValue("0") Long idEjercicioAnterior) {

		Ejercicio dto = null;
		try {
			dto = getService().siguienteEjercicio(idTarea, idAlumno,
					idAsignatura, respuesta, idEjercicioAnterior);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (dto == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return dto;

	}



	// ####seṕaracion de siguiente y responder
	/**
	 * Recurso para traer el siguiente ejercicio. Tiene que ser query para ser
	 * del front
	 **/
	@GET
	@Path("/siguiente/{idAsignatura}/{idTarea}/{idAlumno}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ejercicio siguienteEjercicioPrimerTest(
			@PathParam("idAsignatura") Long idAsignatura,
			@PathParam("idTarea") Long idTarea,
			@PathParam("idAlumno") Long idAlumno) {

		Ejercicio dto = null;
		try {
			dto = getService().siguienteEjercicioPrimerTest(idTarea, idAlumno,
					idAsignatura);
			System.out.println("el siguiente ejercicio es: " + dto.toString());
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (dto == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return dto;

	}

	/** Recurso para responder el ejercicio del primer test **/
	@POST
	@Path("/responder")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean responderEjercicioPrimerTest(
			RespuestaEjercicio respuestaEjercicio) {

		Boolean retorno = null;
		try {
			retorno = getService().responderEjercicio(
					respuestaEjercicio.getIdTarea(),
					respuestaEjercicio.getIdAlumno(),
					respuestaEjercicio.getIdAsignatura(),
					respuestaEjercicio.getRespuesta(),
					respuestaEjercicio.getIdEjercicio(), httpRequest);
			/** Aqui guardamos el log **/
			Log log = new Log();

			log.setAlumno(respuestaEjercicio.getIdAlumno());
			log.setAsignatura(respuestaEjercicio.getIdAsignatura());
			log.setTarea(respuestaEjercicio.getIdTarea());
			log.addSecuencia("Respuesta de 1er test.\n Alumno numero: ");
			log.addSecuencia(respuestaEjercicio.getIdAlumno().toString());
			log.addSecuencia("\n");
			log.addSecuencia(respuestaEjercicio.getIdAsignatura().toString());
			log.addSecuencia(respuestaEjercicio.getIdTarea().toString());
			log.addSecuencia(respuestaEjercicio.getIdEjercicio().toString());
			log.addSecuencia(respuestaEjercicio.getRespuesta());
			log.addSecuencia(retorno.toString());
			log.addSecuencia("\n");
			logService.insertar(log, httpRequest);
			
			/**Una vez que responde el ejercicio se guarda el ejercicio resuelto**/
			Resuelto resuelto = new Resuelto();
			resuelto.setEsMaterial(false);
			resuelto.setEsCorrecto(retorno);
			resuelto.setIdAlumno(respuestaEjercicio.getIdAlumno());
			resuelto.setIdAsignatura(respuestaEjercicio.getIdAsignatura());
			resuelto.setIdEjercicio(respuestaEjercicio.getIdEjercicio());
			resuelto.setIdTarea(respuestaEjercicio.getIdTarea());
			resuelto.setRespuesta(respuestaEjercicio.getRespuesta());
			resueltoService.insertar(resuelto, httpRequest);

		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (retorno == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return retorno;

	}

	@GET
	@Path("/criterio/{idTarea}/{idAlumno}")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean criterioParada(@PathParam("idTarea") Long idTarea,
			@PathParam("idAlumno") Long idAlumno) {

		Boolean criterio = false;
		try {
			criterio = getService().criterio(idTarea, idAlumno, httpRequest);
			if (criterio)
				System.out.println("parar el tema");
			else
				System.out.println("continuar con otro ejercicios");
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		return criterio;

	}

	
	// ####seṕaracion de siguiente y responder para el tutor
	/**
	 * Criterio de parada para tutor o segundo examen o prueba aqui se revisa si
	 * tiene conceptos disponibles, y si su cantidad de intentos no llego a su
	 * fin
	 * @throws AppException 
	 **/
	@GET
	@Path("/criterioTutor/{idAsignatura}/{idTarea}/{idAlumno}")
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaCriterio criterioParadaTutor(
			@PathParam("idAsignatura") Long idAsignatura,
			@PathParam("idTarea") Long idTarea,
			@PathParam("idAlumno") Long idAlumno) throws AppException {

		RespuestaCriterio resultado = new RespuestaCriterio();

		resultado = getService().criterioTutor(idAsignatura, idTarea, idAlumno,
				httpRequest);

		return resultado;

	}
	
	
	
	/**
	 * Recurso para traer el siguiente ejercicio del tutor
	 * 
	 **/
	@GET
	@Path("/siguienteEjercicio/{idAsignatura}/{idTarea}/{idAlumno}/{idConcepto}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ejercicio siguienteEjercicioSegundoTutor(
			@PathParam("idAsignatura") Long idAsignatura,
			@PathParam("idTarea") Long idTarea,
			@PathParam("idAlumno") Long idAlumno,
			@PathParam("idConcepto") Long idConcepto) {

		Ejercicio dto = null;
		try {
			dto = getService().siguienteEjercicioTutor(idConcepto, idAsignatura, idAlumno, idTarea);
			System.out.println("el siguiente ejercicio es: " + dto.toString());
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (dto == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return dto;

	}
	
	
	/** Recurso para responder el ejercicio del tutor **/
	@POST
	@Path("/responderEjercicioTutor")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean responderEjercicioTutor(
			RespuestaEjercicio respuestaEjercicio) {

		Boolean retorno = null;
		try {
			retorno = getService().responderEjercicioTutor(
					respuestaEjercicio.getIdTarea(),
					respuestaEjercicio.getIdAlumno(),
					respuestaEjercicio.getIdAsignatura(),
					respuestaEjercicio.getRespuesta(),
					respuestaEjercicio.getIdEjercicio(),
					respuestaEjercicio.getIdConcepto(), httpRequest);
			
			
			/**El algoritmo lo hacemos dentro de service de ejercicio
			 * en cuanto a sesionIntentos, sesionConcepto y camino*/
			
			
			
			
			/** Aqui guardamos el log **/
			Log log = new Log();
			log.setAlumno(respuestaEjercicio.getIdAlumno());
			log.setAsignatura(respuestaEjercicio.getIdAsignatura());
			log.setTarea(respuestaEjercicio.getIdTarea());
			log.addSecuencia("Respuesta de 1er test.\n Alumno numero: ");
			log.addSecuencia(respuestaEjercicio.getIdAlumno().toString());
			log.addSecuencia("\nSegundo test con el concepto: ");
			log.addSecuencia(respuestaEjercicio.getIdConcepto().toString());
			log.addSecuencia("\n");
			log.addSecuencia(respuestaEjercicio.getIdAsignatura().toString());
			log.addSecuencia(respuestaEjercicio.getIdTarea().toString());
			log.addSecuencia(respuestaEjercicio.getIdEjercicio().toString());
			log.addSecuencia(respuestaEjercicio.getRespuesta());
			log.addSecuencia(retorno.toString());
			log.addSecuencia("\n");
			logService.insertar(log, httpRequest);
			
			/**Una vez que responde el ejercicio se guarda el ejercicio resuelto**/
			Resuelto resuelto = new Resuelto();
			resuelto.setEsMaterial(false);
			resuelto.setEsCorrecto(retorno);
			resuelto.setIdAlumno(respuestaEjercicio.getIdAlumno());
			resuelto.setIdAsignatura(respuestaEjercicio.getIdAsignatura());
			resuelto.setIdEjercicio(respuestaEjercicio.getIdEjercicio());
			resuelto.setIdTarea(respuestaEjercicio.getIdTarea());
			resuelto.setRespuesta(respuestaEjercicio.getRespuesta());
			resuelto.setIdConcepto(respuestaEjercicio.getIdConcepto());
			resueltoService.insertar(resuelto, httpRequest);

		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (retorno == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return retorno;

	}


}
