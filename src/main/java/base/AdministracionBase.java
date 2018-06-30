package base;

import java.util.List;

import javax.ejb.Stateless;

import model.Asignatura;
import model.Concepto;
import model.Ejercicio;
import model.Tema;
import smile.Network;
import utils.Separador;

@Stateless
public class AdministracionBase {

	Separador sp = new Separador();

	public AdministracionBase() {

	}

	/***
	 * Crear. Crea una red con el nombre de la asignatura. Recibe una asignatura
	 * Modificar. Para editar no necesita modificar la red. Eliminar. No hace
	 * falta eliminar la asignatura. Solo queda colgado el archivo en redes. en
	 * todo caso deberia ver como eliminar el archivo
	 * **/
	public void agregarAsignaturaRed(Asignatura asignatura) {
		System.out.println("agregarAsignaturaRed");
		Network net = new Network();
		String titulo = sp.convertirEspacioToGuion(asignatura.getNombre());
		// //cambiar formato nodo
		net.addNode(Network.NodeType.Cpt, titulo);
		net.setOutcomeId(titulo, 0, "No_conoce");
		net.setOutcomeId(titulo, 1, "Conoce");
		String nombreRed = "red_asignatura_" + asignatura.getId() + ".xdsl";
		System.out
				.println("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
						+ nombreRed);
		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
	}

	/**
	 * Agrega un tema a la red bayesiana.
	 **/
	public void agregarTemaRed(Tema tema) {
		System.out.println("agregarTemaRed/////////////");
		// operaciones sobre la red bayesiana con smile
		System.out.println(tema.getAsignatura().getId());
		String nombreRed = "red_asignatura_" + tema.getAsignatura().getId()
				+ ".xdsl";
		System.out
				.println("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
						+ nombreRed);
		Network net = new Network();
		net.readFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);

		// agregar nodo tema
		// cambiar formato nodo
		String titulo = sp.convertirEspacioToGuion(tema.getNombre());

		net.addNode(Network.NodeType.Cpt, titulo);
		net.setOutcomeId(titulo, 0, "No_conoce");
		net.setOutcomeId(titulo, 1, "Conoce");

		// agregar los arcos

