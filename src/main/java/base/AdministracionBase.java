package base;

import java.math.BigDecimal;
import java.util.ArrayList;
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

	//final String dir = "/home/mauro/proyectos/tesis/sti-back/src/main/resources/redes/";
	final String dir = "/home/catherine/tesis/sti-back/src/main/resources/redes/";
	Separador sp = new Separador();

	public AdministracionBase() {

	}

	/***
	 * Crear. Crea una red con el nombre de la asignatura. Recibe una asignatura
	 * Modificar. Para editar no necesita modificar la red. Eliminar. No hace
	 * falta eliminar la asignatura. Solo queda colgado el archivo en redes. en
	 * todo caso deberia ver como eliminar el archivo
	 ***/
	public void agregarAsignaturaRed(Asignatura asignatura) {
		System.out.println("agregarAsignaturaRed");
		Network net = new Network();
		String titulo = sp.convertirEspacioToGuion(asignatura.getNombre());
		// //cambiar formato nodo
		net.addNode(Network.NodeType.Cpt, titulo);
		net.setOutcomeId(titulo, 0, "No_conoce");
		net.setOutcomeId(titulo, 1, "Conoce");
		String nombreRed = "red_asignatura_" + asignatura.getId() + ".xdsl";
		System.out.println(dir + nombreRed);
		net.writeFile(dir + nombreRed);
	}

	/**
	 * Agrega un tema a la red bayesiana.
	 **/
	public void agregarTemaRed(Tema tema) {
		System.out.println("agregarTemaRed");
		// operaciones sobre la red bayesiana con smile
		System.out.println("id Asignatura: " + tema.getAsignatura().getId());
		Long idAsignatura = tema.getAsignatura().getId();
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		System.out.println(dir + nombreRed);
		Network net = new Network();
		net.readFile(dir + nombreRed);

		// agregar nodo tema
		// cambiar formato nodo
		String titulo = sp.convertirEspacioToGuion(tema.getNombre());

		net.addNode(Network.NodeType.Cpt, titulo);
		net.setOutcomeId(titulo, 0, "No_conoce");
		net.setOutcomeId(titulo, 1, "Conoce");

		// agregar los arcos

		net.addArc(titulo,
				sp.convertirEspacioToGuion(tema.getAsignatura().getNombre()));

		net.writeFile(dir + nombreRed);
	}

	/**
	 * Modifica un tema a la red bayesiana.
	 **/
	public void modificarTemaRed(Tema tema, String titulo) {
		// operaciones sobre la red bayesiana con smile

		Long idAsignatura = tema.getAsignatura().getId();
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile(dir + nombreRed);

		String tituloNuevo = sp.convertirEspacioToGuion(tema.getNombre());

		// cambiar formato nodo
		// modifica el nombre
		net.setNodeId(sp.convertirEspacioToGuion(titulo), tituloNuevo);
		net.setNodeName(tituloNuevo, tituloNuevo);

		net.writeFile(dir + nombreRed);
	}

	/**
	 * Elimina un tema a la red bayesiana.
	 **/
	public void eliminarTemaRed(Tema tema) {
		// TODO Auto-generated method stub
		Long idAsignatura = tema.getAsignatura().getId();
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		Network net = new Network();
		net.readFile(dir + nombreRed);
		// cambiar formato nombre
		String nom = sp.convertirEspacioToGuion(tema.getNombre());
		net.deleteNode(nom);
		net.writeFile(dir + nombreRed);
	}

	/**
	 * Se agrega un concepto a la red
	 **/
	public void agregarConceptoRed(Concepto concepto, Long idAsignatura) {
		// operaciones sobre la red bayesiana con smile
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		String titulo;
		String tituloTema;
		System.out.println(dir + nombreRed);
		Network net = new Network();
		net.readFile(dir + nombreRed);

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
		net.writeFile(dir + nombreRed);
	}

	/**
	 * Modificar un concepto en la red
	 ***/
	public void modificarConceptoRed(Concepto concepto, String tituloViejo,
			Long idAsignatura) {
		// operaciones sobre la red bayesiana con smile
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";

		System.out.println(dir + nombreRed);
		Network net = new Network();
		net.readFile(dir + nombreRed);

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

		net.writeFile(dir + nombreRed);
	}

	/*** Eliminar ejercicio de la red bayesiana ***/
	public void eliminarEjercicioRed(Ejercicio ejercicio, Long idAsignatura) {
		// TODO Auto-generated method stub\
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile(dir + nombreRed);

		// cambiar formato nodo
		String nom = "E" + Long.toString(ejercicio.getId());

		net.deleteNode(nom);
		net.writeFile(dir + nombreRed);

	}

	/** Eliminar concepto de la red bayesiana */
	public void eliminarConceptoRed(Concepto concepto, Long idAsignatura) {
		// TODO Auto-generated method stub\
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile(dir + nombreRed);
		// cambiar formato nodo
		String nom = sp.convertirEspacioToGuion(concepto.getNombre());

		net.deleteNode(nom);
		net.writeFile(dir + nombreRed);

	}

	/**
	 * Por cada concepto borrado o agregado. Se regenera el ejercicio.
	 ***/
	public void reCalcularEjercicios(List<Ejercicio> listaEjercicios,
			Long idAsignatura) {
		System.out.println("Re calcularEjercicios");

		for (Ejercicio ejercicio : listaEjercicios) {

			/** Tipo si esta vacio. Eliminar el nodo ejercicio. **/
			if (ejercicio.getListaConceptos().isEmpty()) {
				System.out.println("elimine ejercicio.");
				eliminarEjercicioRed(ejercicio, idAsignatura);
				/**
				 * Tipo si esta con conceptos. Se borra el ejercicio y se vuelve
				 * a meter con sus nuevos conceptos
				 **/
			} else {
				System.out.println("calcule metodo para que quede bien");
				eliminarEjercicioRed(ejercicio, idAsignatura);
				agregarEjercicioRed(ejercicio, idAsignatura);
			}
		}
	}

	/**
	 * Agregar Ejercicio a la red bayesiana
	 **/
	public void agregarEjercicioRed(Ejercicio ejercicio, Long idAsignatura) {
		System.out.println("Agregar Ejercicio Red");
		// operaciones sobre la red bayesiana con smile
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		System.out.println(dir + nombreRed);
		Network net = new Network();
		net.readFile(dir + nombreRed);

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
				String conceptoRed = new String();
				System.out.println("primerconcepto: " + concepto.getNombre());
				conceptoRed = sp.convertirEspacioToGuion(concepto.getNombre());

				net.addArc(conceptoRed, titulo);
			}
		}

		// definir probabilidades condicionales
		double[] ejercicioDef = calcularProbabilidadesCCI(ejercicio);
		net.setNodeDefinition(titulo, ejercicioDef);

		net.writeFile(dir + nombreRed);
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

	/***
	 * Calculos de probabilidades Aqui todo este calculo se hace cuando un
	 * alumno se inscribe.calcularProbabilidadesTema Lo que hace es agregar los
	 * pesos de los temas y de los conceptos en si.
	 * *******/
	/*** AUN NO USO **/
	public void calcularProbabilidades(Asignatura asignatura) {

		System.out.println("CalcularProbabilidades. Inscribirse");
		// Calculo de las probabilidades condicionales de las relaciones de
		// agregacion
		/** Lee la red de la asignatura **/
		Long idAsignatura = asignatura.getId();
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		System.out.println(dir + nombreRed);
		Network net = new Network();
		net.readFile(dir + nombreRed);

		String nomTem;
		// Asignatura asignatura = em.find(Asignatura.class, idAsignatura);

		// List<Tema> temaList = (List<Tema>)
		// em.createQuery("Select e from Tema e where e.profesor = :profesor and e.asignatura=:asignatura order by e.idTema"
		// )
		// .setParameter("profesor",
		// asignatura.getProfesor()).setParameter("asignatura",
		// asignatura).getResultList() ;
		List<Tema> listaTema = asignatura.getListaTemas();

		for (Tema tema : listaTema) {
			double[] temaDef = calcularProbabilidadesTema(tema);
			System.out.println(tema.getNombre() + " " + temaDef.toString());
			// cambiar formato de nodo
			nomTem = sp.convertirEspacioToGuion(tema.getNombre());
			net.setNodeDefinition(nomTem, temaDef);
		}

		double[] asignaturaDef = calcularProbabilidadesAsignatura(asignatura);
		// cambiar formato de ndo
		nomTem = sp.convertirEspacioToGuion(asignatura.getNombre());
		net.setNodeDefinition(nomTem, asignaturaDef);

		net.writeFile(dir + nombreRed);

	}

	/*** AUN NO USO **/
	private double[] calcularProbabilidadesAsignatura(Asignatura asignatura) {
		int dimension = (int) Math.pow(2, asignatura.getListaTemas().size());
		double[] asignaturaDef = new double[dimension * 2];

		System.out.println("DimensionAsignatura " + dimension);

		int j = 0;
		for (int i = 0; i < dimension; i++) {
			asignaturaDef[j] = 1 - cpAsignatura(asignatura, i);
			j++;
			asignaturaDef[j] = 1 - asignaturaDef[j - 1];
			j++;

		}

		return asignaturaDef;
	}

	private double cpAsignatura(Asignatura asignatura, int i) {

		// //HAY QUE REVISAR ESTA FUNCION
		String comb = Integer.toBinaryString(i);
		int dimension = asignatura.getListaTemas().size();
		while (comb.length() != dimension) {
			comb = "0" + comb;
		}

		double valTemp = 0;
		// List<Tema> temaList =
		// em.createQuery("Select e from Tema e where e.asignatura = :asignatura")
		// .setParameter("asignatura", asignatura).getResultList();
		List<Tema> temaList = asignatura.getListaTemas();
		System.out.println(temaList);
		for (int j = 0; j < temaList.size(); j++) {
			if (comb.charAt(j) == '1') {
				// valTemp = valTemp + temaList.get(j).getPeso();
				valTemp = valTemp + temaList.get(j).getPesoTema();
			}
		}

		return valTemp;
	}

	/*** AUN NO USO **/
	private double[] calcularProbabilidadesTema(Tema tema) {
		// Calculo de las probabilidades condicionales
		int dimension = (int) Math.pow(2, tema.getListaConceptos().size());
		System.out.println("dimension " + dimension);
		double[] temaDef = new double[dimension * 2];
		System.out
				.println("**************************************************************");
		int j = 0;
		for (int i = 0; i < dimension; i++) {
			temaDef[j] = 1 - cpTema(tema, i);
			j++;
			temaDef[j] = 1 - temaDef[j - 1];
			j++;

		}
		System.out
				.println("**************************************************************");

		return temaDef;
	}

	/*** AUN NO USO ***/
	private double cpTema(Tema tema, int i) {

		int cantidadConceptos = tema.getListaConceptos().size();
		String comb = Integer.toBinaryString(i);
		while (comb.length() < cantidadConceptos) {
			comb = "0" + comb;
		}

		double valTemp = 0;
		for (int j = 0; j < cantidadConceptos; j++) {
			System.out
					.println("--------------------------------------------------------");
			System.out.println("j: " + j);

			System.out.println("comb: " + comb);
			if (comb.charAt(j) == '1') {

				System.out.println("concepto: "
						+ tema.getListaConceptos().get(j));
				System.out.println("peso: "
						+ tema.getListaConceptos().get(j).getPeso());
				System.out
						.println("--------------------------------------------------------");

				valTemp = valTemp + tema.getListaConceptos().get(j).getPeso();
			}
		}

		return valTemp;

	}

	/*** FIN Calculos de probabilidades *******/

	/**
	 * NO USE AUN* PERO CREA RED BAYESIANA POR LISTA DE ALUMNOS Lo que hace es
	 * leer la red bayesiana con idAsignatura y guardalo con un idAsignatura +
	 * un idAlumno. Que seria el arbol del alumno.
	 **/
	public void crearRedAlumno(Long idAsignatura, Long idAlumno) {
		// TODO Auto-generated method stub

		// Asignatura asignatura = em.find(Asignatura.class, idAsignatura);
		// Curso curso = asignatura.getCurso();
		// List<Alumno> alumnoList =
		// em.createQuery("Select e from Alumno e where e.curso = :curso")
		// .setParameter("curso", curso).getResultList();
		/** Crear Red Alumno **/
		String nombreRed = "red_asignatura_" + idAsignatura + ".xdsl";
		Network net = new Network();
		net.readFile(dir + nombreRed);

		// for (Alumno alumno : alumnoList) {
		// String nombreRedAlumno = "red_alumno_" + alumno.getIdAlumno() +
		// "_asignatura_" + idAsignatura + ".xdsl";
		String nombreRedAlumno = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";
		net.writeFile(dir + nombreRedAlumno);

		// }

	}
	
	/**Genera un backup del archivo alumno con su asignatura.
	 * Este contiene su resultado en test 1 y test 2 */
	public void backupDelAlumno(Long idAsignatura, Long idAlumno, Long idTarea) {
		// TODO Auto-generated method stub
		/** Llama a la red de asignatura para crear el backup**/
		String nombreRed = "red_alumno_" + idAlumno + 
				"_asignatura_"+ idAsignatura+".xdsl";
		Network net = new Network();
		net.readFile(dir + nombreRed);

		
		/**Se crea la red del alumno backupeado*/
		String nombreRedAlumno = "backup_red_alumno_" + idAlumno 
				+ "_asignatura_" + idAsignatura  
				+ "_tarea_"+ idTarea +".xdsl";
		net.writeFile(dir +  "backup/"+ nombreRedAlumno);
	}

	
	
	/**Genera un backup del archivo alumno con su asignatura.
	 * Este contiene su resultado en test 1 y test 2 */
	public void backupDelPrimerTestAlumno(Long idAsignatura, Long idAlumno, Long idTarea) {
		// TODO Auto-generated method stub
		/** Llama a la red de asignatura para crear el backup**/
		String nombreRed = "red_alumno_" + idAlumno + 
				"_asignatura_"+ idAsignatura+".xdsl";
		Network net = new Network();
		net.readFile(dir + nombreRed);

		
		/**Se crea la red del alumno backupeado*/
		String nombreRedAlumno = "backup_PrimerTest_red_alumno_" + idAlumno 
				+ "_asignatura_" + idAsignatura  
				+ "_tarea_"+ idTarea +".xdsl";
		net.writeFile(dir +  "backupPrimerTest/"+ nombreRedAlumno);
	}

	
	
	/** Metodo para el log **/
	public List<Object> registrarEjercicio(Asignatura asignatura,
			Long idAlumno, Ejercicio ejercicio, Boolean respuesta,
			int cantEjercicio, String tipoAlumno) {

		List<Object> datosFila = new ArrayList<Object>();

		/***
		 * String queryConceptos =
		 * "select c from Concepto c  join c.tema t where t.asignatura = :asignatura"
		 * ; Query query = em.createQuery(queryConceptos);
		 * query.setParameter("asignatura", asignatura); List<Concepto>
		 * conceptos = query.getResultList();
		 */
		/* List<Tema> temas = obtenerTemas(asignatura); */

		datosFila.add(idAlumno);
		datosFila.add(getValorNodoRed(asignatura.getNombre(),
				asignatura.getId(), idAlumno));

		/**
		 * Lista de conceptos y temas
		 ***/

		List<Tema> temas = new ArrayList<Tema>();

		temas = asignatura.getListaTemas();
		for (Tema tema : temas) {
			String porcentajeTema = getValorNodoRed(tema.getNombre(),
					asignatura.getId(), idAlumno);
			if (porcentajeTema == null)
				porcentajeTema = "temaNulo";

			datosFila.add(porcentajeTema);
			for (Concepto concepto : tema.getListaConceptos()) {
				String porcentajeConcepto = getValorNodoRed(
						concepto.getNombre(), asignatura.getId(), idAlumno);

				if (porcentajeConcepto == null)
					porcentajeConcepto = "conceptoNulo";

				datosFila.add(porcentajeConcepto);

			}
		}

		datosFila.add(ejercicio.getId());
		datosFila.add(ejercicio.getAdivinanza());
		datosFila.add(ejercicio.getNivelDificultad());
		datosFila.add((respuesta) ? "SI" : "NO");
		datosFila.add(cantEjercicio);
		datosFila.add(tipoAlumno);
		datosFila.add("\r");

		return datosFila;

	}

	/** Utilizado para el log **/
	public String getValorNodoRed(String nombre, Long idAsignatura,
			Long idAlumno) {
		// TODO Auto-generated method stub
		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile(dir + nombreRed);

		net.updateBeliefs();
		String titulo = sp.convertirEspacioToGuion(nombre);
		// System.out.println(titulo);
		double[] valor = net.getNodeValue(titulo);

		double conoce = valor[1];

		return Double.toString(conoce);

	}

	
	/**Retorna el double*/
	public Double getValorNodoRedDouble(String nombre, Long idAsignatura,
			Long idAlumno) {
		// TODO Auto-generated method stub
		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile(dir + nombreRed);

		net.updateBeliefs();
		String titulo = sp.convertirEspacioToGuion(nombre);
		// System.out.println(titulo);
		double[] valor = net.getNodeValue(titulo);

		double conoce = valor[1];

		return conoce;

	}
	
	
	
	/** Utilizado para saber valor de red**/
	public String getValorNodoRedError(String nombre, Long idAsignatura,
			Long idAlumno) {
		// TODO Auto-generated method stub
		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile(dir+"errores/" + nombreRed);

		net.updateBeliefs();
		String titulo = sp.convertirEspacioToGuion(nombre);
		// System.out.println(titulo);
		double[] valor = net.getNodeValue(titulo);
		double[] va = net.getNodeValue("tenondera");
		
		double conoce = valor[1];
		
		String retorno1 = "primer valor sin nada: "+ Double.toString(conoce)+"/N";
		
		return retorno1;

	}
	/** Utilizado para saber valor de red 2**/
	public String getValorNodoRedErrorEvidencia(String nombre, Long idAsignatura,
			Long idAlumno) {
		// TODO Auto-generated method stub
		
		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + ".xdsl";

		Network net = new Network();
		net.readFile(dir+"errores/" + nombreRed);
		//double[] values = net.getNodeValue("tenondera");
		//String retorno1 = "primer valor sin evidencia: "+ String.valueOf(values[1])+"/n";
		
		net.setEvidence("tenondera", "Conoce");
		
		net.updateBeliefs();
		double[] values = net.getNodeValue("tenondera");
		double conoce = values[1];
		double nconoce = values[0];
		String retorno2 = "conoce con evidencia: "+ String.valueOf(conoce)+"/n";
		String retorno1 = "No conoce con evidencia: "+ String.valueOf(nconoce)+"/n";
		net.clearEvidence("tenondera");
		net.updateBeliefs();

		String r = retorno2+retorno1;
		
		return r;
		
	}

	
	/** Utilizado para el log **/
	public String getValorNodoRedFinal(String nombre, Long idAsignatura,
			Long idAlumno) {
		// TODO Auto-generated method stub
		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + "_tarea_1.xdsl";

		Network net = new Network();
		net.readFile("/home/catherine/tesis/sti-back/src/main/resources/redes/pruebasRedesTest/backup_PrimerTest_" + nombreRed);
		net.updateBeliefs();
		String titulo = sp.convertirEspacioToGuion(nombre);
		// System.out.println(titulo);
		double[] valor = net.getNodeValue(titulo);

		double conoce = valor[1];

		return Double.toString(conoce);

	}
	
	/**Retorna el double resultados finales*/
	public Double getValorNodoRedDoubleFinales(String nombre, Long idAsignatura,
			Long idAlumno) {
		// TODO Auto-generated method stub
		String nombreRed = "red_alumno_" + idAlumno + "_asignatura_"
				+ idAsignatura + "_tarea_1.xdsl";

		Network net = new Network();
		net.readFile("/home/catherine/tesis/sti-back/src/main/resources/redes/pruebasRedesTest/backup_PrimerTest_" + nombreRed);

		net.updateBeliefs();
		String titulo = sp.convertirEspacioToGuion(nombre);
		// System.out.println(titulo);
		double[] valor = net.getNodeValue(titulo);

		double conoce = valor[1];

		return conoce;

	}
	
	
	
	

	
	
	
}
