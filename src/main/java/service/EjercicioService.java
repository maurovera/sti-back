package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Asignatura;
import model.Concepto;
import model.Curso;
import model.Ejercicio;
import model.Respuesta;
import model.Tarea;
import model.Tema;
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
	private SesionService sesionService;

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
	private TemaService temaService;

	@Inject
	private CursoService cursoService;

	@Inject
	private TareaService tareaService;

	@Inject
	private AlumnoService alumnoService;

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

	public String simulacion(HttpServletRequest httpRequest)
			throws AppException {
		Asignatura asig = new Asignatura();
		asig.setNombre("matematica");
		asig.setDescripcion("prueba de asignatua");
		asignaturaService.insertar(asig, httpRequest);

		System.out.println("idAsignatura: " + asig.getId());

		Tema tema01 = new Tema();
		Tema tema02 = new Tema();
		tema01.setNombre("operaciones01");
		tema01.setDescripcion("operaciones01");
		tema01.setPesoTema(new Double(0.5));
		tema01.setAsignatura(asig);
		temaService.insertar(tema01, httpRequest);

		tema02.setNombre("operaciones02");
		tema02.setDescripcion("operaciones02");
		tema02.setPesoTema(new Double(0.5));
		tema02.setAsignatura(asig);
		temaService.insertar(tema02, httpRequest);

		System.out.println("idtema01: " + tema01.getId());
		System.out.println("idtema02: " + tema02.getId());

		Concepto c1 = new Concepto();
		Concepto c2 = new Concepto();
		Concepto c3 = new Concepto();
		Concepto c4 = new Concepto();
		Concepto c5 = new Concepto();

		c1.setApriori(new Double(0.5));
		c1.setNombre("suma");
		c1.setIdAsignatura(asig.getId());
		c1.setPeso(new Double(0.2));
		c1.setTema(tema01);
		conceptoService.insertar(c1, httpRequest);

		c2.setApriori(new Double(0.5));
		c2.setNombre("resta");
		c2.setIdAsignatura(asig.getId());
		c2.setPeso(new Double(0.2));
		c2.setTema(tema01);
		conceptoService.insertar(c2, httpRequest);

		c3.setApriori(new Double(0.5));
		c3.setNombre("division");
		c3.setIdAsignatura(asig.getId());
		c3.setPeso(new Double(0.6));
		c3.setTema(tema01);
		conceptoService.insertar(c3, httpRequest);

		c4.setApriori(new Double(0.5));
		c4.setNombre("multiplicacion");
		c4.setIdAsignatura(asig.getId());
		c4.setPeso(new Double(0.5));
		c4.setTema(tema02);
		conceptoService.insertar(c4, httpRequest);

		c5.setApriori(new Double(0.5));
		c5.setNombre("potencia");
		c5.setIdAsignatura(asig.getId());
		c5.setPeso(new Double(0.5));
		c5.setTema(tema02);
		conceptoService.insertar(c5, httpRequest);

		System.out.println("idSuma: " + c1.getId());
		System.out.println("idResta: " + c2.getId());
		System.out.println("idDivision: " + c3.getId());
		System.out.println("idMultiplicacion: " + c4.getId());
		System.out.println("idPotencia: " + c5.getId());

		// ejercicio01
		Ejercicio e1 = new Ejercicio();
		e1.setAdivinanza(new Double(0.1));// bajo
		e1.setNivelDificultad(new Double(1));// facil
		e1.setEnunciado("ejercicio01");
		e1.setIdAsignatura(asig.getId());
		e1.addConceptos(c5);

		// Ejercicio02
		Ejercicio e2 = new Ejercicio();
		e2.setAdivinanza(new Double(0.1));// bajo
		e2.setNivelDificultad(new Double(1));// facil
		e2.setEnunciado("ejercicio02");
		e2.setIdAsignatura(asig.getId());
		e2.addConceptos(c4);

		// Ejercicio03
		Ejercicio e3 = new Ejercicio();
		e3.setAdivinanza(new Double(0.1));// medio
		e3.setNivelDificultad(new Double(1));// normal
		e3.setEnunciado("ejercicio03");
		e3.setIdAsignatura(asig.getId());
		e3.addConceptos(c3);

		// Ejercicio04
		Ejercicio e4 = new Ejercicio();
		e4.setAdivinanza(new Double(0.1));// medio
		e4.setNivelDificultad(new Double(1));// dificil
		e4.setEnunciado("ejercicio04");
		e4.setIdAsignatura(asig.getId());
		e4.addConceptos(c2);

		// Ejercicio05
		Ejercicio e5 = new Ejercicio();
		e5.setAdivinanza(new Double(0.1));// bajo
		e5.setNivelDificultad(new Double(1));// facil
		e5.setEnunciado("ejercicio05");
		e5.setIdAsignatura(asig.getId());
		e5.addConceptos(c1);

		// Ejercicio06
		Ejercicio e6 = new Ejercicio();
		e6.setAdivinanza(new Double(0.2));// medio
		e6.setNivelDificultad(new Double(1));// facil
		e6.setEnunciado("ejercicio06");
		e6.setIdAsignatura(asig.getId());
		e6.addConceptos(c1);
		e6.addConceptos(c2);

		// Ejercicio07
		Ejercicio e7 = new Ejercicio();
		e7.setAdivinanza(new Double(0.2));// medio
		e7.setNivelDificultad(new Double(2));// normal
		e7.setEnunciado("ejercicio07");
		e7.setIdAsignatura(asig.getId());
		e7.addConceptos(c1);
		e7.addConceptos(c2);
		e7.addConceptos(c3);

		// Ejercicio08
		Ejercicio e8 = new Ejercicio();
		e8.setAdivinanza(new Double(0.3));// alto
		e8.setNivelDificultad(new Double(2));// normal
		e8.setEnunciado("ejercicio08");
		e8.setIdAsignatura(asig.getId());
		e8.addConceptos(c1);
		e8.addConceptos(c2);
		e8.addConceptos(c3);
		e8.addConceptos(c4);

		// Ejercicio09
		Ejercicio e9 = new Ejercicio();
		e9.setAdivinanza(new Double(0.3));// alto
		e9.setNivelDificultad(new Double(3));// dificil
		e9.setEnunciado("ejercicio09");
		e9.setIdAsignatura(asig.getId());
		e9.addConceptos(c1);
		e9.addConceptos(c2);
		e9.addConceptos(c3);
		e9.addConceptos(c4);
		e9.addConceptos(c5);

		// Ejercicio10
		Ejercicio e10 = new Ejercicio();
		e10.setAdivinanza(new Double(0.2));// medio
		e10.setNivelDificultad(new Double(1));// facil
		e10.setEnunciado("ejercicio10");
		e10.setIdAsignatura(asig.getId());
		e10.addConceptos(c4);
		e10.addConceptos(c5);

		List<Ejercicio> listaEjercicio = new ArrayList<Ejercicio>();
		listaEjercicio.add(e1);
		listaEjercicio.add(e2);
		listaEjercicio.add(e3);
		listaEjercicio.add(e4);
		listaEjercicio.add(e5);
		listaEjercicio.add(e6);
		listaEjercicio.add(e7);
		listaEjercicio.add(e8);
		listaEjercicio.add(e9);
		listaEjercicio.add(e10);

		for (Ejercicio e : listaEjercicio) {
			Respuesta r1 = new Respuesta();
			Respuesta r2 = new Respuesta();
			Respuesta r3 = new Respuesta();
			Respuesta r4 = new Respuesta();

			r1.setFechaCreacion(new Date());
			r1.setUsuarioCreacion(userId);
			r1.setIpCreacion(httpRequest.getRemoteAddr());
			r1.setDescripcion("r1");
			e.addRespuesta(r1);

			r2.setFechaCreacion(new Date());
			r2.setUsuarioCreacion(userId);
			r2.setIpCreacion(httpRequest.getRemoteAddr());
			r2.setDescripcion("r2");
			e.addRespuesta(r2);

			r3.setFechaCreacion(new Date());
			r3.setUsuarioCreacion(userId);
			r3.setIpCreacion(httpRequest.getRemoteAddr());
			r3.setDescripcion("r3");
			e.addRespuesta(r3);

			r4.setFechaCreacion(new Date());
			r4.setUsuarioCreacion(userId);
			r4.setIpCreacion(httpRequest.getRemoteAddr());
			r4.setDescripcion("r4");
			e.addRespuesta(r4);

			e.setRespuesta(r1);
			e.setFechaCreacion(new Date());
			e.setUsuarioCreacion(userId);
			e.setIpCreacion(httpRequest.getRemoteAddr());

			// insertamos todo los ejercicios.
			getDao().insert(e);
		}

		System.out.println("Ejercicio 1: " + e1.getId());
		System.out.println("Ejercicio 2: " + e2.getId());
		System.out.println("Ejercicio 3: " + e3.getId());
		System.out.println("Ejercicio 4: " + e4.getId());
		System.out.println("Ejercicio 5: " + e5.getId());
		System.out.println("Ejercicio 6: " + e6.getId());
		System.out.println("Ejercicio 7: " + e7.getId());
		System.out.println("Ejercicio 8: " + e8.getId());
		System.out.println("Ejercicio 9: " + e9.getId());
		System.out.println("Ejercicio 10: " + e10.getId());

		// Curso
		Curso c = new Curso();
		c.setAlumno(new Long(1));
		c.setAsignatura(asig);
		c.setDescripcion("curso matematica");
		c.setNombre("matematica2018");

		// obtenemos el alumno
		Long idAlumno = new Long(1);
		Alumno al = alumnoService.obtener(idAlumno);
		c.agregarAlumno(al);
		cursoService.insertar(c, httpRequest);
		System.out.println("Curso id: " + c.getId());

		// Tareas
		Tarea tarea01 = new Tarea();
		tarea01.setCantidadEjercicioParada(10);
		tarea01.setCurso(c);
		tarea01.setDescripcion("Tarea01 de prueba");
		tarea01.setEstadoTarea(false);
		tarea01.setNombre("Tarea01");
		tarea01.addConceptos(c1);
		tarea01.addConceptos(c3);
		tarea01.addConceptos(c5);

		Tarea tarea02 = new Tarea();
		tarea02.setCantidadEjercicioParada(10);
		tarea02.setCurso(c);
		tarea02.setDescripcion("Tarea02 de prueba");
		tarea02.setEstadoTarea(false);
		tarea02.setNombre("Tarea02");
		tarea02.addConceptos(c2);
		tarea02.addConceptos(c4);

		// Falta agregar tareas. ya
		// falta registrarSesion
		// Falta falta llamar a siguienteEjercicio
		tareaService.insertar(tarea01, httpRequest);
		tareaService.insertar(tarea02, httpRequest);
		System.out.println("Tarea01 id: " + tarea01.getId());
		System.out.println("Tarea02 id: " + tarea01.getId());

		// inscribirse.
		// aqui ya no se persiste un carajo
		tema01.addConcepto(c1);
		tema01.addConcepto(c2);
		tema01.addConcepto(c3);
		tema02.addConcepto(c4);
		tema02.addConcepto(c5);

		asig.addTema(tema01);
		asig.addTema(tema02);

		System.out.println("lista de temas: " + asig.getListaTemas());

		adm.calcularProbabilidades(asig);
		adm.crearRedAlumno(asig.getId(), idAlumno);

		// registrarSesion
		// se registra en la tarea01
		List<String> listaResultado = new ArrayList<String>();
		String eje1 = new String();
		String eje2 = new String();
		String eje3 = new String();
		String eje4 = new String();
		String eje5 = new String();
		String rr1 = new String();
		String rr2 = new String();
		String rr3 = new String();
		String rr4 = new String();
		String rr5 = new String();
		
		Long idSesion = sesionService
				.registrarSesion(idAlumno, tarea01.getId());
		// se prueba con siguiente ejercicio
		// como es el primero. No existe ejercicio anterior. Por ello null
		
		
		/*****************PRIMER EJERCICIO**********************************/
		String siguienteEjercicio = null;
		siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea01, al, null,
				asig.getId(), "respuesta", asig);
		if (siguienteEjercicio == null)
			siguienteEjercicio = "No hay nada";

		System.out.println("Primer ejercicio: " + siguienteEjercicio);
		eje1 = new String("Primer ejercicio: " + siguienteEjercicio);
		listaResultado.add(eje1);
		// traigo el ejercicio y le tiro la respuesta
		String[] ejercicioString = siguienteEjercicio.split("#");
		Long idEjercicio = Long.valueOf(ejercicioString[0]);
		Ejercicio ejercicioNumero = dao.get(idEjercicio);

		// la primera respuesta es
		String respuesta = ejercicioNumero.getListaRespuesta().get(0)
				.getDescripcion();
		rr1 = new String("respuesta1: " + respuesta);
		listaResultado.add(rr1);
		
		if (respuesta == null) {
			System.out.println("no trajo respuesta");
		} else {
			System.out.println("trajo esta respuesta: " + respuesta);
		}
		
		
		
		// llama al siguiente ejercicio
		/***************SEGUNDO EJERCICIO*************************/
		siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea01, al,
				ejercicioNumero, asig.getId(), respuesta, asig);
		if (siguienteEjercicio == null)
			siguienteEjercicio = "No hay nada de siguiente ejercicio";

		System.out.println("Segundo  ejercicio: " + siguienteEjercicio);
		eje2 = "Segundo  ejercicio: " + siguienteEjercicio;
		listaResultado.add(eje2);
		
		
		// traigo el ejercicio y le tiro la respuesta
		ejercicioString = siguienteEjercicio.split("#");
		idEjercicio = Long.valueOf(ejercicioString[0]);
		ejercicioNumero = dao.get(idEjercicio);

		// la segunda respuesta del ejercicio 02
		respuesta = ejercicioNumero.getListaRespuesta().get(0).getDescripcion();
		if (respuesta == null) {
			System.out.println("no trajo respuesta");
		} else {
			System.out.println("trajo esta respuesta: " + respuesta);
		}
		rr2 = "respuesta2: " + respuesta;
		listaResultado.add(rr2);
		
		/**** LLAMA AL TERCER EJERCICIO ***************/
		siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea01, al,
				ejercicioNumero, asig.getId(), respuesta, asig);
		if (siguienteEjercicio == null)
			siguienteEjercicio = "No hay nada de siguiente ejercicio";

		System.out.println("Tercer  ejercicio: " + siguienteEjercicio);
		eje3 = "Tercer  ejercicio: " + siguienteEjercicio;
		listaResultado.add(eje3);
		
		
		ejercicioString = siguienteEjercicio.split("#");
		idEjercicio = Long.valueOf(ejercicioString[0]);
		ejercicioNumero = dao.get(idEjercicio);

		// la primera respuesta es
		respuesta = ejercicioNumero.getListaRespuesta().get(0)
				.getDescripcion();
		if (respuesta == null) {
			System.out.println("no trajo respuesta");
		} else {
			System.out.println("trajo esta respuesta: " + respuesta);
		}
		rr3 = "respuesta3: " + respuesta;
		listaResultado.add(rr3);
		
		
		
		/***LLAMA AL CUARTO EJERCICIO**/
		siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea01, al,
				ejercicioNumero, asig.getId(), respuesta, asig);
		if (siguienteEjercicio == null)
			siguienteEjercicio = "No hay nada de siguiente ejercicio";

		System.out.println("cuarto  ejercicio: " + siguienteEjercicio);
		eje4 = "cuarto  ejercicio: " + siguienteEjercicio;
		listaResultado.add(eje4);
		// cuarto
		ejercicioString = siguienteEjercicio.split("#");
		idEjercicio = Long.valueOf(ejercicioString[0]);
		ejercicioNumero = dao.get(idEjercicio);

		// la primera respuesta es
		respuesta = ejercicioNumero.getListaRespuesta().get(0)
				.getDescripcion();
		
		if (respuesta == null) {
			System.out.println("no trajo respuesta");
		} else {
			System.out.println("trajo esta respuesta: " + respuesta);
		}
		rr4 = "respuesta4: " + respuesta;
		listaResultado.add(rr4);
		
		
		/****LLAMA AL QUINTO EJERCICIO***/
		siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea01, al,
				ejercicioNumero, asig.getId(), respuesta, asig);
		if (siguienteEjercicio == null)
			siguienteEjercicio = "No hay nada de siguiente ejercicio";

		System.out.println("quinto  ejercicio: " + siguienteEjercicio);
		eje5 = "quinto  ejercicio: " + siguienteEjercicio;
		listaResultado.add(eje5);
		
		
		System.out.println("ResultadoFINAL");
		for (String resp : listaResultado) {
			System.out.println(resp);
			
			
		}
		
		
		
		return siguienteEjercicio;
	}

}
