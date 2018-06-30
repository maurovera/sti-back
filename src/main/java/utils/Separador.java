package utils;

import java.util.StringTokenizer;

public class Separador {

	public String convertirEspacioToGuion(String cadena) {

		StringTokenizer st = new StringTokenizer(cadena);

		String cadena_guion = (String) st.nextElement();
		while (st.hasMoreElements()) {
			cadena_guion = cadena_guion + "_" + st.nextElement();
		}
		return cadena_guion;

	}

	public String convertirGuionToEspacio(String cadena) {

		StringTokenizer st = new StringTokenizer(cadena, "_");

		String cadena_guion = (String) st.nextElement();
		while (st.hasMoreElements()) {
			cadena_guion = cadena_guion + " " + st.nextElement();
		}
		return cadena_guion;

	}

}
