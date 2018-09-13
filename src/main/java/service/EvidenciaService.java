package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Evidencia;
import utils.AppException;
import base.BaseServiceImpl;
import dao.EvidenciaDAO;

@Stateless
public class EvidenciaService extends BaseServiceImpl<Evidencia, EvidenciaDAO> {

	final String direccionArchivo = "/home/mauro/proyectos/tesis/sti-back/src/main/resources/archivoWeka/";

	
	@Inject
	private EvidenciaDAO dao;

	@Override
	public EvidenciaDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	/** Lista de evidencias por su idAsignatura y su idCurso **/
	public List<Evidencia> listaEvidencia(Long idAsig, Long idCurso)
			throws AppException {
		System.out.println("Lista de evidencias service");
		try {
			return dao.listaEvidencia(idAsig, idCurso);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * La idea es generar el archivo para que weka pueda leerlo y generar las
	 * reglas para pasarle a motor del drools
	 * 
	 * @throws AppException
	 * ***/
	public String escribirArchivoWekaEvidencia(Long idAsig, Long idCurso)
			throws AppException {

		List<Evidencia> lista = listaEvidencia(idAsig, idCurso);
		String nombreArchivo = new String();
		List<Object> datosFila = new ArrayList<Object>();
		/** Cabecera **/
		String cabecera = "﻿CONCEPTO,NIVEL," + "ESTILO,A_MOSTRAR,SEC_VIDEOS,"
				+ "SEC_EJER,EJER_VALIDO\r";
		datosFila.add(cabecera);
		
		for (Evidencia evidencia : lista) {
			String datos = new String();
			datos+=evidencia.getConcepto()+",";
			datos+=evidencia.getNivel()+",";
			datos+=evidencia.getEstilo()+",";
			datos+=evidencia.getMaterialAMostrar()+",";
			datos+=evidencia.getSecuenciaMaterial()+",";
			datos+=evidencia.getSecuenciaEjercicio()+",";
			datos+=evidencia.getEjercicioValido()+"\r";
			datosFila.add(datos);
			
			
		}

		nombreArchivo = crearArchivoWeka(datosFila, idAsig);
		
		
		
		return nombreArchivo;
	}

	/** Escribe en archivo las pruebas **/
	private String crearArchivoWeka(List<Object> fila, Long idAsig) {

		
		String csv2 = null;
		String nombreArchivo = direccionArchivo + "weka_"+idAsig+".csv";
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
	
	
	

}
