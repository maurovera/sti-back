package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.classifiers.rules.NNge;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class HerramientasWeka {

	private List<Regla> listaReglas;
	private String nombreArchivo;
	private StringBuilder drl;

	/**Constructor de herramientas de weka
	 **/
	public HerramientasWeka(String path) {

		this.listaReglas = new ArrayList<Regla>();
		this.nombreArchivo = path;
		this.drl = new StringBuilder();

	}

	/**Ejecutar todo este procedimiento
	 **/
	public void ejecutar() {
		try {
			Instances datos = abrirCSV(nombreArchivo);
			String nnge = ejecutarNNge(datos);
			pasearNNGeAReglas(nnge);
			procesarTodasLasReglas();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Lee un archivo csv y lo devuelve con una instancia. No lee la cabecera
	 * 
	 * @param nomArchivo
	 *            Nombre del archivo csv
	 * @return instancia del archivo
	 **/
	private Instances abrirCSV(String nomArchivo) throws Exception {
		Instances datos;
		try {

			CSVLoader loader = new CSVLoader();
			loader.setOptions(new String[] { "-H" });
			loader.setSource(new File(nomArchivo));
			datos = loader.getDataSet();
			// elige el 3 atributo. Que es el a mostrar
			datos.setClassIndex(3);
			return datos;

		} catch (IOException e) {
			System.out.println("Hay algo mal al leer el archivo " + e);
			return null;
		}
	}

	/**
	 * Algoritmo NNge. Se utiliza el algoritmo de NNGE para generar las reglas
	 * que luego seran parseadas.
	 * 
	 * @param coleccion
	 *            . Es una instancia del documento csv
	 * @return devuelve la reglas en formato String
	 * */
	private String ejecutarNNge(Instances coleccion) {
		String resultado = "\n";
		NNge nnge = new NNge();
		try {
			nnge.buildClassifier(coleccion);

			resultado = resultado + nnge.toString();
			//borrar
			
			System.out.println(resultado + "\n");
		} catch (Exception exception) {

			resultado = exception.toString();
			System.out.println("No pudo ejecutar el nnge\n");
		}
		return resultado;
	}

	/**
	 * Parsea lo que devuelve el algoritmo NNGE a una lista de reglas
	 * 
	 * @param nnge
	 *            . String que genera el algoritmo nnge de weka
	 * */
	private void pasearNNGeAReglas(String nnge) {

		// convertimos el nnge a stringBuilder
		StringBuilder cadena = new StringBuilder(nnge);

		/**
		 * Quitamos todo los campos que entran en juego
		 * */
		Pattern pat = Pattern
				.compile(".class (\\w+).*\\{(.*)\\}.*\\{(.*)\\}.*\\{(.*)\\}.*\\{(.*)\\}.*\\{(.*)\\}.*\\{(.*)\\}.*\\((\\d+)\\)");
		Matcher mat = pat.matcher(cadena);

		// un contador para el numero de apariciones de reglas.
		int numApariciones = 0;

		/** Se busca la primera ocurrencia **/
		while (mat.find()) {

			// Contador de reglas
			numApariciones++;
			/** Quitamos los campos */
			String el1 = mat.group(1);
			String el2 = mat.group(2);
			String el3 = mat.group(3);
			String el4 = mat.group(4);
			String el5 = mat.group(5);
			String el6 = mat.group(6);
			String el7 = mat.group(7);
			String el8 = mat.group(8);

			/** se crea una nueva regla */
			Regla r = new Regla(el1, el2, el3, el4, el5, el6, el7, el8);
			/** Se agrega a la lista de reglas */
			listaReglas.add(r);

		}

	}

	/**
	 * La verdad no se si esto deberia de estar aqui. Procesa una sola regla. La
	 * trasnforma a su regla DRL
	 * 
	 * @param regla
	 *            . Regla que se le adjunta al string drl
	 * @param r
	 *            . string drl
	 **/
	private void procesarUnaRegla(Regla regla, StringBuilder r, Integer secuencia) {

		// Nombre de la regla y prioridad.

		r.append("rule 'regla_" + regla.getMaterialAMostrar() +"_"+ secuencia + "' \n"
				+ "salience 1\n");
				//+ "salience " + regla.getPrioridad() + "\n");

		// la parte de antecesor
		r.append("when\n");
		// conceptos
		r.append("regla : Regla( concepto ==  '" + regla.getConcepto() + "',\n(");

		// niveles
		String niveles = regla.getNivel().replaceAll(",", "||");
		niveles = niveles.replaceAll("(\\w+)", " nivel == '$1' ");

		r.append(niveles + "),\n(");

		// estilos
		String estilos = regla.getEstilo().replaceAll(",", "||");
		estilos = estilos.replaceAll("(\\w+)", " estilo == '$1' ");

		r.append(estilos + "),\n(");

		/**
		 * secuencia de ejercicios. Acotacion: se recomienda usar guion bajo
		 * para que pueda tomar como palabra. Esto va para secVideos tambien.
		 * 
		 * Otra acotación es que: el ejercicio que hizo bien y el video que le
		 * sirvio no forme parte de la secuencia para que la regla sea mas
		 * entendible. y mas facil de parsear.
		 */
		String secuenciaEjercicios = regla.getSecuenciaEjercicios().replaceAll(
				",", "||");
		secuenciaEjercicios = secuenciaEjercicios.replaceAll("(\\w+)",
				" secuenciaEjercicios == '$1' ");

		r.append(secuenciaEjercicios + "),\n(");

		// secuenciaVideos
		String secuenciaVideos = regla.getSecuenciaVideos().replaceAll(",",
				"||");
		secuenciaVideos = secuenciaVideos.replaceAll("(\\w+)",
				" secuenciaVideos == '$1' ");

		r.append(secuenciaVideos + "));\n");

		// consecuente
		r.append("then\n");
		r.append("ImprimirRegla.writeTODO('" + regla.getMaterialAMostrar() + "','"
				+ regla.getEjercioValido() + "');\n");
		r.append("regla.setResultado('"+regla.getMaterialAMostrar() + "');\n" );
		//r.append("regla.addRespuestaRegla('" + regla.getMaterialAMostrar() + "','"
			//	+ regla.getPrioridad() + "');\n");
		r.append("end\n\n");

	}

	/**
	 * Procesa todo el archivo DRL Recibe como parametro el stringBuilder drl
	 **/
	private void procesarTodasLasReglas() {
		Integer secuencia = 0;
		// StringBuilder drl = new StringBuilder();
		// importamos la clase regla. Esto cambiara cuando tenga el paquete
		// correcto
		drl.append("import utils.Regla;\n");
		// importamos la clase que utilizara para poder generar el video y el
		// ejercicio
		drl.append("import utils.ImprimirRegla;\n");
		// declaramos el dialecto
		drl.append("dialect 'java'\n");
		// procesamos todas las reglas
		for (Regla reglas : listaReglas)
		{
			secuencia++;
			procesarUnaRegla(reglas, drl,secuencia);
		}
	}
	
	public String getDrl(){
		return drl.toString();
	}

}
