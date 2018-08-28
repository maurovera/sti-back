package service;

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
import model.Concepto;
import model.Curso;
import model.Ejercicio;
import model.Evidencia;
import model.Material;
import model.Respuesta;
import model.Sesion;
import model.SesionMaterial;
import model.Simulacion;
import model.Tarea;
import model.Tema;
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

	final String direccionArchivo = "/home/mauro/proyectos/tesis/sti-back/src/main/resources/archivos/";

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
	private EvidenciaService evidenciaService;

	@Inject
	private DrlService drlService;

	@Inject
	private MaterialService materialService;

	@Inject
	private SesionMaterialService sesionMaterialService;

	// private SessionService session;
	final private long userId = 1;

	@Override
	public SimulacionDAO getDao() {

		return dao;
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

		// Ejercicio11
		Ejercicio e11 = new Ejercicio();
		e11.setAdivinanza(new Double(0.2));// medio
		e11.setNivelDificultad(new Double(1));// facil
		e11.setEnunciado("ejercicio11");
		e11.setIdAsignatura(asig.getId());
		e11.addConceptos(c1);

		// Ejercicio12
		Ejercicio e12 = new Ejercicio();
		e12.setAdivinanza(new Double(0.3));// alto
		e12.setNivelDificultad(new Double(1));// facil
		e12.setEnunciado("ejercicio12");
		e12.setIdAsignatura(asig.getId());
		e12.addConceptos(c1);

		// Ejercicio13
		Ejercicio e13 = new Ejercicio();
		e13.setAdivinanza(new Double(0.2));// medio
		e13.setNivelDificultad(new Double(1));// facil
		e13.setEnunciado("ejercicio13");
		e13.setIdAsignatura(asig.getId());
		e13.addConceptos(c3);

		// Ejercicio14
		Ejercicio e14 = new Ejercicio();
		e14.setAdivinanza(new Double(0.3));// alto
		e14.setNivelDificultad(new Double(1));// facil
		e14.setEnunciado("ejercicio14");
		e14.setIdAsignatura(asig.getId());
		e14.addConceptos(c3);

		// Ejercicio15
		Ejercicio e15 = new Ejercicio();
		e15.setAdivinanza(new Double(0.2));// medio
		e15.setNivelDificultad(new Double(1));// facil
		e15.setEnunciado("ejercicio15");
		e15.setIdAsignatura(asig.getId());
		e15.addConceptos(c5);

		// Ejercicio16
		Ejercicio e16 = new Ejercicio();
		e16.setAdivinanza(new Double(0.3));// alto
		e16.setNivelDificultad(new Double(1));// facil
		e16.setEnunciado("ejercicio16");
		e16.setIdAsignatura(asig.getId());
		e16.addConceptos(c5);

		// Ejercicio17
		Ejercicio e17 = new Ejercicio();
		e17.setAdivinanza(new Double(0.2));// medio
		e17.setNivelDificultad(new Double(2));// medio
		e17.setEnunciado("ejercicio17");
		e17.setIdAsignatura(asig.getId());
		e17.addConceptos(c3);
		e17.addConceptos(c1);

		// Ejercicio18
		Ejercicio e18 = new Ejercicio();
		e18.setAdivinanza(new Double(0.1));// medio
		e18.setNivelDificultad(new Double(2));// medio
		e18.setEnunciado("ejercicio18");
		e18.setIdAsignatura(asig.getId());
		e18.addConceptos(c3);
		e18.addConceptos(c1);

		// Ejercicio19
		Ejercicio e19 = new Ejercicio();
		e19.setAdivinanza(new Double(0.1));// medio
		e19.setNivelDificultad(new Double(3));// medio
		e19.setEnunciado("ejercicio19");
		e19.setIdAsignatura(asig.getId());
		e19.addConceptos(c3);
		e19.addConceptos(c1);
		e19.addConceptos(c5);

		// Ejercicio20
		Ejercicio e20 = new Ejercicio();
		e20.setAdivinanza(new Double(0.2));// medio
		e20.setNivelDificultad(new Double(3));// medio
		e20.setEnunciado("ejercicio20");
		e20.setIdAsignatura(asig.getId());
		e20.addConceptos(c3);
		e20.addConceptos(c1);
		e20.addConceptos(c5);

		// Ejercicio21
		Ejercicio e21 = new Ejercicio();
		e21.setAdivinanza(new Double(0.3));// medio
		e21.setNivelDificultad(new Double(3));// medio
		e21.setEnunciado("ejercicio21");
		e21.setIdAsignatura(asig.getId());
		e21.addConceptos(c3);
		e21.addConceptos(c1);
		e21.addConceptos(c5);

		// Ejercicio22
		Ejercicio e22 = new Ejercicio();
		e22.setAdivinanza(new Double(0.3));// medio
		e22.setNivelDificultad(new Double(3));// medio
		e22.setEnunciado("ejercicio22");
		e22.setIdAsignatura(asig.getId());
		e22.addConceptos(c3);
		e22.addConceptos(c1);
		e22.addConceptos(c2);
		e22.addConceptos(c4);

		// Ejercicio23
		Ejercicio e23 = new Ejercicio();
		e23.setAdivinanza(new Double(0.3));// medio
		e23.setNivelDificultad(new Double(3));// medio
		e23.setEnunciado("ejercicio23");
		e23.setIdAsignatura(asig.getId());
		e23.addConceptos(c3);
		e23.addConceptos(c1);
		e23.addConceptos(c2);
		e23.addConceptos(c4);
		e23.addConceptos(c5);

		// Ejercicio24
		Ejercicio e24 = new Ejercicio();
		e24.setAdivinanza(new Double(0.1));// bajo
		e24.setNivelDificultad(new Double(2));// medio
		e24.setEnunciado("ejercicio24");
		e24.setIdAsignatura(asig.getId());
		e24.addConceptos(c5);
		e24.addConceptos(c1);

		// Ejercicio25
		Ejercicio e25 = new Ejercicio();
		e25.setAdivinanza(new Double(0.2));// medio
		e25.setNivelDificultad(new Double(2));// medio
		e25.setEnunciado("ejercicio25");
		e25.setIdAsignatura(asig.getId());
		e25.addConceptos(c5);
		e25.addConceptos(c1);

		// Ejercicio26
		Ejercicio e26 = new Ejercicio();
		e26.setAdivinanza(new Double(0.2));// medio
		e26.setNivelDificultad(new Double(3));// alto
		e26.setEnunciado("ejercicio26");
		e26.setIdAsignatura(asig.getId());
		e26.addConceptos(c5);
		e26.addConceptos(c1);

		// Ejercicio27
		Ejercicio e27 = new Ejercicio();
		e27.setAdivinanza(new Double(0.1));// medio
		e27.setNivelDificultad(new Double(2));// alto
		e27.setEnunciado("ejercicio27");
		e27.setIdAsignatura(asig.getId());
		e27.addConceptos(c5);
		e27.addConceptos(c3);

		// Ejercicio28
		Ejercicio e28 = new Ejercicio();
		e28.setAdivinanza(new Double(0.2));// medio
		e28.setNivelDificultad(new Double(2));// alto
		e28.setEnunciado("ejercicio28");
		e28.setIdAsignatura(asig.getId());
		e28.addConceptos(c5);
		e28.addConceptos(c3);

		// Ejercicio29
		Ejercicio e29 = new Ejercicio();
		e29.setAdivinanza(new Double(0.3));// medio
		e29.setNivelDificultad(new Double(3));// alto
		e29.setEnunciado("ejercicio29");
		e29.setIdAsignatura(asig.getId());
		e29.addConceptos(c2);
		e29.addConceptos(c3);
		e29.addConceptos(c4);

		// Ejercicio30
		Ejercicio e30 = new Ejercicio();
		e30.setAdivinanza(new Double(0.3));// medio
		e30.setNivelDificultad(new Double(3));// alto
		e30.setEnunciado("ejercicio30");
		e30.setIdAsignatura(asig.getId());
		e30.addConceptos(c2);
		e30.addConceptos(c3);
		e30.addConceptos(c4);
		e30.addConceptos(c1);
		e30.addConceptos(c5);

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
		listaEjercicio.add(e11);
		listaEjercicio.add(e12);
		listaEjercicio.add(e13);
		listaEjercicio.add(e14);
		listaEjercicio.add(e15);
		listaEjercicio.add(e16);
		listaEjercicio.add(e17);
		listaEjercicio.add(e18);
		listaEjercicio.add(e19);
		listaEjercicio.add(e20);
		listaEjercicio.add(e21);
		listaEjercicio.add(e22);
		listaEjercicio.add(e23);
		listaEjercicio.add(e24);
		listaEjercicio.add(e25);
		listaEjercicio.add(e26);
		listaEjercicio.add(e27);
		listaEjercicio.add(e28);
		listaEjercicio.add(e29);
		listaEjercicio.add(e30);

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
		System.out.println("Ejercicio 30: " + e30.getId());

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

		/** Creacion de material ****/
		Material material1 = new Material();
		Material material2 = new Material();
		Material material3 = new Material();
		Material material4 = new Material();
		Material material5 = new Material();
		Material material6 = new Material();

		// M1
		material1.setConcepto("division");
		material1.setEstilo("visual");
		material1.setIdAsignatura(asig.getId());
		material1.setNivel("bajo");
		material1.setUrlMaterial("https://www.youtube.com/watch?v=-4pe1ui9b7g");

		// M2
		material2.setConcepto("division");
		material2.setEstilo("visual");
		material2.setIdAsignatura(asig.getId());
		material2.setNivel("bajo");
		material2.setUrlMaterial("https://www.youtube.com/watch?v=kuZii-vk5Xw");

		// M3
		material3.setConcepto("potencia");
		material3.setEstilo("visual");
		material3.setIdAsignatura(asig.getId());
		material3.setNivel("bajo");
		material3.setUrlMaterial("https://www.youtube.com/watch?v=5WU3id8xhiw");

		// M4
		material4.setConcepto("potencia");
		material4.setEstilo("visual");
		material4.setIdAsignatura(asig.getId());
		material4.setNivel("bajo");
		material4.setUrlMaterial("https://www.youtube.com/watch?v=vC33KSOkmk8");

		// M6
		material6.setConcepto("potencia");
		material6.setEstilo("visual");
		material6.setIdAsignatura(asig.getId());
		material6.setNivel("bajo");
		material6.setUrlMaterial("https://www.youtube.com/watch?v=JwjvT-9Zz58");

		// M5
		material5.setConcepto("suma");
		material5.setEstilo("visual");
		material5.setIdAsignatura(asig.getId());
		material5.setNivel("bajo");
		material5.setUrlMaterial("https://www.youtube.com/watch?v=MnKRsHDchmw");

		materialService.insertar(material1, httpRequest);
		materialService.insertar(material2, httpRequest);
		materialService.insertar(material3, httpRequest);
		materialService.insertar(material4, httpRequest);
		materialService.insertar(material5, httpRequest);
		materialService.insertar(material6, httpRequest);

		return asig;

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
		tarea01.setDescripcion("Tarea01 de prueba");
		tarea01.setEstadoTarea(false);
		tarea01.setNombre("Tarea01");
		// c1,c3,c5
		for (Concepto cc : conceptos01)
			tarea01.addConceptos(cc);

		Tarea tarea02 = new Tarea();
		tarea02.setCantidadEjercicioParada(5);
		tarea02.setCurso(c);
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
		sesion.setEstadoTerminado(true);
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

		Double valorFijadoProfesor = new Double(0.70);
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
		if (conceptosAEvaluar != null) {

			evaluarTutor(conceptosAEvaluar, idAlu, idAsig, idTarea,
					httpRequest, hd);
		}

		return "termineTutor";

	}

	/**
	 * Aqui esta el tutor en si
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
		int cantidadIntentos = 7; // esto deberia ser
									// tarea.getCantidadIntentos.
		boolean pasoPorMaterial = false;
		String respuestaEjercicio = null;
		Boolean respuesta = null;// respuesta true si respondio bien y false si
									// respondio mal

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
					System.out.println("ejercicio nulo");
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

				if (respuesta && pasoPorMaterial) {

					registrarLog(e, httpRequest);
					x--;
					pasoPorMaterial = false;
					// Aqui si se registra un nuevo valor de nodo
					e.setNivelEvidencia(valorNodo);

				} else if (!respuesta) {
					// e.formatearEvidencia();

					Material material = aplicarReglaMaterial(e, hd, idTarea,
							idAlu);
					mostrarMaterial(material);
					e.addMaterial(material.getId());
					pasoPorMaterial = true;
				}

				// se recalcula el valor del nodo pase o no por un material.
				valorNodo = adm.getValorNodoRedDouble(nombreConcepto, idAsig,
						idAlu);

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

				System.out.println("\nEjercicio: " + ejercicio.toString());
				System.out.println("Respuesta del ejercicio: " + respuesta
						+ "\n");

				/** Contador Para respuestasGeneradas **/
				// contador++
			}

			if (x > cantidadIntentos) {

				informarProfesor(valorNodo, nombreConcepto);
			} else {
				System.out.println("##############################");
				System.out.println("Concepto que el alumno logro superar: ");
				System.out.println("Concepto: " + nombreConcepto + ", Valor: "
						+ valorNodo);
				System.out.println("\n");
				System.out.println("##############################\n");

			}

		}

	}

	/** Retorna si tiene segundo estilo **/
	private Boolean tieneSegundoEstilo(Long idAlu) {
		return false;
	}

	/** Retorna si tiene segundo estilo **/
	private void informarProfesor(Double valor, String nombre) {

		System.out.println("##############################");
		System.out
				.println("Concepto que el alumno no logro superar el la tutorizacion: ");
		System.out.println("Concepto: " + nombre + ", Valor: " + valor);
		System.out.println("\n");
		System.out.println("##############################\n");
	}

	/** cambia de estilo **/
	private void cambiarEstilo(long idAlu) {

	}

	/** Registra una evidencia y reinicia la evidencia por llamarlo asi **/
	private void registrarLog(Evidencia e, HttpServletRequest httpRequest)
			throws AppException {

		e.formatearEvidencia();
		Evidencia e01 = new Evidencia(e);

		evidenciaService.insertar(e01, httpRequest);

	}

	private Material aplicarReglaMaterial(Evidencia e, HerramientasDrools hd,
			Long idTarea, Long idAlu) throws AppException {

		Material material = new Material();

		// iniciamos sesion y le tiramos el material
		hd.iniciarSession();
		Regla r = new Regla();
		r.setConcepto(e.getConcepto());
		r.setNivel(e.getNivel());
		r.setEstilo(e.getEstilo());
		r.setSecuenciaEjercicios(e.getSecuenciaEjercicio());
		r.setSecuenciaVideos(e.getSecuenciaMaterial());

		hd.ejecutarRegla(r);
		hd.terminarSession();

		/**Aqui abrimos una sesion**/
		SesionMaterial sesionMaterialAnterior = sesionMaterialService
				.sesionMaterialAnterior(idAlu, idTarea);
		
		
		// quitamos su id
		if (r.getMaterialAMostrar() != null) {
			String[] parts = r.getMaterialAMostrar().split("M");
			String part2 = parts[1]; // 654321
			Long idMaterial = new Long(part2);
			material = materialService.obtener(idMaterial);
			System.out.println("#####################################");
			System.out.println("Material de la regla");

			// Aqui se reemplaza por sesionAnterior
			try {
				
				sesionMaterialService.insertarMaterialVisto(
						sesionMaterialAnterior.getId(), sesionMaterialAnterior,
						material);
			} catch (AppException ex) {
				System.out
						.println("No se pudo obtener la sesionMaterial o simplemente no se pudo insertar sesion");
				ex.printStackTrace();
			}

		} else { // aun no esta hecho
			material = materialService.obtener(new Long(1));
			
			if (sesionMaterialAnterior.getListaMaterial() == null
					|| !sesionMaterialAnterior.getListaMaterial().contains(
							material))
				
				
			
			System.out.println("#####################################");
			System.out.println("Material al azar");
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

}
