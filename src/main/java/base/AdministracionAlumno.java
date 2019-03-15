package base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Asignatura;
import model.Concepto;
import model.Ejercicio;
import model.Material;
import model.Sesion;
import model.Tarea;
import service.AlumnoService;
import service.AsignaturaService;
import service.ConceptoService;
import service.EjercicioService;
import service.SesionService;
import service.TareaService;
import smile.Network;
import utils.AppException;

@Stateless
public class AdministracionAlumno {

	@Inject
	SesionService sesionService;

	@Inject
	EjercicioService ejercicioService;

	@Inject
	TareaService tareaService;

	@Inject
	AsignaturaService asignaturaService;

	@Inject
	AlumnoService alumnoService;

	@Inject
	ConceptoService conceptoService;

	final String dir = "/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/";

	// ##############metodo de siguiente ejercicio para primertest###########
	/***
	 * Es el metodo get siguiente ejercicios Solo deberia de devolver el
	 * siguiente ejercicio
	 ***/
	public Ejercicio getSiguienteEjercicioPrimerTest(Tarea tarea,
			Alumno alumno, Long idAsignatura, Asignatura asig) {
		/**
		 * Ejercicio a ser devuelto
		 */
		Ejercicio ejercicioSiguiente = null;

		/**
		 * Tambien cambiamos la utilidad maxima
		 **/
		ejercicioSiguiente = seleccionUtilidadMaxPrimerTest(tarea, alumno, asig);

		return ejercicioSiguiente;

	}

