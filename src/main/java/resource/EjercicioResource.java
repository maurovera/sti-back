package resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

import model.Asignatura;
import model.Camino;
import model.Ejercicio;
import model.Log;
import model.Material;
import model.Resuelto;
import model.Sesion;
import service.AsignaturaService;
import service.EjercicioService;
import service.LogService;
import service.ResueltoService;
import service.SesionService;
import utils.AppException;
import utils.EjercicioView;
import utils.Regla;
import utils.RespuestaCriterio;
import utils.RespuestaEjercicio;
import base.AdministracionBase;
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

	@Inject
	private AsignaturaService asignaturaService;

	@Inject
	private AdministracionBase admService;

	@Inject
	private SesionService sesionService;

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

	/** Recurso para traer el siguiente ejercicio borrar **/
	@GET
	@Path("/siguienteEjercicioMalo")
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
	 * @throws AppException 
	 **/

	/*
	 * @GET
	 * 
	 * @Path("/siguiente/{idAsignatura}/{idTarea}/{idAlumno}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Ejercicio
	 * siguienteEjercicioPrimerTest(
	 * 
	 * @PathParam("idAsignatura") Long idAsignatura,
	 * 
	 * @PathParam("idTarea") Long idTarea,
	 * 
	 * @PathParam("idAlumno") Long idAlumno) {
	 */
	@GET
	@Path("/siguiente")
	@Produces(MediaType.APPLICATION_JSON)
	public Ejercicio siguienteEjercicioPrimerTest(
			@QueryParam("idAsignatura") Long idAsignatura,
			@QueryParam("idTarea") Long idTarea,
			@QueryParam("idAlumno") Long idAlumno){

		Ejercicio dto = null;
		dto = getService().siguienteEjercicioPrimerTest(idTarea, idAlumno,
				idAsignatura, httpRequest);
		
		if (dto != null) {
			System.out.println("el siguiente ejercicio es: " + dto.toString());
		 //throw new WebApplicationException(Response.Status.NOT_FOUND);
		 }else{
			 System.out.println("ejercicio nulo resource");
		 }
		
		

		return dto;

	}

	/**
	 * Recurso para responder el ejercicio del primer test
	 * 
	 * @throws AppException
	 **/
	@POST
	@Path("/responder")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean responderEjercicioPrimerTest(
			RespuestaEjercicio respuestaEjercicio) throws AppException {

		Boolean retorno = null;

		Resuelto resuelto = new Resuelto();

		/** Quitamos el nombre de la asignatura y tambien su nodo valor inicial **/
		Asignatura asignaturaResuelto = asignaturaService
				.obtener(respuestaEjercicio.getIdAsignatura());
		String nombreAsignatura = asignaturaResuelto.getNombre();
		Double valorInicialR = admService.getValorNodoRedDouble(
				nombreAsignatura, respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getIdAlumno());
		resuelto.setNivelInicial(valorInicialR);

		/** Aqui recien se responde */
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

		/**
		 * Una vez que responde el ejercicio se guarda el ejercicio resuelto
		 **/
		Double valorFinalR = admService.getValorNodoRedDouble(nombreAsignatura,
				respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getIdAlumno());
		resuelto.setNivelFinal(valorFinalR);
		resuelto.setEsMaterial(false);
		resuelto.setEsCorrecto(retorno);
		resuelto.setIdAlumno(respuestaEjercicio.getIdAlumno());
		resuelto.setIdAsignatura(respuestaEjercicio.getIdAsignatura());
		resuelto.setIdEjercicio(respuestaEjercicio.getIdEjercicio());
		resuelto.setIdTarea(respuestaEjercicio.getIdTarea());
		resuelto.setRespuesta(respuestaEjercicio.getRespuesta());

		Sesion seAnterior = sesionService.sesionAnterior(
				respuestaEjercicio.getIdAlumno(),
				respuestaEjercicio.getIdTarea());
		if (seAnterior.getTestFinal())
			resuelto.setTestFinal(true);
		else
			resuelto.setEsPrimerTest(true);

		resueltoService.insertar(resuelto, httpRequest);

		return retorno;

	}

	// @GET
	// @Path("/criterio/{idTarea}/{idAlumno}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public Boolean criterioParada(@PathParam("idTarea") Long idTarea,
	// @PathParam("idAlumno") Long idAlumno) {
	@GET
	@Path("/criterio")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean criterioParada(@QueryParam("idAlumno") Long idAlumno,
			@QueryParam("idTarea") Long idTarea) throws AppException {

		Boolean criterio = false;

		criterio = getService().criterio(idTarea, idAlumno, httpRequest);
		if (criterio)
			System.out.println("parar el tema");
		else
			System.out.println("continuar con otro ejercicios");

		return criterio;

	}

	// ####separacion de siguiente y responder para el tutor
	/**
	 * Criterio de parada para tutor o segundo examen o prueba aqui se revisa si
	 * tiene conceptos disponibles, y si su cantidad de intentos no llego a su
	 * fin
	 * 
	 * @throws AppException
	 **/
	/*
	 * @GET
	 * 
	 * @Path("/criterioTutor/{idAsignatura}/{idTarea}/{idAlumno}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public RespuestaCriterio
	 * criterioParadaTutor(
	 * 
	 * @PathParam("idAsignatura") Long idAsignatura,
	 * 
	 * @PathParam("idTarea") Long idTarea,
	 * 
	 * @PathParam("idAlumno") Long idAlumno) throws AppException {
	 */

	@GET
	@Path("/criterioTutor")
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaCriterio criterioParadaTutor(
			@QueryParam("idAsignatura") Long idAsignatura,
			@QueryParam("idTarea") Long idTarea,
			@QueryParam("idAlumno") Long idAlumno) throws AppException {
		RespuestaCriterio resultado = new RespuestaCriterio();

		System.out.println("estoy en criterioTutor Resource");
		resultado = getService().criterioTutor(idAsignatura, idTarea, idAlumno,
				httpRequest);
		System.out.println("fin de criterio tutor");
		return resultado;

	}

	/**
	 * Recurso para traer el siguiente ejercicio del tutor se cambia pathParam
	 * por queryParam y se borra los datos del link
	 **/
	@GET
	@Path("/siguienteEjercicio")
	@Produces(MediaType.APPLICATION_JSON)
	public Ejercicio siguienteEjercicioSegundoTutor(
			@QueryParam("idAsignatura") Long idAsignatura,
			@QueryParam("idTarea") Long idTarea,
			@QueryParam("idAlumno") Long idAlumno,
			@QueryParam("idConcepto") Long idConcepto) {

		System.out.println("estoy en siguientEjercicio resource");
		Ejercicio dto = null;
		try {
			dto = getService().siguienteEjercicioTutor(idConcepto,
					idAsignatura, idAlumno, idTarea);
			System.out.println("el siguiente ejercicio es: " + dto.toString());
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (dto == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		System.out.println("fin de ejercicio resource");
		return dto;

	}

	/**
	 * Recurso para responder el ejercicio del tutor
	 * 
	 * @throws AppException
	 **/
	@POST
	@Path("/responderEjercicioTutor")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean responderEjercicioTutor(RespuestaEjercicio respuestaEjercicio)
			throws AppException {

		/** Quitamos el nombre de la asignatura y tambien su nodo valor inicial **/
		Resuelto resuelto = new Resuelto();
		Asignatura asignaturaResuelto = asignaturaService
				.obtener(respuestaEjercicio.getIdAsignatura());
		String nombreAsignatura = asignaturaResuelto.getNombre();
		Double valorInicialR = admService.getValorNodoRedDouble(
				nombreAsignatura, respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getIdAlumno());
		resuelto.setNivelInicial(valorInicialR);

		System.out.println("estoy en responderEjercicioTutor resource");
		Boolean retorno = null;

		retorno = getService().responderEjercicioTutor(
				respuestaEjercicio.getIdTarea(),
				respuestaEjercicio.getIdAlumno(),
				respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getRespuesta(),
				respuestaEjercicio.getIdEjercicio(),
				respuestaEjercicio.getIdConcepto(), httpRequest);

		/**
		 * El algoritmo lo hacemos dentro de service de ejercicio en cuanto a
		 * sesionIntentos, sesionConcepto y camino
		 */

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

		/**
		 * Una vez que responde el ejercicio se guarda el ejercicio resuelto
		 **/
		Double valorFinalR = admService.getValorNodoRedDouble(nombreAsignatura,
				respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getIdAlumno());
		resuelto.setNivelFinal(valorFinalR);
		resuelto.setEsMaterial(false);
		resuelto.setEsCorrecto(retorno);
		resuelto.setIdAlumno(respuestaEjercicio.getIdAlumno());
		resuelto.setIdAsignatura(respuestaEjercicio.getIdAsignatura());
		resuelto.setIdEjercicio(respuestaEjercicio.getIdEjercicio());
		resuelto.setIdTarea(respuestaEjercicio.getIdTarea());
		resuelto.setRespuesta(respuestaEjercicio.getRespuesta());
		resuelto.setIdConcepto(respuestaEjercicio.getIdConcepto());
		resuelto.setEsPrimerTest(false);
		resueltoService.insertar(resuelto, httpRequest);

		System.out.println("fin de responder ejercicio tutor resource");
		return retorno;
	}

	/**
	 * Recurso para traer el siguiente material
	 * 
	 * @throws AppException
	 * 
	 **/
	@GET
	@Path("/siguienteMaterial")
	@Produces(MediaType.APPLICATION_JSON)
	public Material siguienteMaterial(
			@QueryParam("idAsignatura") Long idAsignatura,
			@QueryParam("idTarea") Long idTarea,
			@QueryParam("idAlumno") Long idAlumno,
			@QueryParam("idConcepto") Long idConcepto,
			@QueryParam("idArchivo") Long idArchivo) throws AppException {

		Material dto = null;
		System.out.println("estos son los datos que recibe: ");
		System.out.println("idAsignatura: " + idAsignatura);
		System.out.println("idTarea: " + idTarea);
		System.out.println("idAlumno: " + idAlumno);
		System.out.println("idConcepto: " + idConcepto);
		System.out.println("idArchivo: " + idArchivo);
		dto = getService().siguienteMaterial(idArchivo, idAlumno, idAsignatura,
				idConcepto, idTarea, httpRequest);
		if (dto == null) {
			/****/
			System.out.println("se envia un material nulo. ");
			dto = new Material();
		} else {
			System.out.println("el siguiente Material es: " + dto.toString());
		}
		return dto;

	}

	/**
	 * Recurso para responder el material visto. para guardar el camino
	 * correspondiente
	 * 
	 * @throws AppException
	 **/
	@POST
	@Path("/responderMaterial")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean responderMaterial(RespuestaEjercicio respuestaEjercicio)
			throws AppException {

		/** Quitamos el nombre de la asignatura y tambien su nodo valor inicial **/
		Resuelto resuelto = new Resuelto();
		Asignatura asignaturaResuelto = asignaturaService
				.obtener(respuestaEjercicio.getIdAsignatura());
		String nombreAsignatura = asignaturaResuelto.getNombre();
		Double valorInicialR = admService.getValorNodoRedDouble(
				nombreAsignatura, respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getIdAlumno());
		resuelto.setNivelInicial(valorInicialR);

		Boolean retorno = null;

		System.out.println("responderMaterialRecibe: ");
		System.out.println("idTarea: " + respuestaEjercicio.getIdTarea());
		System.out.println("idAlumno: " + respuestaEjercicio.getIdAlumno());
		System.out.println("idAsignatura: "
				+ respuestaEjercicio.getIdAsignatura());
		System.out.println("idConcepto: " + respuestaEjercicio.getIdConcepto());
		System.out.println("idMaterial: " + respuestaEjercicio.getIdMaterial());

		retorno = getService().responderMaterial(
				respuestaEjercicio.getIdTarea(),
				respuestaEjercicio.getIdAlumno(),
				respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getIdConcepto(),
				respuestaEjercicio.getIdMaterial(), httpRequest);

		/** Aqui guardamos el log **/
		Log log = new Log();
		log.setAlumno(respuestaEjercicio.getIdAlumno());
		log.setAsignatura(respuestaEjercicio.getIdAsignatura());
		log.setTarea(respuestaEjercicio.getIdTarea());

		log.addSecuencia("Material visto: ");
		log.addSecuencia(respuestaEjercicio.getIdMaterial().toString());
		log.addSecuencia("\n");
		logService.insertar(log, httpRequest);

		/**
		 * Una vez que responde el ejercicio se guarda el ejercicio resuelto
		 **/
		Double valorFinalR = admService.getValorNodoRedDouble(nombreAsignatura,
				respuestaEjercicio.getIdAsignatura(),
				respuestaEjercicio.getIdAlumno());
		resuelto.setNivelFinal(valorFinalR);
		resuelto.setEsMaterial(true);
		resuelto.setEsCorrecto(false);
		resuelto.setIdAlumno(respuestaEjercicio.getIdAlumno());
		resuelto.setIdAsignatura(respuestaEjercicio.getIdAsignatura());
		resuelto.setIdTarea(respuestaEjercicio.getIdTarea());
		resuelto.setIdConcepto(respuestaEjercicio.getIdConcepto());
		resuelto.setEsPrimerTest(false);
		resuelto.setIdMaterial(respuestaEjercicio.getIdMaterial());
		resueltoService.insertar(resuelto, httpRequest);

		return retorno;

	}

	// ##########INFORMES####################################################

	/**
	 * Trae la lista de resuelto por un alumno de una tarea de una asignatura en
	 * el primer test donde se quita el nivel
	 **/
	@GET
	@Path("/listaResueltoInicial")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<Resuelto> listarResueltoTestIncial(
			@QueryParam("idAlumno") @DefaultValue("63") Long idAlumno,
			@QueryParam("idTarea") @DefaultValue("1") Long idTarea)
			throws NoSuchFieldException, AppException {
		System.out.println("Listar resultados de primer test resource");

		return getService().listarResueltoTestIncial(idAlumno, idTarea);

	}

	/**
	 * Trae la lista de resuelto por un alumno de una tarea de una asignatura en
	 * el test tutor donde se quita el nivel
	 **/
	@GET
	@Path("/listaResueltoTestTutor")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<Resuelto> listarResueltoTestTutor(
			@QueryParam("idAlumno") @DefaultValue("63") Long idAlumno,
			@QueryParam("idTarea") @DefaultValue("1") Long idTarea)
			throws NoSuchFieldException, AppException {
		System.out.println("Listar resultados de test tutor resource");

		return getService().listarResueltoTesttutor(idAlumno, idTarea);

	}

	/**
	 * Trae la lista de camino recorrido por el alumno en el test tutor donde se
	 * trae su nivel inicial y final por concepto
	 **/
	@GET
	@Path("/listaCamino")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<Camino> listarCamino(
			@QueryParam("idAlumno") @DefaultValue("63") Long idAlumno,
			@QueryParam("idTarea") @DefaultValue("1") Long idTarea)
			throws NoSuchFieldException, AppException {
		System.out.println("Listar caminos de test tutor resource");

		return getService().listarCamino(idAlumno, idTarea);

	}
	// ############FIN DE INFORMES#####################################

	// ejercicio disponible
	/***
	 * Lista de ejercicios disponibles. Ejemplo
	 * **/

}
