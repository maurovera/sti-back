package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Asignatura;
import model.Camino;
import model.Concepto;
import model.Ejercicio;
import model.Evidencia;
import model.Material;
import model.Respuesta;
import model.Sesion;
import model.SesionConcepto;
import model.Tarea;
import utils.AppException;
import utils.EjercicioView;
import utils.HerramientasDrools;
import utils.Regla;
import utils.RespuestaCriterio;
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

	@Inject
	private SesionConceptoService sesionConceptoService;

	@Inject
	private EvidenciaService evidenciaService;

	@Inject
	private DrlService drlService;

	@Inject
	private MaterialService materialService;

	@Inject
	private CaminoService caminoService;

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

	/** Retorna el criterio de parada que es la cantidad de ejercicios resueltos **/
	public Boolean criterio(Long idTarea, Long idAlumno,
			HttpServletRequest httpRequest) throws AppException {
		try {
			Boolean retorno = false;
			/**
			 * Obtenemos la cantidad de ejercicios por tarea
			 **/
			Tarea tarea = tareaService.obtener(idTarea);
			Integer cantidadParada = tarea.getCantidadEjercicioParada();
			/** Obtenemos la sesion anterior **/
			Sesion sesion = sesionService.sesionAnterior(idAlumno, idTarea);
			Integer resuelto = sesion.getcantidadEjerciciosResueltos();

			if (resuelto >= cantidadParada)
				retorno = true;

			if (retorno == null)
				System.out
						.println("respondio mal y no se que paso. error interno en responder ejercicio servicio");

			return retorno;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	// #########################Criterio
	// tutor#######################################
	/**
	 * Retorna el criterio de parada para el tutor o segundo examne donde se
	 * controla si tiene conceptos o si ya no puede seguir porque se acabo sus
	 * oportunidades
	 **/
	public RespuestaCriterio criterioTutor(Long idAsignatura, Long idTarea,
			Long idAlumno, HttpServletRequest httpRequest) throws AppException {

		RespuestaCriterio respuesta = new RespuestaCriterio();
		/**
		 * Lista de conceptos que no superan el margen requerido
		 **/
		List<Concepto> lista = listaAEvaluar(idTarea, idAsignatura, idAlumno);
		if (lista.isEmpty() || lista == null) {
			respuesta.setExitoso(true);
			respuesta
					.setMensaje("no existe conceptos a evaluar, todos aprobados");

			/** Lista con conceptos aun a evaluar **/
		} else {

			/**
			 * Obtenemos la cantidad de ejercicios por tarea
			 **/
			Tarea tarea = tareaService.obtener(idTarea);
			/** Obtenemos la sesion anterior y su lista se SesionConcepto **/
			Sesion sesion = sesionService.sesionAnterior(idAlumno, idTarea);
			List<SesionConcepto> listaSesionConcepto = sesion
					.getListaSesionConceptos();

			/**
			 * Cantidad de intentos por el alumno. Este no se usa porque es tipo
			 * la cantidad de intentos por sesion en total Ayuda a la primera
			 * vez. Es el cargador de sesionConceptos de la sesion misma por
			 * conceptos que no conoce el alumno por tarea por asignatura
			 **/
			Integer resuelto = sesion.getCantidadIntentos();
			System.out.println("cantidad de intentos : " + resuelto);
			if (resuelto == null || resuelto == 0) {
				System.out.println("entre en resueltos cero");
				/** cargamos los conceptos en sesion */
				Long conceptoUno = cargarConceptosEnSesion(sesion, lista,
						tarea, httpRequest);
				respuesta.setExitoso(false);
				respuesta
						.setMensaje("no  se intento ninguna vez,"
								+ " se cargan los conceptos, o los conceptos que faltan");
				respuesta.setConcepto(conceptoUno);
				/** si ya se cargaron todos entonces se controla **/
				/** La lista de conceptos a evaluar que es lista */

			} else {
				/** La lista de conceptos a evaluar que es lista */

				respuesta = comprobarIntentosYMargen(listaSesionConcepto);
			}

		}

		/**
		 * Aqui entra camino Se va a ir agregando camino solo por los conceptos
		 * que no se conocen Si respuesta es no exitosa. osea existen conceptos
		 * a evaluar
		 * **/
		if (respuesta.getConcepto() != null) {
			Long idCon = respuesta.getConcepto();
			Camino camino = caminoService.caminoAnterior(idAlumno, idTarea,
					idCon, idAsignatura, httpRequest);
			Boolean esEjercicio = camino.getEsEjercicio();
			respuesta.setEsEjercicio(esEjercicio);
			System.out.println("esEjercicio es: " + respuesta.getEsEjercicio());
		}

		return respuesta;

	}

	/**
	 * Comprueba si la lista sesionConcepto con sus conceptos ya no alcanzo el
	 * limite de intentos por concepto o el margen correspondiente. Para
	 * criterioTutor
	 **/
	private RespuestaCriterio comprobarIntentosYMargen(
			List<SesionConcepto> lista) {

		/**
		 * Ordenamos la lista a lo salvaje Siempre se ordena de menor a mayor
		 * con el id del objeto
		 **/
		Collections.sort(lista, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				// Aqui esta el truco, ahora comparamos p2 con p1 y no al reves
				// como antes
				SesionConcepto p1 = (SesionConcepto) o1;
				SesionConcepto p2 = (SesionConcepto) o2;
				return new Integer(p1.getId().intValue())
						.compareTo(new Integer(p2.getId().intValue()));
			}
		});

		RespuestaCriterio respuesta = new RespuestaCriterio();
		Boolean parar = true;
		for (SesionConcepto seCo : lista) {

			if (!seCo.getResuelto() && seCo.getIntentos() < seCo.getTotal()) {
				parar = false;
				respuesta.setConcepto(seCo.getIdConcepto());

			}
			/** si ya encontro el primero saltar del for **/
			if (!parar)
				break;
		}

		respuesta.setExitoso(parar);
		if (!parar)
			respuesta.setMensaje("aun no se resolvio y la cantidad de"
					+ " intentos por concepto aun no alcanzo el limite");
		else
			respuesta
					.setMensaje("Ya se resolvio o la cantidad de intentos de todos llego a su limite");

		return respuesta;

	}

	/**
	 * Carga los conceptos en sesionConcepto la primera vez y carga mas si
	 * encuentra otros que no estan Para criterioTutor
	 * **/
	private Long cargarConceptosEnSesion(Sesion sesion, List<Concepto> lista,
			Tarea tarea, HttpServletRequest httpRequest) throws AppException {

		List<SesionConcepto> sesionConcepto = sesion.getListaSesionConceptos();

		if (sesionConcepto == null || sesionConcepto.isEmpty()) {
			System.out.println("cargamos todo los conceptos");
			for (Concepto concepto : lista) {
				Long id = concepto.getId();
				SesionConcepto variable = new SesionConcepto();
				variable.setIdConcepto(id);
				variable.setIntentos(0);
				variable.setMargen(tarea.getMargenConocimiento());
				variable.setResuelto(false);
				variable.setSesion(sesion);
				variable.setTotal(tarea.getTotalIntentos());
				sesionConceptoService.insertar(variable, httpRequest);

			}
		} else {
			// cargamos las listas long
			List<Long> listaSesion1 = new ArrayList<Long>();
			for (SesionConcepto sesionConcepto2 : sesionConcepto)
				listaSesion1.add(sesionConcepto2.getIdConcepto());

			// cargamos las listas2 long
			List<Long> listaConcepto2 = new ArrayList<Long>();
			for (Concepto lc : lista)
				listaConcepto2.add(lc.getId());

			List<Long> cargador = new ArrayList<Long>();
			cargador = noContiene(listaSesion1, listaConcepto2);
			if (!cargador.isEmpty()) {
				for (Long idCargado : cargador) {
					SesionConcepto variable = new SesionConcepto();
					variable.setIdConcepto(idCargado);
					variable.setIntentos(0);
					variable.setMargen(tarea.getMargenConocimiento());
					variable.setResuelto(false);
					variable.setSesion(sesion);
					variable.setTotal(tarea.getTotalIntentos());
					sesionConceptoService.insertar(variable, httpRequest);
				}
			}
		}
		/** Se retorna el id del primer concepto de la lista **/
		return lista.get(0).getId();
	}

	/**
	 * compara dos lista y devuelve lo que no tiene es decir devuelve los
	 * conceptos que falta en la lista sesion Para cargarConceptoSesion
	 **/
	private List<Long> noContiene(List<Long> listaSesion, List<Long> conceptos) {
		List<Long> lista = new ArrayList<Long>();
		Boolean tiene = false;
		for (Long concepto : conceptos) {
			for (Long sesion : listaSesion) {
				if (concepto == sesion)
					tiene = true;
			}
			if (!tiene) {
				lista.add(concepto);
				tiene = false;
			}
		}
		return lista;
	}

	/**
	 * Devuelve una lista a evaluar si existe conceptos por debajo del criterio
	 * del profesor ParaRespuestaCriterio
	 **/
	private List<Concepto> listaAEvaluar(Long idTarea, Long idAsig, Long idAlu)
			throws AppException {

		/** Obtenemos la tarea **/
		Tarea tarea = new Tarea();
		tarea = tareaService.obtener(idTarea);
		if (tarea != null) {
			System.out.println("Conseguimos tarea");
		} else {
			System.out.println("No conseguimos la tarea");
		}

		/** Lista de conceptos de la tarea a tutorizar */
		List<Concepto> listaConcepto = new ArrayList<Concepto>();
		listaConcepto = tarea.getListaConceptosTarea();
		if (listaConcepto != null) {
			System.out
					.println("Traje la lista de conceptos de la tarea sin drama");
		} else {
			System.out.println("No tiene conceptos la tarea.");
		}

		String nombreConcepto = null;
		Double valorNodo = null;

		Double valorFijadoProfesor = tarea.getMargenConocimiento();

		List<Concepto> conceptosAEvaluar = new ArrayList<Concepto>();

		/**
		 * Se determina que conceptos no conoce Por el criterio de
		 * valorFijadoProfesor
		 **/
		for (Concepto c : listaConcepto) {
			nombreConcepto = c.getNombre();
			valorNodo = adm
					.getValorNodoRedDouble(nombreConcepto, idAsig, idAlu);
			if (valorNodo < valorFijadoProfesor) {
				conceptosAEvaluar.add(c);
				System.out.println("Entro Concepto:" + c.getNombre()
						+ ", valor: " + valorNodo);
			}

		}

		System.out.println("Conceptos a evaluar:\n");
		if (conceptosAEvaluar != null) {
			for (Concepto concepto : conceptosAEvaluar) {
				System.out.println("- " + concepto.getNombre());
			}

		}

		return conceptosAEvaluar;

	}

	// ############################FIN CRITERIOTUTOR############################
	// ######################SIGUIENTE EJERCICIO TUTOR#######################
	/*********************************************************************/
	/***
	 * siguiente ejercicio tutor
	 * 
	 * @throws AppException
	 **/
	public Ejercicio siguienteEjercicioTutor(Long idConcepto, Long idAsig,
			Long idAlu, Long idTarea) throws AppException {

		String nombreConcepto = null;
		Double valorNodo = null;

		Concepto c = conceptoService.obtener(idConcepto);
		/** Traemos el valor del nodo y su nombre **/
		nombreConcepto = c.getNombre();
		valorNodo = adm.getValorNodoRedDouble(nombreConcepto, idAsig, idAlu);

		/**
		 * Esta funcion trae el siguiente ejercicio en base a su utilidad sobre
		 * el concepto c y no sobre la asignatura
		 **/
		Ejercicio ejercicio = admAlumno.getSiguienteEjercicioPorConcepto(
				idTarea, idAlu, idAsig, c);

		if (ejercicio == null) {
			System.out.println("ejercicio nulo, avisar al profesor");
		} else {
			System.out.println("Ejercicio :" + ejercicio.toString()
					+ "\n#######");
		}

		return ejercicio;

	}

	// ################FIN SIGUIENTE TUTOR##########################

	// ###################RESPONDER TUTOR########################
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
	public Boolean responderEjercicioTutor(Long idTarea, Long idAlumno,
			Long idAsignatura, String respuesta, Long idEjercicio,
			Long idConcepto, HttpServletRequest httpRequest)
			throws AppException {
		try {
			Boolean retorno = null;

			retorno = admAlumno.responderEjercicioServicio(idEjercicio,
					respuesta, idAlumno, idAsignatura, idTarea, httpRequest);

			if (retorno == null) {
				System.out
						.println("respondio mal y no se que paso. error interno en responder ejercicio servicio");
			} else {
				/**
				 * Primer tema es cambiar la cantidad de intentos en sesion
				 * Queda pendiente estado terminado.
				 */
				Sesion sesionAnterior = sesionService.sesionAnterior(idAlumno,
						idTarea);
				Integer cantidad = sesionAnterior.getCantidadIntentos() + 1;
				sesionAnterior.setCantidadIntentos(cantidad);
				/**
				 * Traemos la lista de conceptos y actualizamos la cantidad de
				 * intentos, tambien traemos el camino.
				 * 
				 */
				Camino camino = caminoService.caminoAnterior(idAlumno, idTarea,
						idConcepto, idAsignatura, httpRequest);
				/** deberia controlar resuelto */
				SesionConcepto sesionConcepto = sesionAnterior
						.traerSesionConcepto(idConcepto);
				/** Se aumenta la cantidad de intentos */
				Integer intentosC = sesionConcepto.getIntentos() + 1;
				sesionConcepto.setIntentos(intentosC);
				/** vemos si alcanzo el margen */
				Concepto c = conceptoService.obtener(idConcepto);
				String nombreConcepto = c.getNombre();
				Double valorNodo = adm.getValorNodoRedDouble(nombreConcepto,
						idAsignatura, idAlumno);
				/** Si alcanzo el margen se setea resuelto **/
				if (valorNodo >= sesionConcepto.getMargen()) {

					System.out.println("alcanzo el margen: " + c.getNombre()
							+ " valor: " + valorNodo);
					sesionConcepto.setResuelto(true);
					camino.setParar(true);
					System.out.println("este es tu ultimo ejercicio. Alcanzo el margen requerido: "+ nombreConcepto);

				}
				/** Se setea parar en camino si alcanzo su cantidad de intentos **/
				if (intentosC >= sesionConcepto.getTotal()){
					camino.setParar(true);
					System.out.println("este es tu ultimo ejercicio. se acabaron las oportunidades"
							+ "para este concepto: "+ nombreConcepto);
				}
					

				/** Si respondio bien */
				if (retorno) {
					/**
					 * Actualizamos su nivelActual. mirar para no cagarla Esta
					 * bien porque es un ejercicio que Acerto en cambio si falla
					 * no se actualiza.
					 */
					camino.setNivelInicial(valorNodo);
					camino.setNivelEvidencia(valorNodo);
					/** seteamos los campos de camino si acierta el ejercicio **/
					String anterior = camino.getAnterior();
					
					camino.setAnterior(camino.getActual());
					camino.setActual("E");
					camino.setEsEjercicio(true);
					System.out.println("ejercicio: " + idEjercicio.toString());
					camino.setSecuenciaEjercicio(idEjercicio.toString());

					/**
					 * preguntamos si anterior es M, si es m se registra una
					 * evidencia
					 **/
					String mate = "M";
					if (anterior.equals(mate) ){
						System.out.println("registramos evidencia");
						registrarEvidencia(camino, httpRequest);
					}
						

					/** Si respondio mal **/
				} else {
					camino.setAnterior(camino.getActual());
					camino.setActual("M");
					camino.setEsEjercicio(false);
					camino.setSecuenciaEjercicio(idEjercicio.toString());
				}

				/**
				 * Actualizamos su sesionConcepto y camino
				 */
				sesionConceptoService.modificar(sesionConcepto.getId(),
						sesionConcepto, httpRequest);
				caminoService.modificar(camino.getId(), camino, httpRequest);

			}

			return retorno;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * Registra una evidencia y reinicia la evidencia por llamarlo asi
	 * 
	 * @Param evidencia
	 * @Param http
	 * **/
	private void registrarEvidencia(Camino c, HttpServletRequest httpRequest)
			throws AppException {

		/**Entre en registro evidencia*/
		System.out.println("entre en registrarEvidencia a ver si guarda");
		c.cargarMaterialYEjercicio();
		Evidencia e01 = new Evidencia(c);
		e01.formatearEvidencia();
		evidenciaService.insertar(e01, httpRequest);

	}

	// #####################FIN RESPONDER TUTOR##################

	// ##########################SIGUIENTE MATERIAL##################
	/***
	 * siguiente material
	 * 
	 * @throws AppException
	 **/
	public Material siguienteMaterial(Long idArchivo, Long idAlu, Long idAsig,
			Long idConcepto, Long idTarea, HttpServletRequest httpRequest)
			throws AppException {

		/** Aqui inicia el motor drools. Esto no deberia estar aqui. **/
		String archivo = drlService.obtenerArchivo(idArchivo);
		HerramientasDrools hd = drlService.iniciarDrools(archivo);
		/**
		 * Trae un camino anterior o un camino nuevo para busqueda de materiales
		 **/
		Camino camino = caminoService.caminoAnterior(idAlu, idTarea,
				idConcepto, idAsig, httpRequest);

		// material a devolver
		Material material = new Material();

		material = aplicarReglaMaterial(camino, hd, idTarea, idAlu);
		if (material == null)
			System.out.println("salte porque no existe material disponible");

		return material;

	}

	/**
	 * Obtiene un material si existe una regla por la evidencia del alumno en
	 * caso de no obtener un material por regla. obtiene un material aleatorio
	 * basado en concepto, nivel y estilo que aun no se haya visto el alumno
	 * Para siguienteMaterial
	 * 
	 * @Param evidencia e
	 * @Param hd. Instancia del drools
	 * @Return {@link Class} Material
	 ***/
	private Material aplicarReglaMaterial(Camino camino, HerramientasDrools hd,
			Long idTarea, Long idAlu) throws AppException {

		Material material = new Material();

		/**
		 * Copiamos la evidencia en otro y formateamos Le paso un uno para que
		 * copie tambien los array de la clase
		 **/
		camino.cargarMaterialYEjercicio();
		Evidencia evide = new Evidencia(camino);
		evide.formatearEvidenciaParaRegla();

		// iniciamos sesion y le tiramos el material
		hd.iniciarSession();
		Regla r = new Regla();
		r.setConcepto(evide.getConcepto());
		r.setNivel(evide.getNivel());
		r.setEstilo(evide.getEstilo());
		r.setSecuenciaEjercicios(evide.getSecuenciaEjercicio());
		r.setSecuenciaVideos(evide.getSecuenciaMaterial());

		System.out.println("######################### Concepto: "
				+ evide.getConcepto());
		System.out.println("######################### nivel: "
				+ evide.getNivel());
		System.out.println("######################### estilo: "
				+ evide.getEstilo());
		System.out.println("######################### secuenciaEjercicio: "
				+ evide.getSecuenciaEjercicio());
		System.out.println("######################### secuenciaMaterial: "
				+ evide.getSecuenciaMaterial());

		hd.ejecutarRegla(r);
		hd.terminarSession();
		System.out.println("######################### material a mostrar: "
				+ r.getResultado());

		/** Aqui abrimos la sesion anterior **/
		Sesion sesionAnterior = sesionService.sesionAnterior(idAlu, idTarea);

		/**
		 * Si consigo un material entra aqui. - Si el material ya se mostro. Que
		 * se hace??????????????? se busca otra regla. o se asume que funciona y
		 * se guarda en materiales mostrados - Ahora se muestra el material que
		 * genera la regla por mas que ya se haya mostrado Falta =controlar que
		 * no repita ya el material guardado ya sea regla o al azar.
		 * ***/
		if (r.getResultado() != null) {
			String[] parts = r.getResultado().split("M");
			String part2 = parts[1]; // 654321
			Long idMaterial = new Long(part2);
			material = materialService.obtener(idMaterial);
			System.out.println("#####################################");
			System.out.println("Material de la regla: M" + material.getId());
			System.out.println("#####################################\n");
			material.setEsRegla(true);
			// noEsRegla = true;
			/***
			 * Si la regla no consiguio material entonces se busca un material
			 * que no haya sido seleccionado aun es decir que no este en la
			 * sesion anterior
			 ***/
		} else {

			List<Material> materiales = sesionAnterior.getListaMaterial();
			/**
			 * Atencion. Aqui suele fallar. y trae material nulo. Cuando trae
			 * material nulo es cuando falla. OJOOOO REVISAR
			 **/
			material = materialService.materialesDisponibles(materiales, r);
			if (material == null) {
				System.out.println("ya no tengo material disponible");
				System.out.println("##############################");

			} else {
				System.out.println("#####################################");
				System.out.println("Material al azar: M" + material.getId());
				System.out.println("#####################################\n");
				material.setEsRegla(false);
			}

		}

		return material;
	}

	/**
	 * tipo aqui hay que ver que onda si es material o ejercicio el criterio de
	 * parada es ver si lleno el cupo nada mas pero aqui se tiene que ver - si
	 * es material o no - manejar las evidencias - ver en que momento guardar la
	 * evidencia y recuperarlo. para siguienteMaterial
	 ***/
	public void materialOEjercicio() {

		/** Arranca el motor de reglas. Va a obtener el archivo 1 **/
		// String archivo = drlService.obtenerArchivo(idArchivo);
		// HerramientasDrools hd = drlService.iniciarDrools(archivo);

	}

	// ##############################FIN SIGUIENTE MATERIAL#############

	// ###################RESPONDER MATERIAL########################
	/***
	 * Responde el Material visto
	 **/
	public Boolean responderMaterial(Long idTarea, Long idAlumno, Long idAsignatura,
			Long idConcepto, Long idMaterial, HttpServletRequest httpRequest)
			throws AppException {
		try {
			Boolean retorno = true;

			/** Traemos la sesion */
			Sesion sesionAnterior = sesionService.sesionAnterior(idAlumno,
					idTarea);
			/** Traemos el material **/
			Material material = materialService.obtener(idMaterial);

			/** Solo si el material es no nulo se inserta **/
			if (material != null) {
				/**
				 * Una vez mostrado el material por la regla o al azar se guarda
				 * en la sesionMaterialAnterior
				 */
				try {

					/** Insertamos un material a la sesion */
					sesionService.insertarMaterialVisto(sesionAnterior.getId(),
							sesionAnterior, material);
				} catch (AppException ex) {
					System.out.println("No se pudo insertar el material");
					ex.printStackTrace();
				}

			}
			/**
			 * Traemos el camino y actualizamos
			 */
			Camino camino = caminoService.caminoAnterior(idAlumno, idTarea,
					idConcepto, idAsignatura, httpRequest);
			/** trae el concepto para quitar su nombre y su nivel */
			Concepto c = conceptoService.obtener(idConcepto);
			String nombreConcepto = c.getNombre();
			Double valorNodo = adm.getValorNodoRedDouble(nombreConcepto,
					idAsignatura, idAlumno);

			/** Se actualiza su nivel */
			camino.setNivelInicial(valorNodo);
			camino.setNivelEvidencia(valorNodo);
			camino.setAnterior(camino.getActual());
			camino.setActual("E");
			camino.setEsEjercicio(true);
			camino.setSecuenciaMaterial(idMaterial.toString());

			caminoService.modificar(camino.getId(), camino, httpRequest);

			return retorno;

		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	// #####################FIN RESPONDER MATERIAL##################

}