	private Ejercicio seleccionUtilidadMaxPrimerTest(Tarea tarea,
			Alumno alumno, Asignatura asignatura) {

		try {
			Long idTarea = tarea.getId();
			Long idAlumno = alumno.getId();
			Long idAsignatura = asignatura.getId();

			/** Se obtiene la sesion anterior */
			Sesion sesionAnterior = null;
			try {
				sesionAnterior = sesionService
						.sesionAnterior(idAlumno, idTarea);
			} catch (AppException e) {
				System.out.println("No se pudo obtener la sesion anterior");
				e.printStackTrace();
			}
			/**
			 * Traemos la lista de conceptos por tarea
			 **/
			List<Concepto> listaConcepto = new ArrayList<Concepto>();
			listaConcepto = tarea.getListaConceptosTarea();
			if (listaConcepto != null) {
				System.out.println("Traje la lista de tareas sin drama");
			} else {
				System.out
						.println("Quilombo. No tiene conceptos. O no trae esta mierda");
			}

			/** inicializacion */

			Double utilidadMax = 0.0;
			Map<BigDecimal, List> hUtilidades = new HashMap<BigDecimal, List>();
			Ejercicio ejercicio = null;

			/**
			 * Por cada concepto trae un ejercicio Pondera que tanto le ayudara
			 * a la materia
			 **/
			for (Concepto concepto : listaConcepto) {

				/**
				 * lee la red del alumno y actualiza las probabilidades
				 **/
				Network net1 = new Network();
				String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
						+ idAsignatura + ".xdsl";
				net1.readFile(dir + nombreRed);
				net1.updateBeliefs();
				String nombreConcepto = concepto.getNombre();

				/** llama a asignatura y quita sus probabilidades */
				double[] values = net1.getNodeValue(asignatura.getNombre());
				BigDecimal pC1 = new BigDecimal(String.valueOf(values[1])); // P(C=1)
				BigDecimal pC0 = new BigDecimal(String.valueOf(values[0])); // P(C=0)

				int cont = 0;

				/** Trae la lista de ejercicios por concepto. ***/
				List<Ejercicio> ejercicios = conceptoService
						.listaEjercicioConcepto(concepto);
				if (ejercicios != null) {
					System.out
							.println("Traje la lista de ejercicios por concepto");
				} else {
					System.out
							.println("Quilombo. no traje la lista de ejercicios por concepto");
				}

				/**
				 * Por cada ejercicio quita el ponderado de cuanto ayuda a la
				 * asignatura
				 **/
				for (Ejercicio ejercicioConcepto : ejercicios) {

					String nombreEjercicio = "E" + ejercicioConcepto.getId();
					values = net1.getNodeValue(nombreEjercicio);
					Double pE1 = values[1]; // P(E=1)
					Double pE0 = values[0]; // P(E=0)

					// ingresa las evidencias para conoce
					net1.setEvidence(nombreConcepto, "Conoce");
					net1.updateBeliefs();
					values = net1.getNodeValue(nombreEjercicio);
					BigDecimal pE1C1 = new BigDecimal(String.valueOf(values[1])); // P(E=1/C=1)
					BigDecimal pAuxi1 = new BigDecimal(pE1C1.toString());// -pE1;
					BigDecimal utilidadParcial1 = pAuxi1.multiply(pC1);

					// borra la evidencia
					net1.clearEvidence(nombreConcepto);
					net1.updateBeliefs();

					// ingresa la evidencia para no conoce
					net1.setEvidence(nombreConcepto, "No_conoce");
					net1.updateBeliefs();
					values = net1.getNodeValue(nombreEjercicio);
					Double pE0C0 = values[0]; // P(E=0/C=0)
					BigDecimal pAuxi0 = new BigDecimal(pE0C0.toString());// -pE0;
																			// //
																			// P(E=0)
					BigDecimal utilidadParcial2 = pAuxi0.multiply(pC0);
					BigDecimal utilidadMaxParcial = utilidadParcial1
							.add(utilidadParcial2);

					/*
					 * if(utilidadMaxParcial > utilidadMax) { utilidadMax =
					 * utilidadMaxParcial; ejercicio = ejercicioConcepto; } else
					 * if (utilidadMaxParcial == utilidadMax) { Random rnd = new
					 * Random(); int eleccion = rnd.nextInt(2); if (eleccion ==
					 * 1) { ejercicio = ejercicioConcepto; } }
					 */
					utilidadMaxParcial = utilidadMaxParcial.setScale(10,
							utilidadMaxParcial.ROUND_HALF_UP);
					// System.out.println("utilidadMaxParcial: " +
					// utilidadMaxParcial);
					List utilidades = hUtilidades.get(utilidadMaxParcial);
					// System.out.println("utilidades" + hUtilidades);
					if (utilidades == null)
						utilidades = new ArrayList();

					/**
					 * Si la lista de ejercicios es nula o la sesion no contiene
					 * al ejercicio. guardar
					 **/
					if (sesionAnterior.getListaEjercicio() == null
							|| !sesionAnterior.getListaEjercicio().contains(
									ejercicioConcepto))
						utilidades.add(ejercicioConcepto);

					/** Si la utilidad es mayor a cero guardar **/
					if (utilidades.size() > 0)
						hUtilidades.put(utilidadMaxParcial, utilidades);

				}
			}

			TreeMap<BigDecimal, List> tUtilidades = new TreeMap<BigDecimal, List>(
					hUtilidades);
			TreeMap<BigDecimal, List> tree = (TreeMap<BigDecimal, List>) tUtilidades
					.clone();

			for (int i = 0; i < tUtilidades.size(); i++) {

				Map.Entry<BigDecimal, List> valor = tree.pollLastEntry();
				System.out.println("ejercicio: " + valor.getValue().toString()
						+ "utilidad: " + valor.getKey().toString());
				List<Ejercicio> ejercicioLista = valor.getValue();
			}

			Map.Entry<BigDecimal, List> valor;
			Map.Entry<BigDecimal, List> primerValor;
			valor = tUtilidades.pollLastEntry();

			List lista = valor.getValue();
			Random rnd = new Random();
			int eleccion = rnd.nextInt(lista.size());
			return (Ejercicio) lista.get(eleccion);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ########################################################################
	/***
	 * Es el metodo get siguiente ejercicios Si viene el primer ejercicio llama
	 * id tarea, idAlumno, Ejercicio is null respuesta = 'respuesta'
	 ***/
	public String getSiguienteEjercicio(Tarea tarea, Alumno alumno,
			Ejercicio ejercicioAnterior, Long idAsignatura, String respuesta,
			Asignatura asig) {

		// Tarea tarea = em.find(Tarea.class, idTarea);
		// Alumno alumno = em.find(Alumno.class, idAlumno);
		//
		/*** Segunda llamada. Seria ya cuando es el siguiente ejercicio. **/
		Long idEjercicio = null;
		String ejercicioElement = new String();
		String enunciado = new String();
		Ejercicio ejercicioSiguiente = null;

		if (ejercicioAnterior != null) {

			idEjercicio = ejercicioAnterior.getId();
			responderEjercicio(ejercicioAnterior, respuesta, alumno,
					idAsignatura, tarea);
		}

		// Ejercicio ejercicio = seleccionAleatoria(tarea);
		ejercicioSiguiente = seleccionUtilidadMax(tarea, alumno, idEjercicio,
				asig);

		if (ejercicioSiguiente != null) {
			enunciado = toASCII(ejercicioSiguiente.getEnunciado());
			ejercicioElement = ejercicioSiguiente.getId().toString() + "#"
					+ enunciado;

		} else {
			ejercicioElement = "No se quito ningun ejercicio";
		}

		return ejercicioElement;

	}

	/***
	 * Es la funcion que se utiliza como servicio para responder las preguntas.
	 * Al responder un ejercicio se guarda en la sesion anterior
	 * 
	 * @throws AppException
	 * @return boolean si responde bien o mal.
	 ***/
	public Boolean responderEjercicioServicio(Long idEjercicio,
			String respuesta, Long idAlumno, Long idAsignatura, Long idTarea, HttpServletRequest httpRequest) {

		Ejercicio ejercicio = null;
		try {
			ejercicio = ejercicioService.obtener(idEjercicio);
		} catch (AppException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out
					.println("no se pudo obtener el ejercicio para responder el ejercicio.");
		}
		// Alumno alumno = alumnoService.obtener(idAlumno);

		Boolean retorno = false;

		Network net1 = new Network();
		// Long idAlumno = alumno.getId();
		// Long idEjercicio = ejercicio.getId();
		// Long idTarea = tarea.getId();

		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";
		net1.readFile(dir + nombreRed);
		net1.updateBeliefs();
		String nombreEjercicio = "E" + idEjercicio;
		/**
		 * Aca se envia la respuesta. Si es correcto o no.
		 ***/
		if (respuesta.equals(ejercicio.getRespuesta().getDescripcion())) {
			net1.setEvidence(nombreEjercicio, "Correcto");
			System.out.println("Respuesta correcta");
			retorno = true;
		} else {
			net1.setEvidence(nombreEjercicio, "Incorrecto");
			retorno = false;
			System.out.println("Respuesta incorrecta");
		}
		net1.updateBeliefs();

		List<Concepto> listaConceptos = ejercicio.getListaConceptos();
		if (listaConceptos != null)
			System.out
					.println("hay conceptos. No es nulo. Numero 89 AdministracionAlumno");
		else
			System.out
					.println("No hay concepto. Es nulo la lista de conceptos. ");

		/**
		 * Observacion importante. Porque todos los conceptos del arbol No
		 * deberia ser de la tarea nomas
		 ***/
		for (Concepto concepto : listaConceptos) {
			String nombreConcepto = concepto.getNombre();
			double[] probCalc = net1.getNodeValue(nombreConcepto);
			// System.out.println("proba " + probCalc.toString());
			net1.setNodeDefinition(nombreConcepto, probCalc);
		}

		net1.clearEvidence(nombreEjercicio);
		net1.updateBeliefs();
		net1.writeFile(dir + nombreRed);

		// Aqui se reemplaza por sesionAnterior
		try {
			Sesion sesionAnterior = sesionService.sesionAnterior(idAlumno,
					idTarea);
			sesionService.insertarEjercicioResuelto(sesionAnterior.getId(),
					sesionAnterior, ejercicio);
			/**Ademas de insertar el ejercicio anterior se inserta la cantidad 
			 * de ejercicios que resuelve. en este caso se le suma uno**/
			Integer cantidad = sesionAnterior.getcantidadEjerciciosResueltos() + 1;
			sesionAnterior.setCantidadEjerciciosResueltos(cantidad);
			sesionService.modificar(sesionAnterior.getId(), sesionAnterior, httpRequest);
		} catch (AppException e) {
			System.out
					.println("No se pudo obtener la sesion o simplemente no se pudo insertar sesion");
			e.printStackTrace();
		}

		return retorno;

	}

	/***
	 * Al responder un ejercicio se guarda en la sesion anterior
	 * 
	 * @throws AppException
	 ***/
	private void responderEjercicio(Ejercicio ejercicio, String respuesta,
			Alumno alumno, Long idAsignatura, Tarea tarea) {

		// Ejercicio ejercicio = em.find(Ejercicio.class, idEjercicio);
		Network net1 = new Network();
		Long idAlumno = alumno.getId();
		Long idEjercicio = ejercicio.getId();
		Long idTarea = tarea.getId();

		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";
		net1.readFile(dir + nombreRed);
		net1.updateBeliefs();
		String nombreEjercicio = "E" + idEjercicio;
		/**
		 * Aca se envia la respuesta. Si es correcto o no.
		 ***/
		if (respuesta.equals(ejercicio.getRespuesta().getDescripcion())) {
			net1.setEvidence(nombreEjercicio, "Correcto");
		} else {
			net1.setEvidence(nombreEjercicio, "Incorrecto");
		}
		net1.updateBeliefs();

		List<Concepto> listaConceptos = ejercicio.getListaConceptos();
		if (listaConceptos != null)
			System.out
					.println("hay conceptos. No es nulo. Numero 89 AdministracionAlumno");
		else
			System.out
					.println("No hay concepto. Es nulo la lista de conceptos. ");

		/**
		 * Observacion importante. Porque todos los conceptos del arbol No
		 * deberia ser de la tarea nomas
		 ***/
		for (Concepto concepto : listaConceptos) {
			String nombreConcepto = concepto.getNombre();
			double[] probCalc = net1.getNodeValue(nombreConcepto);
			// System.out.println("proba " + probCalc.toString());
			net1.setNodeDefinition(nombreConcepto, probCalc);
		}

		net1.clearEvidence(nombreEjercicio);
		net1.updateBeliefs();
		net1.writeFile(dir + nombreRed);

		// Aqui se reemplaza por sesionAnterior
		try {
			Sesion sesionAnterior = sesionService.sesionAnterior(idAlumno,
					idTarea);
			sesionService.insertarEjercicioResuelto(sesionAnterior.getId(),
					sesionAnterior, ejercicio);
		} catch (AppException e) {
			System.out
					.println("No se pudo obtener la sesion o simplemente no se pudo insertar sesion");
			e.printStackTrace();
		}

	}

	private Ejercicio seleccionUtilidadMax(Tarea tarea, Alumno alumno,
			Long idEjercicioAnterior, Asignatura asignatura) {

		try {
			Long idTarea = tarea.getId();
			Long idAlumno = alumno.getId();
			Long idAsignatura = asignatura.getId();

			// para obtener la sesion anterior.
			/**
			 * Query query = em .createQuery(
			 * "Select s from Sesion s where s.alumno.idAlumno = :alumno and s.tarea.idTarea = :tarea  order by s.idSesion desc"
			 * ); query.setParameter("alumno", alumno.getIdAlumno());
			 * query.setParameter("tarea", tarea.getIdTarea());
			 * query.setMaxResults(1); Sesion sesionAnterior = (Sesion)
			 * query.getSingleResult();
			 */
			Sesion sesionAnterior = null;
			try {
				sesionAnterior = sesionService
						.sesionAnterior(idAlumno, idTarea);
			} catch (AppException e) {
				System.out.println("No se pudo obtener la sesion");
				e.printStackTrace();
			}

			/*
			 * List<Concepto> conceptoList = em .createQuery(
			 * "Select c from Concepto c inner join c.tareaList t where t = :tarea"
			 * ) .setParameter("tarea", idTarea).getResultList();
			 */
			// probemos con esto
			List<Concepto> listaConcepto = new ArrayList<Concepto>();
			listaConcepto = tarea.getListaConceptosTarea();
			if (listaConcepto != null) {
				System.out.println("Traje la lista de tareas sin drama");
			} else {
				System.out
						.println("Quilombo. No tiene conceptos. O no trae esta mierda");
			}

			// inicializacion

			Double utilidadMax = 0.0;
			Map<BigDecimal, List> hUtilidades = new HashMap<BigDecimal, List>();
			Ejercicio ejercicio = null;

			// System.out.println("cantidad concepto:  " + conceptoList.size() +
			// " de la tarea: " + tarea.getDescripcion());
			for (Concepto concepto : listaConcepto) {
				// System.out.println("concepto " + concepto.getNombre());
				Network net1 = new Network();
				String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
						+ idAsignatura + ".xdsl";
				// System.out.println("seleccionUtilidadMax - /home/mauro/proyectos/tesis/ejercitarMau/src/main/redes/"
				// + nombreRed);

				net1.readFile(dir + nombreRed);
				net1.updateBeliefs();
				String nombreConcepto = concepto.getNombre();
				// double [] values = net1.getNodeValue(nombreConcepto);

				// llama a asignatura
				double[] values = net1.getNodeValue(asignatura.getNombre());

				// System.out.println("values");

				// System.out.println(" pC0:  " + values[0]);
				// System.out.println(" pC1:  " + values[1]);

				BigDecimal pC1 = new BigDecimal(String.valueOf(values[1])); // P(C=1)

				BigDecimal pC0 = new BigDecimal(String.valueOf(values[0])); // P(C=0)

				int cont = 0;
				/** Aqui no trae porque no esta asi en el modelo. ***/
				List<Ejercicio> ejercicios = conceptoService
						.listaEjercicioConcepto(concepto);
				if (ejercicios != null) {
					System.out.println("Traje la lista de ejercicios concepto");
				} else {
					System.out.println("Quilombo. no traje la lista");
				}

				// List<Ejercicio> ejercicios = concepto.getListaEjercicio();
				// System.out.println("cantidad ejercicio: " +
				// ejercicios.size());
				for (Ejercicio ejercicioConcepto : ejercicios) {
					String nombreEjercicio = "E" + ejercicioConcepto.getId();

					values = net1.getNodeValue(nombreEjercicio);
					Double pE1 = values[1]; // P(E=1)
					Double pE0 = values[0]; // P(E=0)

					// System.out.println("nombreEjercicio:  " +
					// nombreEjercicio);
					net1.setEvidence(nombreConcepto, "Conoce");
					net1.updateBeliefs();
					values = net1.getNodeValue(nombreEjercicio);
					// System.out.println("values " + values);
					BigDecimal pE1C1 = new BigDecimal(String.valueOf(values[1])); // P(E=1/C=1)
					// System.out.println("pE1C1:  " + pE1C1);
					BigDecimal pAuxi1 = new BigDecimal(pE1C1.toString());// -pE1;
																			// //P(E=1/C=1)
																			// -
																			// P(E=1)
					BigDecimal utilidadParcial1 = pAuxi1.multiply(pC1);
					// System.out.println("utilidadParcial1:  " +
					// utilidadParcial1);
					net1.clearEvidence(nombreConcepto);
					net1.updateBeliefs();
					net1.setEvidence(nombreConcepto, "No_conoce");
					net1.updateBeliefs();
					values = net1.getNodeValue(nombreEjercicio);
					// System.out.println("values_ " + values);

					Double pE0C0 = values[0]; // P(E=0/C=0)
					// System.out.println("pE0C0:  " + pE0C0.toString());

					BigDecimal pAuxi0 = new BigDecimal(pE0C0.toString());// -pE0;
																			// //P(E=0/C=0)
																			// -
																			// P(E=0)
					BigDecimal utilidadParcial2 = pAuxi0.multiply(pC0);
					// System.out.println("utilidadParcial2:  " +
					// utilidadParcial2);
					BigDecimal utilidadMaxParcial = utilidadParcial1
							.add(utilidadParcial2);

					/*
					 * if(utilidadMaxParcial > utilidadMax) { utilidadMax =
					 * utilidadMaxParcial; ejercicio = ejercicioConcepto; } else
					 * if (utilidadMaxParcial == utilidadMax) { Random rnd = new
					 * Random(); int eleccion = rnd.nextInt(2); if (eleccion ==
					 * 1) { ejercicio = ejercicioConcepto; } }
					 */
					utilidadMaxParcial = utilidadMaxParcial.setScale(10,
							utilidadMaxParcial.ROUND_HALF_UP);
					// System.out.println("utilidadMaxParcial: " +
					// utilidadMaxParcial);
					List utilidades = hUtilidades.get(utilidadMaxParcial);
					// System.out.println("utilidades" + hUtilidades);
					if (utilidades == null)
						utilidades = new ArrayList();

					if (sesionAnterior.getListaEjercicio() == null
							|| !sesionAnterior.getListaEjercicio().contains(
									ejercicioConcepto))
						utilidades.add(ejercicioConcepto);
					// System.out.println("sesion" +
					// ejercicioConcepto.toString());
					if (utilidades.size() > 0)
						hUtilidades.put(utilidadMaxParcial, utilidades);
					// System.out.println("utilidadMax: " + utilidadMax);
					// System.out.println("utilidades.size()" +
					// utilidades.size());
				}
			}

			// System.out.println("obtener la sesion anterior ");
			TreeMap<BigDecimal, List> tUtilidades = new TreeMap<BigDecimal, List>(
					hUtilidades);
			// System.out.println("treeMap");
			TreeMap<BigDecimal, List> tree = (TreeMap<BigDecimal, List>) tUtilidades
					.clone();

			for (int i = 0; i < tUtilidades.size(); i++) {

				Map.Entry<BigDecimal, List> valor = tree.pollLastEntry();
				System.out.println("ejercicio: " + valor.getValue().toString()
						+ "utilidad: " + valor.getKey().toString());
				List<Ejercicio> ejercicioLista = valor.getValue();
				/*
				 * for( Ejercicio ejer : ejercicioLista){
				 * System.out.println(ejer.getIdEjercicio() + " - " +
				 * ejer.getEnunciado() ); }
				 */
			}

			// int i = tUtilidades.size();
			Map.Entry<BigDecimal, List> valor;
			Map.Entry<BigDecimal, List> primerValor;
			valor = tUtilidades.pollLastEntry();
			// primerValor = valor;
			// System.out.println("valor " + valor);

			List lista = valor.getValue();
			Random rnd = new Random();
			int eleccion = rnd.nextInt(lista.size());
			// System.out.println("radom");
			return (Ejercicio) lista.get(eleccion);

			/*
			 * while(true) { if (valor == null) { List lista =
			 * primerValor.getValue(); Random rnd = new Random(); int eleccion =
			 * rnd.nextInt(lista.size()); return (Ejercicio)
			 * lista.get(eleccion); }
			 * 
			 * List lista = valor.getValue();
			 * 
			 * if(sesionAnterior.getEjercicioList()== null ||
			 * !sesionAnterior.getEjercicioList().contains(e)) { return e; }
			 * valor = tUtilidades.pollLastEntry(); }
			 */

			// return ejercicio;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Función que elimina acentos y caracteres especiales de una cadena de
	 * texto.
	 * 
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public String toASCII(String input) {
		// Cadena de caracteres original a sustituir.
		String original = "��������������u�������������������";
		// Cadena de caracteres ASCII que reemplazar�n los originales.
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = input;
		for (int i = 0; i < original.length(); i++) {
			// Reemplazamos los caracteres especiales.
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}// for i
		System.out.println(output);
		return output;
	}// remove1

	// #####################FUNCIONES PARA EL TUTOR####################3
	/** Se encarga de abrir la redBayesiana **/
	public Network abrirRed(Long alumno, Long asignatura) {

		Network net1 = new Network();
		String nombreRed = "red_alumno_" + alumno + "_asignatura_" + asignatura
				+ ".xdsl";
		// System.out.println("seleccionUtilidadMax - /home/mauro/proyectos/tesis/ejercitarMau/src/main/redes/"
		// + nombreRed);

		net1.readFile(dir + nombreRed);
		net1.updateBeliefs();

		return net1;

	}

	/**
	 * @param la
	 *            red bayesiana y la lista de conceptos
	 * 
	 *            El metodo devuelve una lista de nombre de conceptos que el
	 *            alumno no conoce. El criterio para determinar si conoce o no
	 *            un concepto es si la probabilidad de conocer el concepto en la
	 *            red es mayor o igual a un nivel de concepto fijado por el
	 *            profesor por concepto.
	 * 
	 **/
	public List<String> listarConceptosDesconocidos(Network red,
			List<String> listaConceptos) {

		// Network net = new Network();
		// net.readFile(red);
		// net.updateBeliefs();

		Double valorPurete = new Double(0.75);
		List<String> devolver = new ArrayList<String>();
		System.out.println("\nFuncion listar\n");
		for (String nombre : listaConceptos) {

			double[] valor = red.getNodeValue(nombre);
			double conoce = valor[1];
			if (conoce >= valorPurete) {
				System.out.println("Conoce este concepto: " + nombre
						+ " valor: " + conoce);
			} else {
				System.out.println("No conoce este concepto: " + nombre
						+ " valor: " + conoce);
				devolver.add(nombre);
			}
		}
		return devolver;
	}

	/**
	 * Metodo que devuelve un ejercicio Este ejercicio es el que tiene mayor
	 * utilidad para el concepto
	 **/
	private Ejercicio seleccionUtilidadMaxPorConcepto(Long idTarea,
			Long idAlumno, Long idAsignatura, Concepto concepto) {

		try {

			/*** para obtener la sesion anterior. ***/

			Sesion sesionAnterior = null;
			try {
				sesionAnterior = sesionService
						.sesionAnterior(idAlumno, idTarea);
				System.out.println("tamaño ejercicios: "
						+ sesionAnterior.getListaEjercicio().size());
			} catch (AppException e) {
				System.out.println("No se pudo obtener la sesion");
				e.printStackTrace();
			}

			/*** inicializacion ***/

			Double utilidadMax = 0.0;
			Map<BigDecimal, List> hUtilidades = new HashMap<BigDecimal, List>();
			Ejercicio ejercicio = null;

			/*** Abre el archivo de la red bayesiana **/
			Network net1 = new Network();
			String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
					+ idAsignatura + ".xdsl";
			net1.readFile(dir + nombreRed);
			net1.updateBeliefs();

			/***
			 * Quita las probabilidades del concepto P(c=1) conoce P(c=0)
			 * no_conoce
			 ***/
			String nombreConcepto = concepto.getNombre();
			double[] values = net1.getNodeValue(nombreConcepto);
			BigDecimal pC1 = new BigDecimal(String.valueOf(values[1])); // P(C=1)
			BigDecimal pC0 = new BigDecimal(String.valueOf(values[0])); // P(C=0)

			int cont = 0;
			/**
			 * Traemos todo los ejercicios relacionados con el concepto.
			 ***/
			List<Ejercicio> ejercicios = conceptoService
					.listaEjercicioConcepto(concepto);
			if (ejercicios != null) {
				System.out.println("Traje la lista de ejercicios concepto");
			} else {
				System.out.println("Quilombo. no traje la lista");
			}

			/**
			 * For por la lista de ejercicios Vamos calculando su utilidad
			 ***/
			for (Ejercicio ejercicioConcepto : ejercicios) {
				String nombreEjercicio = "E" + ejercicioConcepto.getId();

				/** Probabilidades de un ejercicio **/
				values = net1.getNodeValue(nombreEjercicio);
				Double pE1 = values[1]; // P(E=1)
				Double pE0 = values[0]; // P(E=0)

				net1.setEvidence(nombreConcepto, "Conoce");
				net1.updateBeliefs();
				values = net1.getNodeValue(nombreEjercicio);
				BigDecimal pE1C1 = new BigDecimal(String.valueOf(values[1])); // P(E=1/C=1)
				BigDecimal pAuxi1 = new BigDecimal(pE1C1.toString());// -pE1;
																		// //P(E=1/C=1)
																		// -
																		// P(E=1)
				BigDecimal utilidadParcial1 = pAuxi1.multiply(pC1);
				net1.clearEvidence(nombreConcepto);
				net1.updateBeliefs();

				net1.setEvidence(nombreConcepto, "No_conoce");
				net1.updateBeliefs();
				values = net1.getNodeValue(nombreEjercicio);
				Double pE0C0 = values[0]; // P(E=0/C=0)
				BigDecimal pAuxi0 = new BigDecimal(pE0C0.toString());// -pE0;
																		// //P(E=0/C=0)
																		// -
																		// P(E=0)
				BigDecimal utilidadParcial2 = pAuxi0.multiply(pC0);
				BigDecimal utilidadMaxParcial = utilidadParcial1
						.add(utilidadParcial2);

				utilidadMaxParcial = utilidadMaxParcial.setScale(10,
						utilidadMaxParcial.ROUND_HALF_UP);
				List utilidades = hUtilidades.get(utilidadMaxParcial);

				if (utilidades == null)
					utilidades = new ArrayList();

				if (sesionAnterior.getListaEjercicio() != null
						&& !sesionAnterior.getListaEjercicio().contains(
								ejercicioConcepto)) {

					utilidades.add(ejercicioConcepto);
				}
				if (utilidades.size() > 0)
					hUtilidades.put(utilidadMaxParcial, utilidades);

			}

			TreeMap<BigDecimal, List> tUtilidades = new TreeMap<BigDecimal, List>(
					hUtilidades);
			TreeMap<BigDecimal, List> tree = (TreeMap<BigDecimal, List>) tUtilidades
					.clone();

			for (int i = 0; i < tUtilidades.size(); i++) {

				Map.Entry<BigDecimal, List> valor = tree.pollLastEntry();
				System.out.println("ejercicio: " + valor.getValue().toString()
						+ "utilidad: " + valor.getKey().toString());
				List<Ejercicio> ejercicioLista = valor.getValue();
			}

			Map.Entry<BigDecimal, List> valor;
			Map.Entry<BigDecimal, List> primerValor;
			valor = tUtilidades.pollLastEntry();

			List lista = valor.getValue();
			Random rnd = new Random();
			int eleccion = rnd.nextInt(lista.size());

			return (Ejercicio) lista.get(eleccion);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * Este metodo recibe tarea: es un id de la tarea. Sirve para la sesion
	 * alumno: id del alumno. Sirve para la sesion idAsignatura: sirve para
	 * abrir la redBayesiana concepto: Es el concepto que se necesita para
	 * buscar el siguiente ejercicio
	 ***/
	public Ejercicio getSiguienteEjercicioPorConcepto(Long tarea, Long alumno,
			Long idAsignatura, Concepto concepto) {

		Ejercicio ejercicioSiguiente = null;

		/**
		 * Ejercicio seleccionado por la utilidad maxima que el ejercicio ofrece
		 * para el concepto seleccionado
		 ***/
		ejercicioSiguiente = seleccionUtilidadMaxPorConcepto(tarea, alumno,
				idAsignatura, concepto);

		if (ejercicioSiguiente == null)
			System.out.println("No hay ejercicio Siguiente");

		return ejercicioSiguiente;

	}

	/***
	 * Al responder un ejercicio del tutor se guarda en la sesion anterior Se
	 * actualiza el conocimiento del concepto o los conceptos que componen el
	 * ejercicio y del alumno en general.
	 * 
	 * @throws AppException
	 ***/
	public Boolean responderEjercicioConcepto(Ejercicio ejercicio,
			String respuesta, Long idAlumno, Long idAsignatura, Long idTarea) {

		Boolean retorno = false;
		/**
		 * Se abre el archivo que corresponde
		 **/
		Network net1 = new Network();
		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";
		net1.readFile(dir + nombreRed);
		net1.updateBeliefs();

		/**
		 * Se quita el nombre del ejercicio dentro de la red bayesiana
		 **/
		Long idEjercicio = ejercicio.getId();
		String nombreEjercicio = "E" + idEjercicio;

		/**
		 * Aca se envia la respuesta. Si es correcto o no.
		 ***/
		if (respuesta.equals(ejercicio.getRespuesta().getDescripcion())) {
			net1.setEvidence(nombreEjercicio, "Correcto");
			retorno = true;
		} else {
			net1.setEvidence(nombreEjercicio, "Incorrecto");
			retorno = false;
		}
		net1.updateBeliefs();

		List<Concepto> listaConceptos = ejercicio.getListaConceptos();
		if (listaConceptos != null)
			System.out.println("hay conceptos. No es nulo");
		else
			System.out
					.println("No hay concepto. Es nulo la lista de conceptos. ");

		/**
		 * Observacion importante. Actualizacion de lista de conceptos del
		 * ejercicio evaluado
		 ***/
		for (Concepto concepto : listaConceptos) {
			String nombreConcepto = concepto.getNombre();
			double[] probCalc = net1.getNodeValue(nombreConcepto);
			// System.out.println("proba " + probCalc.toString());
			net1.setNodeDefinition(nombreConcepto, probCalc);
		}

		net1.clearEvidence(nombreEjercicio);
		net1.updateBeliefs();
		net1.writeFile(dir + nombreRed);

		/**
		 * Se registra en la sesion el ejercicio evaluado
		 **/
		try {
			Sesion sesionAnterior = sesionService.sesionAnterior(idAlumno,
					idTarea);
			sesionService.insertarEjercicioResuelto(sesionAnterior.getId(),
					sesionAnterior, ejercicio);
		} catch (AppException e) {
			System.out
					.println("No se pudo obtener la sesion o simplemente no se pudo insertar sesion");
			e.printStackTrace();
		}

		return retorno;

	}

	public Material aplicarReglaMaterial(Alumno alumno, Concepto concepto) {

		Material material = new Material();
		/** Tipo si no encuentro reglas tirar uno al azar ***/

		/** Si encuentro tirar. **/

		return material;

	}

}
