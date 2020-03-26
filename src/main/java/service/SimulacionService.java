package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Asignatura;
import model.Camino;
import model.Concepto;
import model.Curso;
import model.Ejercicio;
import model.EstiloAprendizaje;
import model.Evidencia;
import model.Log;
import model.Material;
import model.Respuesta;
import model.Resuelto;
import model.Sesion;
import model.Simulacion;
import model.Tarea;
import model.Tema;

import org.apache.shiro.crypto.hash.Md5Hash;

import seguridad.Usuario;
import seguridad.UsuarioController;
import utils.AppException;
import utils.HerramientasDrools;
import utils.Regla;
import base.AdministracionAlumno;
import base.AdministracionBase;
import base.BaseServiceImpl;
import dao.SimulacionDAO;

@Stateless
public class SimulacionService extends
		BaseServiceImpl<Simulacion, SimulacionDAO> {

	// final String direccionArchivo =
	// "/home/mauro/proyectos/tesis/sti-back/src/main/resources/archivos/";
	final String direccionArchivo = "/home/catherine/tesis/sti-back/src/main/resources/archivos/";

	@Inject
	private SimulacionDAO dao;

	@Inject
	private EjercicioService ejercicioService;

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

	@Inject
	private UsuarioController usuarioService;

	@Inject
	private EvidenciaService evidenciaService;

	@Inject
	private LogService logService;

	@Inject
	private DrlService drlService;

	@Inject
	private MaterialService materialService;

	@Inject
	private EstiloAprendizajeService estiloService;

	// private SessionService session;
	final private long userId = 1;

	@Override
	public SimulacionDAO getDao() {

		return dao;
	}

	public String simulacionAsignaturaCompleta(HttpServletRequest httpRequest)
			throws AppException {

		// Creacion de Asignatura
		Asignatura asig = crearAsignatura(httpRequest);
		// una sola vez se calcula la probabilidad quue serian los pesos
		// asignados
		adm.calcularProbabilidades(asig);
		// Creacion de curso asociado al alumno y la tarea y los conceptos
		List<Concepto> conceptos01 = new ArrayList<Concepto>();
		List<Concepto> conceptos02 = new ArrayList<Concepto>();

		Concepto c1 = asig.getListaTemas().get(0).getListaConceptos().get(0);
		Concepto c2 = asig.getListaTemas().get(0).getListaConceptos().get(1);
		Concepto c3 = asig.getListaTemas().get(0).getListaConceptos().get(2);
		Concepto c4 = asig.getListaTemas().get(1).getListaConceptos().get(0);
		Concepto c5 = asig.getListaTemas().get(1).getListaConceptos().get(1);

		conceptos01.add(c1);
		conceptos01.add(c3);
		conceptos01.add(c5);

		conceptos02.add(c2);
		conceptos02.add(c4);

		Curso c = CrearCurso(httpRequest, asig, conceptos01, conceptos02);

		// creacion de varios alumnos inscritos al curso C
		List<Alumno> listaAlumnos = new ArrayList<Alumno>();
		listaAlumnos = generacionAlumnos(httpRequest, asig, c);
		System.out.println("##########alumnos creados######"
				+ listaAlumnos.size());

		// La tarea a hacerse
		List<Tarea> listaTareas = cursoService.listaTarea(c.getId());
		Tarea tareaAResolver = listaTareas.get(0);
		System.out.println("Tarea A Resolver : " + tareaAResolver.getNombre());

		return "asignaturaCompleta";

	}

	/**
	 * Simulacion de carga de alumnos y sin que hagan la primera prueba carga el
	 * rango de alumno todos al curso uno pero sin hacer la prueba uno y tambien
	 * se inscriben a la tarea 1 Modificacion de fecha 11/09/2019 Aqui se cambia
	 * la asignatura y tambien el curso y tambien la tarea.
	 * 
	 ***/
	public String simulacionAlumnosSinTest(HttpServletRequest httpRequest,
			Integer inicio, Integer fin) throws AppException {

		Long numero = new Long(2);
		Asignatura asig = asignaturaService.obtener(numero);
		Curso c = cursoService.obtener(numero);
		// creacion de varios alumnos inscritos al curso C
		List<Alumno> listaAlumnos = new ArrayList<Alumno>();
		listaAlumnos = generadorAlumnosPrueba(httpRequest, asig, c, inicio, fin);
		// listaAlumnos = generacion, asignatura, curso)(httpRequest, asig, c);
		System.out.println("##########alumnos creados######\n" + "Desde: "
				+ inicio + " Hasta: " + fin);
		String retorno = "##########alumnos creados######\n" + "Desde: "
				+ inicio + " Hasta: " + fin;

		return retorno;

	}

	/**
	 * Simulacion de carga de alumnos y que hagan la primera prueba
	 ***/
	public String simulacionAlumnosPrueba(HttpServletRequest httpRequest,
			Long idAsig, Integer inicio, Integer fin) throws AppException {

		// Long numero = new Long(1);
		Long numero = idAsig;
		Asignatura asig = asignaturaService.obtener(numero);
		Curso c = cursoService.obtener(numero);
		// creacion de varios alumnos inscritos al curso C
		List<Alumno> listaAlumnos = new ArrayList<Alumno>();
		listaAlumnos = generadorAlumnosPrueba(httpRequest, asig, c, inicio, fin);
		// listaAlumnos = generacion, asignatura, curso)(httpRequest, asig, c);
		System.out.println("##########alumnos creados######\n" + "Desde: "
				+ inicio + " Hasta: " + fin);
		String retorno = "##########alumnos creados######\n" + "Desde: "
				+ inicio + " Hasta: " + fin;
		// La tarea a hacerse
		List<Tarea> listaTareas = cursoService.listaTarea(c.getId());
		Tarea tareaAResolver = listaTareas.get(0);
		System.out.println("Tarea A Resolver : " + tareaAResolver.getNombre());

		int cont = inicio;

		for (Alumno alumno : listaAlumnos) {
			simularAlumno(alumno, tareaAResolver, asig, httpRequest);
			++cont;
			System.out.println("---------------alumno simulado nro: " + cont
					+ ".....Nombre: " + alumno.getNombres()
					+ "----------------------");
		}

		return retorno;

	}

	public String simulacion(HttpServletRequest httpRequest)
			throws AppException {

		// Creacion de Asignatura
		Asignatura asig = crearAsignatura(httpRequest);
		// una sola vez se calcula la probabilidad quue serian los pesos
		// asignados
		adm.calcularProbabilidades(asig);
		// Creacion de curso asociado al alumno y la tarea y los conceptos
		List<Concepto> conceptos01 = new ArrayList<Concepto>();
		List<Concepto> conceptos02 = new ArrayList<Concepto>();

		Concepto c1 = asig.getListaTemas().get(0).getListaConceptos().get(0);
		Concepto c2 = asig.getListaTemas().get(0).getListaConceptos().get(1);
		Concepto c3 = asig.getListaTemas().get(0).getListaConceptos().get(2);
		Concepto c4 = asig.getListaTemas().get(1).getListaConceptos().get(0);
		Concepto c5 = asig.getListaTemas().get(1).getListaConceptos().get(1);

		conceptos01.add(c1);
		conceptos01.add(c3);
		conceptos01.add(c5);

		conceptos02.add(c2);
		conceptos02.add(c4);

		Curso c = CrearCurso(httpRequest, asig, conceptos01, conceptos02);

		// creacion de varios alumnos inscritos al curso C
		List<Alumno> listaAlumnos = new ArrayList<Alumno>();
		listaAlumnos = generacionAlumnos(httpRequest, asig, c);
		System.out.println("##########alumnos creados######"
				+ listaAlumnos.size());

		// La tarea a hacerse
		List<Tarea> listaTareas = cursoService.listaTarea(c.getId());
		Tarea tareaAResolver = listaTareas.get(0);
		System.out.println("Tarea A Resolver : " + tareaAResolver.getNombre());

		int cont = 0;

		for (Alumno alumno : listaAlumnos) {
			simularAlumno(alumno, tareaAResolver, asig, httpRequest);
			++cont;
			System.out.println("---------------alumno simulado nro: " + cont
					+ ".....Nombre: " + alumno.getNombres()
					+ "----------------------");
		}

		return "termine";

	}

	/**
	 * Crea una asignatura con su red bayes
	 ***/
	private Asignatura crearAsignatura(HttpServletRequest httpRequest)
			throws AppException {

		Asignatura asig = new Asignatura();
		asig.setNombre("Asignatura_Materiales");
		asig.setDescripcion("prueba de materiales");
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

		List<Concepto> listaConc = new ArrayList<Concepto>();
		listaConc.add(c1);
		listaConc.add(c2);
		listaConc.add(c3);
		listaConc.add(c4);
		listaConc.add(c5);

		List<Ejercicio> listaEjercicio = new ArrayList<Ejercicio>();
		List<Ejercicio> listaEje = new ArrayList<Ejercicio>();
		for (int i = 0; i < 5; i++) {
			listaEje = creadorCombinadoDeEjercicios(5, listaConc, asig.getId(),
					i);
			if (!listaEje.isEmpty()) {
				listaEjercicio.addAll(listaEje);
			}

		}
		// listaEjercicio = creadorCombinadoDeEjercicios(5, listaConc,
		// asig.getId(), 5);

		if (listaEjercicio.isEmpty())
			System.out.println(" NO CREO NINGUN EJERCICIO");

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
			ejercicioService.insertarSimulacion(e, httpRequest);
		}

		/** Creacion de asignatura **/

		// aqui ya no se persiste un carajo
		tema01.addConcepto(c1);
		tema01.addConcepto(c2);
		tema01.addConcepto(c3);
		tema02.addConcepto(c4);
		tema02.addConcepto(c5);

		asig.addTema(tema01);
		asig.addTema(tema02);
		/** crea la red de la asignatura ***/
		adm.calcularProbabilidades(asig);

		crearMateriales(httpRequest, asig.getId());

		return asig;

	}

	/***
	 * Esta funcion devuelve un lista de ejercicios que estan definidos por una
	 * escala como la que sigue. cantidadConcepto | adivinanza | dificultad 1 |
	 * bajo | facil 2 | medio | facil 3 | medio | normal 4 | alto | normal 5 |
	 * alto | dificil
	 * 
	 * @param num
	 *            . Es el numero de concepto para hacer la combinacion
	 * @param conceptos
	 *            . Es la lista de conceptos
	 * @param idAsignatura
	 *            . id de la asignatura
	 * @param numeroDelFor
	 *            . un for externo por si se quiera hacer varias veces.
	 * 
	 * @return listaDeEjercicios
	 * ***/
	public List<Ejercicio> creadorCombinadoDeEjercicios(int num,
			List<Concepto> conceptos, Long idAsignatura, int numeroDelFor) {

		List<Ejercicio> listaRetorno = new ArrayList<Ejercicio>();
		Double bajoAdivinanza = new Double(0.1);
		Double medioAdivinanza = new Double(0.2);
		Double altoAdivinanza = new Double(0.3);

		Double facilDificultad = new Double(1);
		Double normalDificultad = new Double(2);
		Double dificilDificultad = new Double(3);

		for (int i = 0; i < Math.pow(2, num); i++) {
			int arreglo[] = new int[num];
			int temp = i;
			for (int l = 0; l < arreglo.length; l++) {
				arreglo[l] = temp % 2;
				temp /= 2;
			}
			String res = "";
			// posicion dentro del arraydeConceptos
			List<Integer> posicion = new ArrayList<Integer>();
			for (int j = 0; j < arreglo.length; j++) {
				if (arreglo[j] == 1) {
					res += "[" + (j + 1) + "]";
					posicion.add(j);
				}
			}

			int combinacion = i + 1;
			int cantidadCon = posicion.size();

			// un concepto
			if (cantidadCon == 1) {
				int secuencia = combinacion + (numeroDelFor * 32);
				Ejercicio e = cargaUnEjercicio(posicion, conceptos, secuencia,
						bajoAdivinanza, facilDificultad, idAsignatura);
				listaRetorno.add(e);

			}
			// dos conceptos
			if (cantidadCon == 2) {

				int secuencia = combinacion + (numeroDelFor * 32);
				Ejercicio e = cargaUnEjercicio(posicion, conceptos, secuencia,
						medioAdivinanza, facilDificultad, idAsignatura);
				listaRetorno.add(e);

			}
			// tres conceptos
			if (cantidadCon == 3) {

				int secuencia = combinacion + (numeroDelFor * 32);
				Ejercicio e = cargaUnEjercicio(posicion, conceptos, secuencia,
						medioAdivinanza, normalDificultad, idAsignatura);
				listaRetorno.add(e);

			}
			// cuatro conceptos
			if (cantidadCon == 4) {

				int secuencia = combinacion + (numeroDelFor * 32);
				Ejercicio e = cargaUnEjercicio(posicion, conceptos, secuencia,
						altoAdivinanza, normalDificultad, idAsignatura);
				listaRetorno.add(e);

			}
			// cinco conceptos
			if (cantidadCon == 5) {

				int secuencia = combinacion + (numeroDelFor * 32);
				Ejercicio e = cargaUnEjercicio(posicion, conceptos, secuencia,
						altoAdivinanza, dificilDificultad, idAsignatura);
				listaRetorno.add(e);

			}

		}

		return listaRetorno;

	}

	/** Carga un ejercicio ***/
	private Ejercicio cargaUnEjercicio(List<Integer> posicion,
			List<Concepto> lista, int secuencia, Double adivinanza,
			Double dificultad, Long idAsignatura) {

		// ejercicio
		Ejercicio e = new Ejercicio();
		e.setAdivinanza(adivinanza);// bajo
		e.setNivelDificultad(dificultad);// facil
		e.setEnunciado("ejercicio" + secuencia);
		e.setIdAsignatura(idAsignatura);
		for (Integer ppp : posicion) {
			// System.out.println("Concepto: " + lista.get(ppp));
			e.addConceptos(lista.get(ppp));
		}

		return e;

	}

	/**
	 * Crea 54 materiales para la lista de conceptos ahi adentro.
	 **/
	private void crearMateriales(HttpServletRequest httpRequest, Long asig)
			throws AppException {

		/**
		 * Creacion de material ** 12 por cada concepto
		 **/
		List<String> lista = new ArrayList<String>();
		lista.add("suma");
		lista.add("division");
		lista.add("potencia");

		for (String c : lista) {

			for (int i = 0; i < 54; i++) {
				if (i < 18) {
					String nivel = "bajo";
					String estilo = "visual";
					String url = "https://www.youtube.com/watch?v=-4pe1ui" + i
							+ "b" + i + "g";
					crearMaterial(httpRequest, nivel, estilo, c, asig, url);
				} else if (i >= 18 && i <= 36) {
					String nivel = "medio";
					String estilo = "visual";
					String url = "https://www.youtube.com/watch?v=-4pe1ui" + i
							+ "b" + i + "g";
					crearMaterial(httpRequest, nivel, estilo, c, asig, url);

				} else if (i > 36) {
					String nivel = "alto";
					String estilo = "visual";
					String url = "https://www.youtube.com/watch?v=-4pe1ui" + i
							+ "b" + i + "g";
					crearMaterial(httpRequest, nivel, estilo, c, asig, url);
				}

			}
		}

	}

	private void crearMaterial(HttpServletRequest httpRequest, String nivel,
			String estilo, String c, Long asig, String url) throws AppException {
		Material material = new Material();
		material.setConcepto(c);
		material.setEstilo(estilo);
		material.setIdAsignatura(asig);
		material.setNivel(nivel);
		material.setUrlMaterial(url);

		materialService.insertar(material, httpRequest);

	}

	/** Crea un curso con tareas asociadas ***/
	private Curso CrearCurso(HttpServletRequest httpRequest, Asignatura asig,
			List<Concepto> conceptos01, List<Concepto> conceptos02)
			throws AppException {

		Curso c = new Curso();

		c.setAsignatura(asig);
		c.setDescripcion("curso matematica");
		c.setNombre("matematica2018");

		cursoService.insertar(c, httpRequest);
		System.out.println("Curso id: " + c.getId());

		// Tareas
		Tarea tarea01 = new Tarea();
		tarea01.setCantidadEjercicioParada(5);
		tarea01.setCurso(c);
		tarea01.setMargenConocimiento(new Double(0.6));
		tarea01.setTotalIntentos(5);
		tarea01.setDescripcion("Tarea01 de prueba");
		tarea01.setEstadoTarea(false);
		tarea01.setNombre("Tarea01");
		// c1,c3,c5
		for (Concepto cc : conceptos01)
			tarea01.addConceptos(cc);

		Tarea tarea02 = new Tarea();
		tarea02.setCantidadEjercicioParada(5);
		tarea02.setCurso(c);
		tarea02.setMargenConocimiento(new Double(0.6));
		tarea02.setTotalIntentos(5);
		tarea02.setDescripcion("Tarea02 de prueba");
		tarea02.setEstadoTarea(false);
		tarea02.setNombre("Tarea02");
		// c2,c4
		for (Concepto cc : conceptos02)
			tarea02.addConceptos(cc);

		tareaService.insertar(tarea01, httpRequest);
		tareaService.insertar(tarea02, httpRequest);
		System.out.println("Tarea01 id: " + tarea01.getId());
		System.out.println("Tarea02 id: " + tarea02.getId());

		return c;
	}

	/**
	 * crea alumno con estilo
	 **/
	private Alumno crearAlumnoConEstilo(HttpServletRequest httpRequest,
			Asignatura asig, Curso c, Integer secuencia, Double tipo,
			List<String> estilos) throws AppException {

		// creacion del alumno
		Alumno alu = new Alumno();
		alu.setApellidos("apellidos_" + secuencia.toString());
		alu.setEdad(secuencia);
		alu.setFechaNacimiento(new Date());
		alu.setGenero("m");
		alu.setNombres("nombres_" + secuencia.toString());
		alu.setTipo(tipo);
		alu.setEdad(secuencia);
		alu.setEmail("correo" + secuencia.toString() + "@gmail.com");
		alu.setRecibirNotificacion(true);
		alu.setCedula("4250090" + secuencia.toString());
		alu.setUsername("marzouser" + secuencia.toString());
		alu.setPassword("marzo");
		alu.setInterno(true);
		alu.setPublico(false);

		alumnoService.insertar(alu, httpRequest);
		Long idAlu = alu.getId();
		// insertamos su estilo de aprendizaje
		EstiloAprendizaje ea = crearEstiloAprendizaje(estilos, httpRequest,
				idAlu);

		// Inscripcion al curso por parte del alumno
		c.agregarAlumno(alu);
		cursoService.modificar(c.getId(), c, httpRequest);

		// Creacion de la red bayesiana para el alumno
		System.out.println("alumno:" + alu.getId());
		adm.crearRedAlumno(asig.getId(), alu.getId());

		return alu;

	}

	/**
	 * Se crea un alumno, se inscribe al curso y se crea su red bayesiana
	 **/
	private Alumno crearAlumno(HttpServletRequest httpRequest, Asignatura asig,
			Curso c, Integer secuencia, Double tipo) throws AppException {

		// creacion del alumno
		Alumno alu = new Alumno();
		alu.setApellidos("apellidos_" + secuencia.toString());
		alu.setEdad(secuencia);
		alu.setFechaNacimiento(new Date());
		alu.setGenero("m");
		alu.setNombres("nombres_" + secuencia.toString());
		alu.setTipo(tipo);
		alu.setEdad(secuencia);
		alu.setEmail("correo" + secuencia.toString() + "@gmail.com");
		alu.setRecibirNotificacion(true);
		alu.setCedula("4250090" + secuencia.toString());
		alu.setUsername("marzouser" + secuencia.toString());
		alu.setPassword("marzo");
		alu.setInterno(true);
		alu.setPublico(false);

		alumnoService.insertar(alu, httpRequest);

		// Inscripcion al curso por parte del alumno
		c.agregarAlumno(alu);
		cursoService.modificar(c.getId(), c, httpRequest);

		// Creacion de la red bayesiana para el alumno
		System.out.println("alumno:" + alu.getId());
		adm.crearRedAlumno(asig.getId(), alu.getId());

		return alu;

	}

	/**
	 * Se genera una cantidad de alumnos aleatoriamente Estos alumnos estaran
	 * inscritos en el curso c y tendran su red bayesiana
	 * 
	 * @throws AppException
	 **/
	public List<Alumno> generacionAlumnos(HttpServletRequest httpRequest,
			Asignatura asignatura, Curso curso) throws AppException {

		int cantidadAlumnos = 12;
		Double tipo;
		List<Alumno> alumnos = new ArrayList<Alumno>();

		for (int i = 0; i < cantidadAlumnos; i++) {

			if (i <= 3) {
				tipo = TipoAlumno.NIVEL_CONOCIMIENTO_BAJO;
			} else if (i > 3 && i <= 7) {
				tipo = TipoAlumno.NIVEL_CONOCIMIENTO_MEDIO;
			} else {
				tipo = TipoAlumno.NIVEL_CONOCIMIENTO_ALTO;
			}
			Alumno alumno = crearAlumno(httpRequest, asignatura, curso, i, tipo);

			alumnos.add(alumno);
		}

		return alumnos;
	}

	/**
	 * Generador de alumnos para un curso x Cambiamos. por estilo solo lector.
	 **/
	public List<Alumno> generadorAlumnosPrueba(HttpServletRequest httpRequest,
			Asignatura asignatura, Curso curso, Integer inicio, Integer fin)
			throws AppException {

		// int cantidadAlumnos = 12 + cantidad;
		Integer cantidadAlumnos = inicio + fin;
		Double tipo;
		List<Alumno> alumnos = new ArrayList<Alumno>();
		List<String> estilos = new ArrayList<String>();
		for (int i = inicio; i <= fin; i++) {

			estilos.add("lector");

			tipo = TipoAlumno.NIVEL_CONOCIMIENTO_BAJO;
			Alumno alumno = crearAlumnoConEstilo(httpRequest, asignatura,
					curso, i, tipo, estilos);
			estilos.clear();
			alumnos.add(alumno);
		}
		System.out.println("generador de alumnos");
		return alumnos;
	}

	private EstiloAprendizaje crearEstiloAprendizaje(List<String> lista,
			HttpServletRequest httpRequest, Long idAlu) throws AppException {
		EstiloAprendizaje ea = new EstiloAprendizaje();
		ea.setFechaCreacion(new Date());
		ea.setPrimerEstilo(lista.get(0));
		if (lista.size() > 1)
			ea.setSegundoEstilo(lista.get(1));

		ea.setResultadoA(1);
		ea.setResultadoK(2);
		ea.setResultadoR(3);
		ea.setResultadoV(4);
		ea.setUsado(false);
		ea.setUsuarioCreacion(new Long(1));
		ea.setIpCreacion("127.0.0.1");
		ea.setIdAlumno(idAlu);
		estiloService.insertar(ea, httpRequest);
		return ea;
	}

	/***
	 * Se simula un alumno con una tarea especifica. En la cual se van creando
	 * sesiones.
	 * 
	 * @throws AppException
	 ***/
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void simularAlumno(Alumno alumno, Tarea tarea,
			Asignatura asignatura, HttpServletRequest httpRequest)
			throws AppException {

		System.out.println("tarea: " + tarea.getId() + "alumno: "
				+ alumno.getId());
		Long idAlumno = alumno.getId();
		Long idTarea = tarea.getId();
		Long idSesion = null;

		/** Creacion de sesion ***/
		// Integer idSesion = crearSesion(alumno.getIdAlumno(),
		// tarea.getIdTarea());
		Sesion sesion = null;
		sesion = sesionService.registrarSesion(idAlumno, idTarea, httpRequest);
		idSesion = sesion.getId();
		System.out.println("##########sesion creada ######");

		/**
		 * Llama al primer ejercicio
		 **/
		// String siguienteEjercicio = ws.getSiguienteEjercicio(
		// tarea.getIdTarea(), alumno.getIdAlumno(), 0, "respuesta");

		String siguienteEjercicio = null;
		siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea, alumno,
				null, asignatura.getId(), "respuesta", asignatura);
		if (siguienteEjercicio == null)
			siguienteEjercicio = "No hay Siguiente Ejercicio";

		System.out.println("Primer ejercicio: " + siguienteEjercicio);

		/***
		 * Variables para el for. Aqui hace tipo por la cantidad de ejercicios
		 * que dispuso el profesor
		 **/

		Boolean parada = false;
		int cant = 0;
		Random rnd = new Random();

		for (int i = 0; i < tarea.getCantidadEjercicioParada(); i++) {
			List<Object> fila = new ArrayList<Object>();
			/*
			 * String[] ejercicioString = siguienteEjercicio.split("#"); Integer
			 * idEjercicio = Integer.valueOf(ejercicioString[0]); Ejercicio
			 * ejercicio = admin.obtenerEjercicio(idEjercicio);
			 */
			/** Obtener el ejercicio **/
			String[] ejercicioString = siguienteEjercicio.split("#");
			Long idEjercicio = Long.valueOf(ejercicioString[0]);
			Ejercicio ejercicioNumero = ejercicioService.obtener(idEjercicio);
			if (ejercicioNumero != null)
				System.out.println("obtuve el ejercicio: "
						+ ejercicioNumero.getEnunciado());

			/** Variables para decidir respuesta **/
			int decision = rnd.nextInt(100);
			String respuesta = null;
			Long idRespuesta = null;
			Boolean respuestaCorrecta = false;

			/**
			 * ES para elegir la respuesta Si la decision aleatoria es menor o
			 * igual al tipo de alumno Por ejemplo Decision 6 < 9 dificil
			 * Decision 4 < 5 medio decision 1 < 1 bajo alumno = 10 bajo Alumno
			 * = 50 medio Alumno = 90 alto
			 * */
			/**
			 * Si decision es menor o igual al tipo del alumno. Es TRUE la
			 * respuesta
			 **/
			if (decision <= (alumno.getTipo() * 100)) {
				respuesta = ejercicioNumero.getRespuesta().getDescripcion();
				idRespuesta = ejercicioNumero.getRespuesta().getId();
				respuestaCorrecta = true;

				/**
				 * Si decision es mayor al tipo del alumno. Es false la
				 * respuesta
				 **/
			} else {
				// List<Respuesta> respuestas =
				// admin.obtenerRespuestas(ejercicio);
				// siempre el dos.

				respuesta = ejercicioNumero.getListaRespuesta().get(1)
						.getDescripcion();
				respuestaCorrecta = false;

			}

			/** Siguiente Ejercicio */
			/*
			 * siguienteEjercicio = ws .getSiguienteEjercicio(tarea.getId(),
			 * alumno.getId(), ejercicioNumero.getId(), respuesta);
			 */

			siguienteEjercicio = admAlumno.getSiguienteEjercicio(tarea, alumno,
					ejercicioNumero, asignatura.getId(), respuesta, asignatura);
			if (siguienteEjercicio == null)
				siguienteEjercicio = "No hay nada de siguiente ejercicio";

			System.out.println("Siguiente  ejercicio: " + siguienteEjercicio);

			// Asignatura asignatura, Integer idAlumno, Integer idEjercicio,
			// Boolean respuesta

			fila = adm.registrarEjercicio(asignatura, alumno.getId(),
					ejercicioNumero, respuestaCorrecta, i, alumno.getTipo()
							.toString());
			crearArchivo(fila);
			fila.clear();
			System.out.println("sali de registarEjercicio");
		}

		// ws.terminarTarea(idSesion);
		// sesion.setEstadoTerminado(true);
		sesionService.modificar(sesion.getId(), sesion, httpRequest);

	}

	/** Escribe en archivo las pruebas **/
	private void crearArchivo(List<Object> fila) {

		File archivo = new File(direccionArchivo + "mprueba_tarea.csv");
		FileWriter escribir;
		try {
			escribir = new FileWriter(archivo, true);

			String idList = fila.toString();
			String csv = idList.substring(1, idList.length() - 1).replace(", ",
					",");
			escribir.write(csv);

			escribir.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/** Clasificacion alumno de forma statica **/
	public static class TipoAlumno {
		public static final Double NIVEL_CONOCIMIENTO_BAJO = 0.1;
		public static final Double NIVEL_CONOCIMIENTO_MEDIO = 0.5;
		public static final Double NIVEL_CONOCIMIENTO_ALTO = 0.9;

	}

	/******
	 * AQUI EMPIEZA LA TUTORIZACION
	 * 
	 * @throws AppException
	 *             ***
	 * 
	 */

	/** Simulacion de varios alumnos en simulador de tutor */
	public String simulacionTutorVarios(HttpServletRequest httpRequest,
			Long idAsig, Long idTarea, Long idArchivo, Integer inicio,
			Integer fin) throws AppException {

		int numeroAlumno = inicio;
		while (numeroAlumno < fin) {
			Long aluNumero = new Long(numeroAlumno);
			String resi = simulacionTutor(httpRequest, idAsig, aluNumero,
					idTarea, idArchivo);
			numeroAlumno++;
		}

		return "fin de varios alumnos simulados en tutor 2 ";

	}

	/**
	 * Simula un solo alumno en una interaccion del tutor que son 7 intentos
	 **/
	public String simulacionTutor(HttpServletRequest httpRequest, Long idAsig,
			Long idAlu, Long idTarea, Long idArchivo) throws AppException {

		/** Arranca el motor de reglas. Va a obtener el archivo 1 **/
		String archivo = drlService.obtenerArchivo(idArchivo);
		HerramientasDrools hd = drlService.iniciarDrools(archivo);

		/* Obtenemos la tarea 1* */
		// Long idTarea = new Long(7);
		Tarea tarea = new Tarea();
		tarea = tareaService.obtener(idTarea);
		if (tarea != null) {
			System.out.println("Conseguimos tarea");
		} else {
			System.out.println("No conseguimos la tarea");
		}

		// Lista de conceptos de la tarea a tutorizar
		List<Concepto> listaConcepto = new ArrayList<Concepto>();
		listaConcepto = tarea.getListaConceptosTarea();
		if (listaConcepto != null) {
			System.out.println("Traje la lista de conceptos sin drama");

		} else {
			System.out.println("No tiene conceptos.");
		}
		String nombreConcepto = null;
		Double valorNodo = null;
		// Long idAsig = new Long(4);
		// Long idAlu = new Long(37);

		Double valorFijadoProfesor = new Double(0.6);
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

		/**
		 * Aqui le vamos a meter un cambio. Que sera un for con los doce alumnos
		 **/
		/*
		 * if (conceptosAEvaluar != null) { int numeroAlumno = 219;
		 * while(numeroAlumno < 237){ Long aluNumero = new Long(numeroAlumno);
		 * evaluarTutor(conceptosAEvaluar, aluNumero, idAsig, idTarea,
		 * httpRequest, hd); numeroAlumno++; }
		 * 
		 * }
		 */
		String alumnoEvaluado = new String();

		if (conceptosAEvaluar != null) {
			System.out.println("Hay conceptos a evaluar\n");
			evaluarTutor(conceptosAEvaluar, idAlu, idAsig, idTarea,
					httpRequest, hd);
			alumnoEvaluado = "alumno evaluado: " + idAlu.toString();
		} else {
			System.out.println("No hay conceptos a evaluar\n");
			alumnoEvaluado = "alumno no evaluado: " + idAlu.toString();
		}

		return alumnoEvaluado;

	}

	/**
	 * Aqui esta el tutor en si. Evalua a un alumno en el test.
	 * 
	 * @throws AppException
	 */
	private void evaluarTutor(List<Concepto> lista, Long idAlu, Long idAsig,
			Long idTarea, HttpServletRequest httpRequest, HerramientasDrools hd)
			throws AppException {

		// List<String> listaR = new ArrayList<String>();
		String nombreConcepto = null;
		Double valorNodo = null;
		/** Concepto umbral definido aqui. **/
		Double conceptoUmbral = new Double("0.70");
		int x = 1; // bandera para controlar la cantidad de intentos.
		int cantidadIntentos = 5; // esto deberia ser
									// tarea.getCantidadIntentos.
		boolean pasoPorMaterial = false;
		String respuestaEjercicio = null;
		Boolean respuesta = null;// respuesta true si respondio bien y false si
									// respondio mal

		/**
		 * Creacion de sesion que contiene los ejercicios y los materiales
		 **/
		// Sesion sesion = null;
		// sesion = sesionService.registrarSesion(idAlu, idTarea, httpRequest);
		// Long idSesion = sesion.getId();
		// System.out.println("##########sesion para Material creada ######");

		/*** Se crea una lista de respuesta **/
		// listaR = respuestasGeneradas(numero);
		// int contador = 0;

		/**
		 * Por cada concepto traer un ejercicio
		 **/
		for (Concepto c : lista) {

			/** Traemos el valor del nodo **/
			nombreConcepto = c.getNombre();
			valorNodo = adm
					.getValorNodoRedDouble(nombreConcepto, idAsig, idAlu);

			/** bandera para controlar la cantidad de intentos **/
			x = 1;
			/**
			 * Paso por material. Bandera para controlar el tema de registrar
			 * los logs
			 **/
			pasoPorMaterial = false;

			// random para que las respuestas sean aleatorias.
			Random aleatorio = new Random(System.currentTimeMillis());

			/**
			 * Se crea una evidencia por concepto y se setea su concepto, su
			 * nivel actual en este concepto
			 * **/
			Evidencia e = new Evidencia();
			e.setConcepto(nombreConcepto);
			e.setNivelEvidencia(valorNodo);
			/** estaticos aun **/
			e.setEstilo("visual");
			e.setIdAsignatura(idAsig);

			/**
			 * OJOOOO .... SE DEBE BORRAR AL FINALIZAR LAS PRUEBAS PRUEBA Se
			 * crea un log que guarda toda la ruta del alumno. Ya existe
			 * evidencia. pero es para las reglas. Este log es temporal para
			 * revisar el comportamiento del tutor en si.
			 * #######################################################
			 * **/
			Log log = new Log();
			log.setConcepto(nombreConcepto);
			log.setAlumno(idAlu);
			log.setAsignatura(idAsig);
			log.setTarea(idTarea);
			log.addSecuencia("alumno: ");
			log.addSecuencia(idAlu.toString());
			log.addSecuencia("\n");
			log.addSecuencia(nombreConcepto);
			log.addSecuencia(valorNodo.toString());

			// ########################################################333

			/**
			 * Mientras el valor del nodo sea menor al umbral requerido
			 * valorNodo < conceptoUmbral &&
			 **/
			while (valorNodo < conceptoUmbral && x <= cantidadIntentos) {
				/**
				 * Esta funcion trae el siguiente ejercicio en base a su
				 * utilidad sobre el concepto c y no sobre la asignatura
				 **/
				Ejercicio ejercicio = admAlumno
						.getSiguienteEjercicioPorConcepto(idTarea, idAlu,
								idAsig, c);

				if (ejercicio == null) {
					System.out
							.println("ejercicio nulo, Hacemos un break porque no existe mas ningun ejercicio"
									+ "######################################################");
					break;
				}
				// res respuestaEjercicio =
				// ejercicio.getRespuesta().getDescripcion();
				/**
				 * Respuesta aleatoria con una semilla presente estatica if
				 * (listaR.get(contador) == "T") { respuestaEjercicio =
				 * ejercicio.getRespuesta().getDescripcion(); } else {
				 * respuestaEjercicio = "respuestaMALA"; }
				 */

				int intAletorio = aleatorio.nextInt(2);
				if (intAletorio == 1) {

					respuestaEjercicio = ejercicio.getRespuesta()
							.getDescripcion();
				} else {
					respuestaEjercicio = "respuestaMALA";
				}

				aleatorio.setSeed(System.currentTimeMillis());

				respuesta = admAlumno.responderEjercicioConcepto(ejercicio,
						respuestaEjercicio, idAlu, idAsig, idTarea);

				// se registra el ejercicio en la evidencia
				e.addEjercicio(ejercicio.getId());

				Material material = null;
				// Boolean noEsRegla = false;

				if (respuesta && pasoPorMaterial) {

					registrarEvidencia(e, httpRequest);
					x--;
					pasoPorMaterial = false;
					// Aqui si se registra un nuevo valor de nodo
					/**
					 * Aqui le re agrego el calculo del valor de nodo por
					 * concepto y le reasigno
					 **/
					// System.out.println("recalculo de nodo valor de concepto");
					// valorNodo = adm.getValorNodoRedDouble(nombreConcepto,
					// idAsig, idAlu);
					// e.setNivelEvidencia(valorNodo);

				} else if (!respuesta) {
					// e.formatearEvidencia();

					material = aplicarReglaMaterial(e, hd, idTarea, idAlu);
					if (material == null) {
						System.out
								.println("salte porque no existe material disponible");
						break;
					} else {
						mostrarMaterial(material);
						e.addMaterial(material.getId());
						pasoPorMaterial = true;
					}

				}

				// se recalcula el valor del nodo pase o no por un material.
				/**
				 * Seria el valor nuevo ya
				 **/
				valorNodo = adm.getValorNodoRedDouble(nombreConcepto, idAsig,
						idAlu);
				e.setNivelEvidencia(valorNodo);

				/***
				 * verifica que exista otro estilo de aprendizaje Por ahora no
				 * tiene
				 **/
				if (x == cantidadIntentos && tieneSegundoEstilo(idAlu)) {
					cambiarEstilo(idAlu);
					// x = 0;
				}

				// aumenta la cantidad de intentos
				x++;
				System.out.println("##############################");
				System.out
						.println("\nEjercicio Visto: " + ejercicio.toString());
				System.out.println("Respuesta del ejercicio: " + respuesta);
				System.out.println("Valor Concepto: " + valorNodo);
				System.out.println("##############################\n");

				log.addSecuencia("E" + ejercicio.getId());
				log.addSecuencia(respuesta.toString());
				log.addSecuencia(valorNodo.toString());
				log.addSecuencia("#");
				log.addSecuencia("\n");
				/*** aca recolecta la informacion **/
				if (material != null) {
					log.addSecuencia("M" + material.getId());
					log.addSecuencia(material.getEsRegla().toString());
					log.addSecuencia(material.getNivel().toString());
					log.addSecuencia(material.getEstilo().toString());
				}
				/** Contador Para respuestasGeneradas **/
				// contador++
			}

			if (x > cantidadIntentos) {

				informarProfesor(valorNodo, nombreConcepto);
				log.addSecuencia("NoSUPERADO");
			} else {
				System.out.println("##############################");
				System.out.println("Concepto Superado: ");
				System.out.println("Concepto: " + nombreConcepto + ", Valor: "
						+ valorNodo);
				System.out.println("##############################\n");
				log.addSecuencia("SUPERADO");

			}

			// se guarda en la base de datos.
			Log l01 = new Log(log);
			logService.insertar(l01, httpRequest);

		}

		/** Terminamos la sesion que tiene materiales y ejercicios ***/
		// sesion.setEstadoTerminado(true);
		// sesionService.modificar(sesion.getId(), sesion, httpRequest);

	}

	/** Retorna si tiene segundo estilo **/
	private Boolean tieneSegundoEstilo(Long idAlu) {
		return false;
	}

	/** Retorna si tiene segundo estilo **/
	private void informarProfesor(Double valor, String nombre) {

		System.out.println("##############################");
		System.out.println("Concepto NO superado: ");
		System.out.println("Concepto: " + nombre + ", Valor: " + valor);
		System.out.println("##############################\n");
	}

	/** cambia de estilo **/
	private void cambiarEstilo(long idAlu) {

	}

	/**
	 * Registra una evidencia y reinicia la evidencia por llamarlo asi
	 * 
	 * @Param evidencia
	 * @Param http
	 * **/
	private void registrarEvidencia(Evidencia e, HttpServletRequest httpRequest)
			throws AppException {

		// e.formatearEvidencia();
		Evidencia e01 = new Evidencia(e, 1);
		e01.formatearEvidencia();
		evidenciaService.insertar(e01, httpRequest);

	}

	/**
	 * Obtiene un material si existe una regla por la evidencia del alumno en
	 * caso de no obtener un material por regla. obtiene un material aleatorio
	 * basado en concepto, nivel y estilo que aun no se haya visto el alumno
	 * 
	 * @Param evidencia e
	 * @Param hd. Instancia del drools
	 * @Return {@link Class} Material
	 ***/
	private Material aplicarReglaMaterial(Evidencia e, HerramientasDrools hd,
			Long idTarea, Long idAlu) throws AppException {

		Material material = new Material();

		/**
		 * Copiamos la evidencia en otro y formateamos Le paso un uno para que
		 * copie tambien los array de la clase
		 **/
		Evidencia evide = new Evidencia(e, 1);
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
			// material = materialService.obtener(new Long(1));
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
		/** Solo si el material es no nulo se inserta **/
		if (material != null) {
			/**
			 * Una vez mostrado el material por la regla o al azar se guarda en
			 * la sesionMaterialAnterior
			 ***/
			try {

				/**
				 * Insertamos un material a la sesion
				 **/
				sesionService.insertarMaterialVisto(sesionAnterior.getId(),
						sesionAnterior, material);
			} catch (AppException ex) {
				System.out.println("No se pudo insertar el material");
				ex.printStackTrace();
			}

		}

		return material;
	}

	private void mostrarMaterial(Material material) {
		System.out.println("##############################");
		System.out.println("Material Mostrado: " + material.getId() + "  ");
		System.out.println("##############################\n");
	}

	/**
	 * Lista de respuestas posibles para tres conceptos. T,T,T
	 **/
	private List<String> respuestasGeneradas(int numero) {

		List<String> d = new ArrayList<String>();

		switch (numero) {

		case 1:
			d.add("T");
			d.add("T");
			d.add("T");
			break;

		case 2:
			d.add("T");
			d.add("T");
			d.add("F");
			break;

		case 3:
			d.add("T");
			d.add("F");
			d.add("T");
			break;

		case 4:
			d.add("T");
			d.add("F");
			d.add("F");
			break;

		case 5:
			d.add("F");
			d.add("T");
			d.add("T");
			break;

		case 6:
			d.add("F");
			d.add("T");
			d.add("F");
			break;

		case 7:
			d.add("F");
			d.add("F");
			d.add("T");
			break;

		case 8:
			d.add("F");
			d.add("F");
			d.add("F");
			break;

		default:
			d.add("F");
			d.add("F");
			d.add("F");
			break;

		}

		return d;

	}

	/** Generador de arboles bayesianos de asignatura y alumnos. */
	public void simulacionGeneracionDeArbolesBayesianos(
			HttpServletRequest httpRequest) throws AppException {

		// Vamos a generar primero la red bayesiana de asignatura
		System.out.println("agregamos asignatura");
		Long idAsignatura = new Long(1);
		Asignatura asignatura = asignaturaService.obtener(idAsignatura);
		adm.agregarAsignaturaRed(asignatura);

		System.out.println("agregamos tema01");
		Long idTema01 = new Long(1);
		Tema tema01 = temaService.obtener(idTema01);
		adm.agregarTemaRed(tema01);

		System.out.println("agregamos tema02");
		Long idTema02 = new Long(2);
		Tema tema02 = temaService.obtener(idTema02);
		adm.agregarTemaRed(tema02);

		System.out.println("agregamos concepto01");
		Long idConcepto01 = new Long(1);
		Concepto concepto01 = conceptoService.obtener(idConcepto01);
		adm.agregarConceptoRed(concepto01, idAsignatura);

		System.out.println("agregamos concepto02");
		Long idConcepto02 = new Long(2);
		Concepto concepto02 = conceptoService.obtener(idConcepto02);
		adm.agregarConceptoRed(concepto02, idAsignatura);

		System.out.println("agregamos concepto03");
		Long idConcepto03 = new Long(3);
		Concepto concepto03 = conceptoService.obtener(idConcepto03);
		adm.agregarConceptoRed(concepto03, idAsignatura);

		System.out.println("agregamos concepto04");
		Long idConcepto04 = new Long(4);
		Concepto concepto04 = conceptoService.obtener(idConcepto04);
		adm.agregarConceptoRed(concepto04, idAsignatura);

		System.out.println("agregamos concepto05");
		Long idConcepto05 = new Long(5);
		Concepto concepto05 = conceptoService.obtener(idConcepto05);
		adm.agregarConceptoRed(concepto05, idAsignatura);

		// ejercicio
		for (int i = 1; i < 352; i++) {
			System.out.println("ejercicio numero " + i);
			Long idEjercicio = new Long(i);
			Ejercicio ejercicio = ejercicioService.obtener(idEjercicio);
			adm.agregarEjercicioRed(ejercicio, idAsignatura);

		}

		// inscribir al alumno
		for (int a = 1; a < 230; a++) {
			Long idAlumno = new Long(a);
			System.out.println("ejercicio numero " + a);
			adm.calcularProbabilidades(asignatura);
			adm.crearRedAlumno(idAsignatura, idAlumno);
		}

	}

	/** Cambio de pass en usuario */
	public String simulacionGeneracionpass(HttpServletRequest httpRequest)
			throws AppException {
		String retorno = "Final";

		for (int i = 1; i < 228; i++) {
			Long idUsu = new Long(i);
			Usuario usu = usuarioService.obtener(idUsu);
			String encryptedToken = new Md5Hash(usu.getPassword(),
					usu.getCedula()).toString();
			usu.setUsername(usu.getCedula());
			usu.setPassword(encryptedToken);
			usuarioService.modificar(idUsu, usu);

		}
		return retorno;
	}

	// valor de asignatura
	public String simulacionValoresAsignatura(Long idAsig, Long idAlu,
			HttpServletRequest httpRequest) {

		String r1 = adm.getValorNodoRedError("tenondera", idAsig, idAlu);
		String r2 = adm.getValorNodoRedErrorEvidencia("tenondera", idAsig,
				idAlu);
		String r = r1 + r2;

		return r;
	}

	/** Longitud de los caminos */
	public String longitudCaminoMateriales(Long usuarioInicial,
			Long usuarioFinal, HttpServletRequest httpRequest) {

		List<Camino> caminos = getDao().consultaTamanoCamino(usuarioInicial,
				usuarioFinal);
		float promedio = 0;
		Integer suma = 0;
		Integer maximo = 0;
		for (Camino camino : caminos) {

			int contador = 0;
		
			for (int i = 0; i < camino.getSecuenciaMaterial().length(); i++) {
				if ((camino.getSecuenciaMaterial().charAt(i) == '-')) {
					contador++;
				}
			}
			// se quita la suma
			suma += contador;
			// se quita el maximo
			if(maximo < contador)
				maximo = contador;
				
			System.out.println("Usuario : " + camino.getUsuarioCreacion()
					+ " tiene longitud: " + contador);

		}
		// se quita el promedio
		promedio = (float)suma/caminos.size();
		System.out.println("Suma : "+ suma + " Promedio: "+ promedio + " Maximo: " + maximo);
		String valorParaArchivo = usuarioInicial.toString() + "-"+usuarioFinal.toString()+ ", " +suma.toString()
				+ ", " + maximo.toString() 
				+ ", " + promedio; 
		List<String> vector = new ArrayList<String>();
		vector.add(valorParaArchivo);
		String ruta = "/home/catherine/ArchivosResultados/resultadosLongitudSept.csv";		
		escribirEnElArchivo(vector, ruta);
		return "fin de longitudCamino";

	}

	// comparacion de test tutor entre resultado final e inicial
	public String comparacionTutor(HttpServletRequest httpRequest,
			Long aluInicial, Long aluFinal) {

		Integer testTutor = 2;
		String gano;
		String mayorA50;
		Long aluActual = new Long(0);
		List<String> vector = new ArrayList<String>();
		while (aluInicial <= aluFinal) {
			// quita el nivel inicial y final
			List<Double> niveles = getDao().comparar(aluInicial, testTutor);
			// aumentamos esto para que salga del while
			aluActual = aluInicial;
			aluInicial++;
			// comparamos si supero su valor inicial, en este caso si gano
			if (niveles.get(0) > niveles.get(1)) {
				gano = "No";
			} else {
				gano = "Si";
			}
			// vemos si supero el 50%
			if (niveles.get(1) > new Double(0.5)) {
				mayorA50 = "Si";
			} else {
				mayorA50 = "No";
			}

			// imprimos los resultados Alumno, nivel inicial, final, gano y es
			// mayor a 60
			System.out.println(aluActual + ", " + niveles.get(0).toString()
					+ ", " + niveles.get(1).toString() + ", " + gano + ", "
					+ mayorA50);

			String text = aluActual + ", " + niveles.get(0).toString() + ", "
					+ niveles.get(1).toString() + ", " + gano + ", " + mayorA50;

			vector.add(text);

		}
		String ruta = "/home/catherine/ArchivosResultados/resultadosNivelesBIGDATA.csv";
		escribirEnElArchivo(vector, ruta);
		return "fin de comparacion";

	}

	// comparacion de test incial con test final
	public String comparacion(HttpServletRequest httpRequest)
			throws AppException {

		String r = "mauro";
		// (105,58,112,79,180,201,41,176,169,181)
		// primera tanda
		List<Long> alumnosPrimeraTanda = new ArrayList<Long>();
		alumnosPrimeraTanda.add(new Long(105));
		alumnosPrimeraTanda.add(new Long(58));
		alumnosPrimeraTanda.add(new Long(112));
		alumnosPrimeraTanda.add(new Long(79));
		alumnosPrimeraTanda.add(new Long(180));
		alumnosPrimeraTanda.add(new Long(201));
		alumnosPrimeraTanda.add(new Long(41));
		alumnosPrimeraTanda.add(new Long(176));
		alumnosPrimeraTanda.add(new Long(169));
		alumnosPrimeraTanda.add(new Long(181));
		// segunda tanda
		List<Long> alumnosSegundaTanda = new ArrayList<Long>();
		alumnosSegundaTanda.add(new Long(106));
		alumnosSegundaTanda.add(new Long(36));
		alumnosSegundaTanda.add(new Long(57));
		alumnosSegundaTanda.add(new Long(103));
		alumnosSegundaTanda.add(new Long(164));
		alumnosSegundaTanda.add(new Long(65));
		alumnosSegundaTanda.add(new Long(96));
		alumnosSegundaTanda.add(new Long(202));
		alumnosSegundaTanda.add(new Long(44));
		alumnosSegundaTanda.add(new Long(13));
		alumnosSegundaTanda.add(new Long(73));

		// tercera tanda
		List<Long> alumnosTerceraTanda = new ArrayList<Long>();
		alumnosTerceraTanda.add(new Long(129));
		alumnosTerceraTanda.add(new Long(133));
		alumnosTerceraTanda.add(new Long(50));
		alumnosTerceraTanda.add(new Long(32));
		alumnosTerceraTanda.add(new Long(320));
		alumnosTerceraTanda.add(new Long(174));
		alumnosTerceraTanda.add(new Long(319));
		// alumnosTerceraTanda.add(new Long(243) );
		// alumnosTerceraTanda.add(new Long(245) );
		alumnosTerceraTanda.add(new Long(92));
		alumnosTerceraTanda.add(new Long(321));
		alumnosTerceraTanda.add(new Long(52));
		alumnosTerceraTanda.add(new Long(207));

		Integer testInicial = 1;
		Integer testFinal = 3;
		Integer testTutor = 2;
		String gano;
		String mayorA60;
		// List<Double> niveles = getDao().comparar(new Long(105), testFinal);
		// System.out.println("Primer test.");
		// System.out.println("Nivel Inicial: "+ niveles.get(0));
		// System.out.println("Nivel Final: "+ niveles.get(1));

		System.out.println("Primera Tanda de alumnos");
		System.out
				.println("id alumno,nivelInicialPrimerTest,nivelFinalPrimerTest,nivelInicialTercerTest,  nivelFinalTercerTest, superoSuNivelIncial, Supero60 ");
		for (Long alumno01 : alumnosPrimeraTanda) {

			List<Double> nivelesInciales = getDao().comparar(alumno01,
					testInicial);
			List<Double> nivelesFinales = getDao()
					.comparar(alumno01, testFinal);
			if (nivelesInciales.get(1) > nivelesFinales.get(1)) {
				gano = "Si";
			} else {
				gano = "No";
			}

			if (nivelesFinales.get(1) > new Double(0.6)) {
				mayorA60 = "Si";
			} else {
				mayorA60 = "No";
			}

			System.out.println(alumno01 + ", "
					+ nivelesInciales.get(0).toString() + ", "
					+ nivelesInciales.get(1).toString() + ", "
					+ nivelesFinales.get(0).toString() + ", "
					+ nivelesFinales.get(1).toString() + ", " + gano + ", "
					+ mayorA60);

		}

		System.out.println("Segunda Tanda de alumnos");
		System.out
				.println("id alumno,nivelInicialPrimerTest,nivelFinalPrimerTest,nivelInicialTercerTest,  nivelFinalTercerTest, superoSuNivelIncial, Supero60 ");
		for (Long alumno02 : alumnosSegundaTanda) {

			List<Double> nivelesInciales = getDao().comparar(alumno02,
					testInicial);
			List<Double> nivelesFinales = getDao()
					.comparar(alumno02, testFinal);
			if (nivelesInciales.get(1) > nivelesFinales.get(1)) {
				gano = "Si";
			} else {
				gano = "No";
			}

			if (nivelesFinales.get(1) >= new Double(0.6)) {
				mayorA60 = "Si";
			} else {
				mayorA60 = "No";
			}

			System.out.println(alumno02 + ", "
					+ nivelesInciales.get(0).toString() + ", "
					+ nivelesInciales.get(1).toString() + ", "
					+ nivelesFinales.get(0).toString() + ", "
					+ nivelesFinales.get(1).toString() + ", " + gano + ", "
					+ mayorA60);

		}

		System.out.println("Tercera Tanda de alumnos");
		System.out
				.println("id alumno,nivelInicialPrimerTest,nivelFinalPrimerTest,nivelInicialTercerTest,  nivelFinalTercerTest, superoSuNivelIncial, Supero60 ");
		for (Long alumno03 : alumnosTerceraTanda) {

			List<Double> nivelesInciales = getDao().comparar(alumno03,
					testInicial);
			List<Double> nivelesFinales = getDao()
					.comparar(alumno03, testFinal);
			if (nivelesInciales.get(1) > nivelesFinales.get(1)) {
				gano = "Si";
			} else {
				gano = "No";
			}

			if (nivelesFinales.get(1) > new Double(0.6)) {
				mayorA60 = "Si";
			} else {
				mayorA60 = "No";
			}

			System.out.println(alumno03 + ", "
					+ nivelesInciales.get(0).toString() + ", "
					+ nivelesInciales.get(1).toString() + ", "
					+ nivelesFinales.get(0).toString() + ", "
					+ nivelesFinales.get(1).toString() + ", " + gano + ", "
					+ mayorA60);

		}

		// lista de nombre de conceptos

		// lista de test tutor del alumno 106,96
		// Long alu106 = new Long(96);

		/** Aca se resuelve el tema de si consumio regla tanda 2. **/
		List<String> vectorReglas = new ArrayList<String>();
		Long idArchivo = new Long(6);
		// Long alu106 = new Long(13);
		for (Long alu106 : alumnosSegundaTanda) {

			Long idAsignatura = new Long(1);
			// obtenemos su estilo de aprendizaje
			Alumno alumno = alumnoService.obtener(alu106);
			String primerEstilo = alumno.getEstilo().getPrimerEstilo();
			String segundoEstilo = "no tiene";
			Boolean tieneDosEstilo = false;

			// estilo de aprendizaje
			if (alumno.getEstilo().getSegundoEstilo() != null) {
				segundoEstilo = alumno.getEstilo().getSegundoEstilo();
				System.out.println("estilos " + primerEstilo + ", "
						+ segundoEstilo);
				tieneDosEstilo = true;
			} else {
				System.out.println("estilos " + primerEstilo);
			}
			// resueltos de un alumno
			List<Resuelto> resueltosTutor = getDao().comparar2TestTutor(alu106,
					testTutor);
			if (resueltosTutor == null || resueltosTutor.isEmpty()) {
				System.out.println("alumno" + " , " + alu106 + ", "
						+ "no tiene resueltos." + ", " + "nada" + ", " + "nada"
						+ ", " + "nada" + ", " + "nada" + ", " + "nada" + ", "
						+ "nada" + ", " + "nada" + " ##");
			} else {

				Resuelto resueltoInicial = resueltosTutor.get(0);
				Long idConceptoActual = resueltoInicial.getIdConcepto();
				Integer contadorIntentos = 1;
				String estiloActual = primerEstilo;

				Concepto concepto01 = conceptoService.obtener(resueltoInicial
						.getIdConcepto());
				String nombreConcepto01 = concepto01.getNombre();

				/** Creacion de un camino nuevo */
				Camino camino = crearCaminoNuevo(primerEstilo, alu106,
						idConceptoActual);
				String esRegla = new String();
				Double nivelConceptoFinalDouble = adm
						.getValorNodoRedDoubleFinales(nombreConcepto01,
								idAsignatura, alu106);
				Double nivelConceptoInicialDouble = nivelConceptoFinalDouble;
				Boolean yaCambie = false;
				for (Resuelto resuelto01 : resueltosTutor) {
					// obtengo el nombre del concepto.
					Concepto concepto = conceptoService.obtener(resuelto01
							.getIdConcepto());
					String nombreConcepto = concepto.getNombre();

					if (idConceptoActual == resuelto01.getIdConcepto()) {

						if (tieneDosEstilo) {
							if (contadorIntentos == 5 && !yaCambie) {
								// System.out.println("cambio de estilo inicial: "
								// + camino.getSecuenciaEjercicio());

								estiloActual = segundoEstilo;
								camino = crearCaminoNuevo(estiloActual, alu106,
										resuelto01.getIdConcepto());
								// System.out.println("cambio de estilo final: "
								// + camino.getSecuenciaEjercicio());
								contadorIntentos = 1;
								yaCambie = true;

							}
							if (!resuelto01.getEsMaterial()) {
								contadorIntentos++;
							}

						}

					} else {

						// System.out.println("camino eje antes de cambiar: "
						// + camino.getSecuenciaEjercicio());
						camino = crearCaminoNuevo(estiloActual, alu106,
								resuelto01.getIdConcepto());
						idConceptoActual = resuelto01.getIdConcepto();
						contadorIntentos = 1;
						// System.out.println("camino eje despues de cambiar: "
						// + camino.getSecuenciaEjercicio());
					}

					// si no es material
					Boolean resp = false;
					if (!resuelto01.getEsMaterial()) {
						resp = admAlumno.responderFinalPrueba(
								resuelto01.getIdEjercicio(),
								resuelto01.getEsCorrecto(),
								resuelto01.getIdAlumno(), idAsignatura,
								idAsignatura, httpRequest);

						// nivel inicial
						nivelConceptoFinalDouble = adm
								.getValorNodoRedDoubleFinales(nombreConcepto,
										idAsignatura, alu106);

						if (resp == true) {
							camino.setNivelEvidencia(nivelConceptoFinalDouble);
							camino.setAnterior(camino.getActual());
							camino.setActual("E");
							camino.setEsEjercicio(true);
							// System.out.println("agrege el ejercicio: "
							// + resuelto01.getIdEjercicio().toString());
							camino.setSecuenciaEjercicio(resuelto01
									.getIdEjercicio().toString());
							// System.out.println("camino ejercicio es: "
							// + camino.getSecuenciaEjercicio());
						} else {
							camino.setAnterior(camino.getActual());
							camino.setActual("M");
							camino.setEsEjercicio(false);
							// System.out.println("agrege el ejercicio: "
							// + resuelto01.getIdEjercicio().toString());
							camino.setSecuenciaEjercicio(resuelto01
									.getIdEjercicio().toString());
							// System.out.println("camino ejercicio es: "
							// + camino.getSecuenciaEjercicio());
						}

						// si es material vemos si es regla.
					} else {

						esRegla = ejercicioService.aplicarReglaFinalTutor(
								camino, idArchivo);

						// nivel inicial
						nivelConceptoInicialDouble = adm
								.getValorNodoRedDoubleFinales(nombreConcepto,
										idAsignatura, alu106);
						nivelConceptoFinalDouble = nivelConceptoInicialDouble;
						/** Se actualiza su nivel */
						camino.setNivelEvidencia(nivelConceptoInicialDouble);
						camino.setAnterior(camino.getActual());
						camino.setActual("E");
						camino.setEsEjercicio(true);
						camino.setSecuenciaMaterial(resuelto01.getIdMaterial()
								.toString());

					}

					// String nivelConceptoFinal =
					// adm.getValorNodoRedFinal(nombreConcepto, idAsignatura,
					// alu106);
					// System.out.println(df.format(number));
					// idResuelto, idAlumno,nombreConcepto,
					// estilo,ejercicio,material,esCorrecto,esRegla,
					// nivelInicial,nivelFinal

					System.out.println(resuelto01.getId() + " , "
							+ resuelto01.getIdAlumno() + ", " + nombreConcepto
							+ ", " + estiloActual + ", "
							+ resuelto01.getIdEjercicio() + ", "
							+ resuelto01.getIdMaterial() + ", "
							+ resuelto01.getEsCorrecto() + ", " + esRegla
							+ ", " + nivelConceptoInicialDouble + ", "
							+ nivelConceptoFinalDouble + " ##");
					String nuevo = resuelto01.getId() + " , "
							+ resuelto01.getIdAlumno() + ", " + nombreConcepto
							+ ", " + estiloActual + ", "
							+ resuelto01.getIdEjercicio() + ", "
							+ resuelto01.getIdMaterial() + ", "
							+ resuelto01.getEsCorrecto() + ", "
							+ nivelConceptoInicialDouble + ", "
							+ nivelConceptoFinalDouble + ", " + esRegla;

					vectorReglas.add(nuevo);
					esRegla = new String();

				}
			}
		}
		String ruta = "/home/catherine/ArchivosResultados/resultadosNivelesBIGDATA.csv";
		escribirEnElArchivo(vectorReglas, ruta);

		return r;
	}

	private Camino crearCaminoNuevo(String primerEstilo, Long idAlumno,
			Long idConcepto) throws AppException {
		Camino camino = new Camino();
		camino.setActual("E");
		camino.setAnterior("N");
		camino.setEsEjercicio(true);
		camino.setFechaCreacion(new Date());
		camino.setUsuarioCreacion(new Long(1));
		camino.setIpCreacion("127.0.0.1");
		camino.setParar(false);

		camino.setEstilo(primerEstilo);
		camino.setIdAlumno(idAlumno);
		camino.setIdAsignatura(new Long(1));
		camino.setIdConcepto(idConcepto);
		Concepto c = conceptoService.obtener(idConcepto);

		camino.setNombreConcepto(c.getNombre());
		Double valorNodo = adm.getValorNodoRedDoubleFinales(c.getNombre(),
				new Long(1), idAlumno);
		camino.setNivelEvidencia(valorNodo);
		camino.setNivelInicial(valorNodo);
		return camino;
	}

	private void escribirEnElArchivo(List<String> reglas,String ruta) {
		
		try {
			// String ruta = "/home/catherine/reglasDiseño.";
			
			String contenido = "Contenido de ejemplo";
			
			
			
			File file = new File(ruta);
			// Si el archivo no existe es creado
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String re : reglas) {
				bw.write(re + "\n");
			}

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String materialesUtilesEjercicios(HttpServletRequest httpRequest)
			throws AppException {

		Ejercicio ejercicioConMaterial = ejercicioService
				.obtener(new Long(115));
		Boolean tieneM = tieneUnMaterialGeneral(ejercicioConMaterial);

		System.out.println("tiene un material con funcion: " + tieneM);

		return "final";

	}

	private Boolean tieneUnMaterialGeneral(Ejercicio ejercicioConMaterial) {

		// materiales visto. Se cambiaria por la sesion del momento.
		List<Long> materialesVistos = new ArrayList<Long>();
		materialesVistos.add(new Long(10)); // material que no esta
		// materialesVistos.add(new Long(35)); // material que si esta
		materialesVistos.add(new Long(18)); // material que no esta

		// materiales si tiene
		List<Long> listaDeMateriales = new ArrayList<Long>();
		// Tiene
		Boolean tieneMas = false;

		if (ejercicioConMaterial.getMaterialUtil() == null) {
			System.out.println("No tiene materiales útiles");

		} else {
			System.out.println("Tiene Materiales útiles");
			// cambiamos a array de long
			String[] materiales = ejercicioConMaterial.getMaterialUtil().split(
					",");
			for (String material : materiales) {
				if (!material.isEmpty())
					listaDeMateriales.add(Long.valueOf(material));
			}
			System.out.println("lista de materiales: " + listaDeMateriales);
			// vemos si tiene algun material
			tieneMas = tieneUnMaterial(materialesVistos, listaDeMateriales);
		}

		return tieneMas;
	}

	private Boolean tieneUnMaterial(List<Long> materialesVistos,
			List<Long> listaDeMateriales) {
		Boolean tiene = false;
		Long m = new Long(0);
		for (Long mateVisto : materialesVistos) {
			if (listaDeMateriales.contains(mateVisto)) {
				System.out.println("tengo este material: " + mateVisto);
				tiene = true;
				m = mateVisto;
				break;
			} else {
				System.out.println("no tengo este material: " + mateVisto);
			}
		}
		return tiene;
	}

}