		net.addArc(titulo,
				sp.convertirEspacioToGuion(tema.getAsignatura().getNombre()));

		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
	}

	/**
	 * Modifica un tema a la red bayesiana.
	 **/
	public void modificarTemaRed(Tema tema, String titulo) {
		// operaciones sobre la red bayesiana con smile
		String nombreRed = "red_asignatura_" + tema.getAsignatura().getId()
				+ ".xdsl";

		Network net = new Network();
		net.readFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);

		String tituloNuevo = sp.convertirEspacioToGuion(tema.getNombre());

		// cambiar formato nodo
		// modifica el nombre
		net.setNodeId(sp.convertirEspacioToGuion(titulo), tituloNuevo);
		net.setNodeName(tituloNuevo, tituloNuevo);

		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
	}

	/**
	 * Elimina un tema a la red bayesiana.
	 **/
	public void eliminarTemaRed(Tema tema) {
		// TODO Auto-generated method stub
		String nombreRed = "red_asignatura_" + tema.getAsignatura().getId()
				+ ".xdsl";
		Network net = new Network();
		net.readFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
		// cambiar formato nombre
		String nom = sp.convertirEspacioToGuion(tema.getNombre());
		net.deleteNode(nom);
		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
	}

	/**
	 * Se agrega un concepto a la red
	 **/
	public void agregarConceptoRed(Concepto concepto, Long idAsignatura) {
		// operaciones sobre la red bayesiana con smile
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		String titulo;
		String tituloTema;
		System.out
				.println("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
						+ nombreRed);
		Network net = new Network();
		net.readFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);

		// agregar nodo concepto
		// cambiar formato nodo
		titulo = sp.convertirEspacioToGuion(concepto.getNombre());
		net.addNode(Network.NodeType.Cpt, titulo);
		net.setOutcomeId(titulo, 0, "No_conoce");
		net.setOutcomeId(titulo, 1, "Conoce");

		// agregar los arcos
		// cambiar formato nodo
		tituloTema = sp.convertirEspacioToGuion(concepto.getTema().getNombre());

		net.addArc(titulo, tituloTema);

		// definir probabilidades
		/** Define valor a priori y tambien 1 menos lo que conoce */
		double[] conceptoDef = new double[2];
		conceptoDef[1] = concepto.getApriori();
		conceptoDef[0] = 1 - conceptoDef[1];
		// cambiar formato nodo
		net.setNodeDefinition(titulo, conceptoDef);
		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
	}

	/**
	 * Modificar un concepto en la red
	 ***/
	public void modificarConceptoRed(Concepto concepto, String tituloViejo,
			Long idAsignatura) {
		// operaciones sobre la red bayesiana con smile
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";

		System.out
				.println("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
						+ nombreRed);
		Network net = new Network();
		net.readFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);

		// se modifica el nombre del concepto
		String titulo = sp.convertirEspacioToGuion(concepto.getNombre());

		// cambiar formato nodo
		System.out.println("titulo/" + titulo);
		System.out.println("tituloViejo/" + tituloViejo);

		net.setNodeId(sp.convertirEspacioToGuion(tituloViejo), titulo);
		net.setNodeName(titulo, titulo);

		// cambiar formato nodo
		// se modifica la probabilidad
		double[] conceptoDef = new double[2];
		conceptoDef[1] = concepto.getApriori();
		conceptoDef[0] = 1 - conceptoDef[1];
		net.setNodeDefinition(titulo, conceptoDef);

		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
	}

	/** Eliminar concepto de la red bayesiana */
	public void eliminarConceptoRed(Concepto concepto, Long idAsignatura) {
		// TODO Auto-generated method stub\
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
		// cambiar formato nodo
		String nom = sp.convertirEspacioToGuion(concepto.getNombre());

		net.deleteNode(nom);
		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);

	}

	/**
	 * Agregar Ejercicio a la red bayesiana
	 **/
	private void agregarEjercicioRed(Ejercicio ejercicio, Long idAsignatura) {
		System.out.println("Agregar Ejercicio Red");
		// operaciones sobre la red bayesiana con smile
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		System.out
				.println("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
						+ nombreRed);
		Network net = new Network();
		net.readFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);

		// agregar nodo ejercicio
		String titulo = "E" + Long.toString(ejercicio.getId());
		net.addNode(Network.NodeType.Cpt, titulo);
		net.setOutcomeId(titulo, 0, "Incorrecto");
		net.setOutcomeId(titulo, 1, "Correcto");

		// agregar los arcos
		List<Concepto> conceptoList = ejercicio.getListaConceptos();
		// Se verfica que tenga conceptos asociados
		if (conceptoList != null) {
			for (Concepto concepto : conceptoList) {
				String conceptoRed = sp.convertirEspacioToGuion(concepto
						.getNombre());

				net.addArc(conceptoRed, titulo);
			}
		}

		// definir probabilidades condicionales
		double[] ejercicioDef = calcularProbabilidadesCCI(ejercicio);
		net.setNodeDefinition(titulo, ejercicioDef);

		net.writeFile("/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/"
				+ nombreRed);
	}

	/***
	 * Calcula las probabilidades condicionales.
	 **/
	public double[] calcularProbabilidadesCCI(Ejercicio ejercicio) {
		System.out.println("CalcularProbabilidades CCI");
		// TODO Auto-generated method stub
		Double dimension = Math.pow(2, ejercicio.getListaConceptos().size());
		double[] ejercicioDef = new double[dimension.intValue() * 2];

		int j = 0;
		int contador = 0;
		double x = calcularXasterisco(ejercicio);
		System.out.println("x: " + x);
		for (int i = 0; i < dimension; i++) {
			double multiplicador = i / (dimension - 1);
			System.out.println("i: " + i + " /dimension: " + dimension);
			System.out.println("mult " + multiplicador);
			ejercicioDef[j] = 1 - funcionGx((multiplicador * x),
					dimension.intValue(), ejercicio);
			System.out.println("funcionGx: " + ejercicioDef[j]);
			j++;
			ejercicioDef[j] = 1 - ejercicioDef[j - 1];
			j++;
			contador++;

		}

		return ejercicioDef;
	}

	private double calcularXasterisco(Ejercicio ejercicio) {
		System.out.println("CalcularXasterisco");
		// TODO Auto-generated method stub
		// indice de discriminacion
		double a = 2;

		// b = nivel de dificultad
		double b = ejercicio.getNivelDificultad();

		// c = 1/n n = cantidad respuesta.
		double c = 1.0 / 4;// Double.valueOf(ejercicio.getConceptoList().size());

		// s = adivinanza
		double s = ejercicio.getAdivinanza();

		double k = ((1 - c) * (1 + Math.exp(-1.7 * a * b))) / s - 1;

		double x = (Math.log(k) + (1.7 * a * b)) / (1.7 * a);

		return x;

	}

	private double funcionGx(double x, int dimension, Ejercicio ejercicio) {
		System.out.println("FuncionGx");
		// G(X) = 1- ( (1-c)(1+exp(-1.7ab)) ) / ( 1+exp(1.7a(x-b)) )
		System.out
				.println("##################################################");
		// indice de discriminacion
		double a = 2;

		// b = nivel de dificultad
		double b = ejercicio.getNivelDificultad();

		// c = 1/n
		double c = 1.0 / 4;// Double.valueOf(ejercicio.getConceptoList().size());
		System.out.println("cant concepto: "
				+ ejercicio.getListaConceptos().size());
		System.out.println("a: " + a + "b: " + b + "c: " + c);

		double numerador = (1 - c) * (1 + Math.exp(-1.7 * a * b));
		System.out.println("numerado: " + numerador);

		double denominador = 1 + Math.exp(1.7 * a * (x - b));
		System.out.println("denominador: " + denominador);
		double gX = 1 - (numerador / denominador);

		System.out.println("gx: " + gX);
		System.out
				.println("##################################################");

		return gX;
	}

}
