package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Log;
import utils.AppException;
import base.BaseServiceImpl;
import dao.LogDAO;

@Stateless
public class LogService extends BaseServiceImpl<Log, LogDAO> {

	
	final String direccionArchivo = "/home/mauro/proyectos/tesis/sti-back/src/main/resources/archivoLog/";

	@Inject
	private LogDAO dao;

	@Override
	public LogDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	
	/**
	 * La idea es generar el archivo para que weka pueda leerlo y generar las
	 * reglas para pasarle a motor del drools
	 * 
	 * @throws AppException
	 * ***/
	public String escribirArchivoLog()
			throws AppException {

		List<Log> lista = listaLog();
		String nombreArchivo = new String();
		List<Object> datosFila = new ArrayList<Object>();
		
		for (Log log : lista) {
			String datos = new String();
			datos+=log.getSecuencia()+"\r";
			datosFila.add(datos);
			
			
		}

		nombreArchivo = crearArchivoLog(datosFila);
		
		
		
		return nombreArchivo;
	}

	/** Escribe en archivo las pruebas **/
	private String crearArchivoLog(List<Object> fila) {

		
		String csv2 = null;
		Date d = new Date(1);
		
		String nombreArchivo = direccionArchivo + "weka_"+d.toString()+".csv";
		File archivo = new File(nombreArchivo);
		
		FileWriter escribir;
		try {
			/**Crea un nuevo archivo**/
			archivo.delete();
			archivo.createNewFile();
			escribir = new FileWriter(archivo, true);

			String idList = fila.toString();
			String csv = idList.substring(1, idList.length() - 1).replace(", ",
					",");
			csv2 = idList.substring(1, idList.length() - 1).replace("\r, ",
					"\r");
			//csv = csv.replace("\r,",""); 
		
			escribir.write(csv2,0,csv2.length());

			escribir.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return nombreArchivo;

	}
	
	
	/** Lista de log's **/
	public List<Log> listaLog()
			throws AppException {
		System.out.println("Lista de logs service");
		try {
			return dao.listaLog();
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}
	
}
