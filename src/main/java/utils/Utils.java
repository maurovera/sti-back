package utils;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.shiro.SecurityUtils;

import seguridad.Usuario;

import com.google.gson.Gson;

public class Utils {
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";
	static SecureRandom rnd = new SecureRandom();
	
	public static Usuario obtenerUsuarioAutenticado() {
        Usuario usuario = null;
        Object user = SecurityUtils.getSubject().getSession().getAttribute("usuario");
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        usuario = gson.fromJson(userJson, Usuario.class);               
        return usuario;
    }
	
	public static String formatearMonto(Integer monto) {
		if (monto == null) {
			return "";
		}
		DecimalFormat formatea = new DecimalFormat("###,###.##");
		return formatea.format(monto);
	}
	
	public static Date sumarDias(Date fecha, int dias) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		/*cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR, 23);*/
		cal.add(Calendar.DATE, dias);
		return cal.getTime();
	}
	
	public static int getNroDiaActual(Date fecha) {
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}
	
	public static Date addSeccondsToCurrentDate(int secconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, secconds);
		Date d = calendar.getTime();
		return d;
	}
	
	public static String randomString( int len ) {
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
		  sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
	
	public static String generarPin() {
		String clave = "";
		Random random = new Random((int) System.currentTimeMillis());
		for (int i = 0; i < 4; i++) {
			int nroRam = random.nextInt(10);
			clave += nroRam;
		}
		return clave;
	}
	
	public static String generarClave(int count, boolean letters, boolean numbers) {

		int start = 0;
		int end = 0;
		Random random = new Random();

		if (start == 0 && end == 0) {
			end = 'z' + 1;
			start = ' ';
			if (!letters && !numbers) {
				start = 0;
				end = Integer.MAX_VALUE;
			}
		}

		char[] buffer = new char[count];
		int gap = end - start;

		while (count-- != 0) {
			char ch = (char) (random.nextInt(gap) + start);
			if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
				if (ch >= 56320 && ch <= 57343) {
					if (count == 0) {
						count++;
					} else {
						// low surrogate, insert high surrogate after putting it
						// in
						buffer[count] = ch;
						count--;
						buffer[count] = (char) (55296 + random.nextInt(128));
					}
				} else if (ch >= 55296 && ch <= 56191) {
					if (count == 0) {
						count++;
					} else {
						// high surrogate, insert low surrogate before putting
						// it in
						buffer[count] = (char) (56320 + random.nextInt(128));
						count--;
						buffer[count] = ch;
					}
				} else if (ch >= 56192 && ch <= 56319) {
					// private high surrogate, no effing clue, so skip it
					count++;
				} else {
					buffer[count] = ch;
				}
			} else {
				count++;
			}
		}

		return new String(buffer).toUpperCase();
	}

}
