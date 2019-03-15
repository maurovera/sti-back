package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Asignatura;
import model.Concepto;
import model.Ejercicio;
import model.Respuesta;
import model.Sesion;
import model.Tarea;
import utils.AppException;
import utils.EjercicioView;
import base.AdministracionAlumno;
import base.AdministracionBase;
import base.BaseServiceImpl;
import base.ListaResponse;
import dao.EjercicioDAO;

@Stateless
public class EjercicioService extends BaseServiceImpl<Ejercicio, EjercicioDAO> {

	@Inject
	private EjercicioDAO dao;

	@Inject
	AdministracionBase adm;

	@Inject
	AdministracionAlumno admAlumno;

	@Inject
	private ConceptoService conceptoService;

	// inject para cargar datos
	@Inject
	private AsignaturaService asignaturaService;

	@Inject
	private TareaService tareaService;

	@Inject
	private AlumnoService alumnoService;
	
	@Inject
	private SesionService sesionService;

	// private SessionService session;
	final private long userId = 1;

	@Override
	public EjercicioDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	/**
	 * @{@inheritDoc
	 */
	@Override
	public Ejercicio insertar(Ejercicio entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("implements de servicio Ejercicio");
			entity.setId(null);
			entity.setFechaCreacion(new Date());
			entity.setUsuarioCreacion(userId);
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			System.out.println("primero elemento de la lista de conceptos id:");
			System.out.println(entity.getListaConceptos().get(0).getId());

			/**
			 * Datos de concepto. Vuelvo a hacer esto porque necesito el nombre.
			 * /***Conceptos
			 **/
			entity.getListaConceptos().clear();
			for (String c : entity.getConceptosAsociados()) {
				Concepto datos = new Concepto();
				datos = conceptoService.obtener(Long.valueOf(c));
				entity.addConceptos(datos);
			}
			;

			// datos de respuesta
			for (Respuesta r : entity.getListaRespuesta()) {
				r.setId(null);
				r.setFechaCreacion(new Date());
				r.setUsuarioCreacion(userId);
				r.setIpCreacion(httpRequest.getRemoteAddr());
			}

			int respuestaNumero = Integer
					.valueOf(entity.getRespuestaCorrecta());
			System.out.println("respuesta Correcta: " + respuestaNumero);
			switch (respuestaNumero) {
			case 1:
				entity.setRespuesta(entity.getListaRespuesta().get(0));
				break;
			case 2:
				entity.setRespuesta(entity.getListaRespuesta().get(1));
				break;
			case 3:
				entity.setRespuesta(entity.getListaRespuesta().get(2));
				break;
			case 4:
				entity.setRespuesta(entity.getListaRespuesta().get(3));
				break;
			}

			System.out.println("creada lista respuesta");

			getDao().insert(entity);

			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/***
	 * insert simulado de ejercicio Utilizado solamente en simulacionService
	 **/
	public Ejercicio insertarSimulacion(Ejercicio entity,
			HttpServletRequest httpRequest) throws AppException {
		try {
			System.out.println("implements de servicio Ejercicio simulacion");

			getDao().insert(entity);

			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * @{@inheritDoc
	 */
	@Override
	public void modificar(Long id, Ejercicio entity,
			HttpServletRequest httpRequest) throws AppException {
		try {
			System.out.println("Modificar de ejercicio service");
			entity.setFechaModificacion(new Date());
			entity.setUsuarioModificacion(userId);
			entity.setIpModificacion(httpRequest.getRemoteAddr());

			/*** Conceptos **/
			entity.getListaConceptos().clear();
			for (String c : entity.getConceptosAsociados()) {
				Concepto datos = new Concepto();
				datos = conceptoService.obtener(Long.valueOf(c));
				entity.addConceptos(datos);
			}
			;

			/** Respuestas **/
			for (Respuesta r : entity.getListaRespuesta()) {
				r.setFechaModificacion(new Date());
				r.setUsuarioModificacion(userId);
				r.setIpModificacion(httpRequest.getRemoteAddr());
			}
			;

			int respuestaNumero = Integer
					.valueOf(entity.getRespuestaCorrecta());
			System.out.println("respuesta Correcta Modificada: "
					+ respuestaNumero);
			switch (respuestaNumero) {
			case 1:
				entity.setRespuesta(entity.getListaRespuesta().get(0));
				break;
			case 2:
				entity.setRespuesta(entity.getListaRespuesta().get(1));
				break;
			case 3:
				entity.setRespuesta(entity.getListaRespuesta().get(2));
				break;
			case 4:
				entity.setRespuesta(entity.getListaRespuesta().get(3));
				break;
			}

			getDao().modify(id, entity);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * Lista ejercicio service. Para el view del select en tarea detalle
	 **/
	public ListaResponse<EjercicioView> listarEjercicio() {
		ListaResponse<EjercicioView> res = getDao().listarEjercicio();
		return res;
	}

	/***
	 * Traer el siguiente Ejercicio correspondiente al test adaptativo
	 * 
	 * @param idTarea
	 *            : tarea al cual corresponde
	 * @param idAlumno
	 *            : alumno de la tarea
	 * @param idAsignatura
	 *            : asignatura al cual corresponde
	 * @param respuesta
	 *            : respuesta del alumno al ejercicio
	 * @return Ejercicio
	 **/
	public Ejercicio siguienteEjercicio(Long idTarea, Long idAlumno,
			Long idAsignatura, String respuesta, Long idEjercicioAnterior)
			throws AppException {
		try {
			String siguienteEjercicio = null;// en un comienzo te devuelve un
												// string
			Tarea tarea = tareaService.obtener(idTarea);
			Alumno alumno = alumnoService.obtener(idAlumno);
			Asignatura asig = asignaturaService.obtener(idAsignatura);
			/** Primer ejercicio **/
			Ejercicio ejercicioAnterior = null;
			if (idEjercicioAnterior != 0)
				ejercicioAnterior = dao.get(idEjercicioAnterior);

			/*** Se obtiene el siguiente ejercicio ***/
			siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea, alumno,
					ejercicioAnterior, idAsignatura, respuesta, asig);
			if (siguienteEjercicio == null)
				siguienteEjercicio = "No hay nada";

			System.out.println("#####################Ejercicio: "
					+ siguienteEjercicio);

			// traigo el ejercicio y le tiro la respuesta
			String[] ejercicioString = siguienteEjercicio.split("#");
			Long idEje = Long.valueOf(ejercicioString[0]);
			Ejercicio ejercicioNuevo = dao.get(idEje);

			return ejercicioNuevo;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/***
	 * Responde el ejercicio y devuelve la respuesta correspondiente
	 * 
	 * @param idEjercicio
	 *            : ejercicio que respondera
	 * @param idTarea
	 *            : tarea al cual corresponde
	 * @param idAlumno
	 *            : alumno de la tarea
	 * @param idAsignatura
	 *            : asignatura al cual corresponde
	 * @param respuesta
	 *            : respuesta del alumno al ejercicio
	 * @return respuestaEjercicio
	 **/
	public Boolean responderEjercicio(Long idTarea, Long idAlumno,
			Long idAsignatura, String respuesta, Long idEjercicio,
			HttpServletRequest httpRequest) throws AppException {
		try {
			Boolean retorno = null;

			retorno = admAlumno.responderEjercicioServicio(idEjercicio,
					respuesta, idAlumno, idAsignatura, idTarea, httpRequest);

			if (retorno == null)
				System.out
						.println("respondio mal y no se que paso. error interno en responder ejercicio servicio");

			return retorno;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	// #########Aplicacion de division de traer siguiente ejercicio sin
	// responder######
	/***
	 * Traer el siguiente Ejercicio correspondiente al test adaptativo Seria el
	 * primer test
	 * 
	 * @param idTarea
	 *            : tarea al cual corresponde
	 * @param idAlumno
	 *            : alumno de la tarea
	 * @param idAsignatura
	 *            : asignatura al cual corresponde
	 * @return Ejercicio
	 **/
	public Ejercicio siguienteEjercicioPrimerTest(Long idTarea, Long idAlumno,
			Long idAsignatura) throws AppException {
		try {
			Ejercicio siguienteEjercicio = null;

			// datos tarea, alumno, asi
			Tarea tarea = tareaService.obtener(idTarea);
			Alumno alumno = alumnoService.obtener(idAlumno);
			Asignatura asig = asignaturaService.obtener(idAsignatura);

			/*** Se obtiene el siguiente ejercicio ***/
			siguienteEjercicio = admAlumno.getSiguienteEjercicioPrimerTest(
					tarea, alumno, idAsignatura, asig);

			System.out.println("\n#####################Ejercicio: "
					+ siguienteEjercicio + " \n##############");

			return siguienteEjercicio;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/***
	 * Responde el ejercicio y devuelve la respuesta correspondiente
	 * 
	 * @param idEjercicio
	 *            : ejercicio que respondera
	 * @param idTarea
	 *            : tarea al cual corresponde
	 * @param idAlumno
	 *            : alumno de la tarea
	 * @param idAsignatura
	 *            : asignatura al cual corresponde
	 * @param respuesta
	 *            : respuesta del alumno al ejercicio
	 * @return respuestaEjercicio
	 **/
	public Boolean responderEjercicioPrimerTest(Long idTarea, Long idAlumno,
			Long idAsignatura, String respuesta, Long idEjercicio,
			HttpServletRequest httpRequest) throws AppException {
		try {
			Boolean retorno = null;

			retorno = admAlumno.responderEjercicioServicio(idEjercicio,
					respuesta, idAlumno, idAsignatura, idTarea, httpRequest);

			if (retorno == null)
				System.out
						.println("respondio mal y no se que paso. error interno en responder ejercicio servicio");

			return retorno;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**Retorna el criterio de parada que es la cantidad de ejercicios resueltos**/
	public Boolean criterio(Long idTarea, Long idAlumno,
			HttpServletRequest httpRequest) throws AppException {
		try {
			Boolean retorno = false;
			/**Obtenemos la cantidad de ejercicios por tarea
			 **/
			Tarea tarea = tareaService.obtener(idTarea);
			Integer cantidadParada = tarea.getCantidadEjercicioParada();
			/**Obtenemos la sesion anterior**/
			Sesion sesion = sesionService.sesionAnterior(idAlumno, idTarea);
			Integer resuelto = sesion.getcantidadEjerciciosResueltos();
			
			if(resuelto >= cantidadParada)
				retorno = true;
			
			if (retorno == null)
				System.out
						.println("respondio mal y no se que paso. error interno en responder ejercicio servicio");

			return retorno;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

}
