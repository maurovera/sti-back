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

import model.Alumno;
import model.Asignatura;
import model.Concepto;
import model.Ejercicio;
import model.Sesion;
import model.Tarea;
import service.ConceptoService;
import service.SesionService;
import smile.Network;
import utils.AppException;

@Stateless
public class AdministracionAlumno {

	@Inject
	SesionService sesionService;
	
	@Inject
	ConceptoService conceptoService;

	final String dir = "/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/";

	/***
	 * Es el metodo get siguiente ejercicios Si viene el primer ejercicio llama
	 * id tarea, idAlumno, Ejercicio is null respuesta = 'respuesta'
	 ***/
	public String getSiguienteEjercicio(Tarea tarea, Alumno alumno,
			Ejercicio ejercicioAnterior, Long idAsignatura, String respuesta, Asignatura asig) {

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
		ejercicioSiguiente = seleccionUtilidadMax(tarea, alumno, idEjercicio, asig);

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
				sesionAnterior = sesionService.sesionAnterior(idAlumno,
						idTarea);
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
				/**Aqui no trae porque no esta asi en el modelo.***/
				List<Ejercicio> ejercicios = conceptoService.listaEjercicioConcepto(concepto);
				if (ejercicios != null) {
					System.out.println("Traje la lista de ejercicios concepto");
				} else {
					System.out
							.println("Quilombo. no traje la lista");
				}
				
				//List<Ejercicio> ejercicios = concepto.getListaEjercicio();
				// System.out.println("cantidad ejercicio: " +
				// ejercicios.size());
				for (Ejercicio ejercicioConcepto : ejercicios) {
					String nombreEjercicio = "E"
							+ ejercicioConcepto.getId();

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
				System.out.println("ejercicio: "+valor.getValue().toString()+"utilidad: " + valor.getKey().toString());
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

}