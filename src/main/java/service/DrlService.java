package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import utils.AppException;
import utils.HerramientasDrools;
import utils.HerramientasWeka;
import model.Drl;
import base.BaseServiceImpl;
import dao.DrlDAO;

@Stateless
public class DrlService extends BaseServiceImpl<Drl, DrlDAO> {

	
	final private long userId = 1;
	
	@Inject
	private DrlDAO dao;
	
	@Inject
	private EvidenciaService evidenciaService;

	@Override
	public DrlDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	
	@Transactional
	public Drl insertarDrl(Drl entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("implements de servicio insertar drl");
			// Usuario user = getCurrentUser();
			entity.setFechaCreacion(new Date());
			entity.setUsuarioCreacion(userId);
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			// validate(entity);
			getDao().insert(entity);
			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene el archivo del drl asignado por el id
	 **/
	public String obtenerArchivo(Long id) throws AppException {
		Drl drl = null;
		try {
			drl =  getDao().get(id);
			return drl.getArchivoDrl();
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}
	
	
	
	/**
	 * Metodo que se encarga de generar las reglas para el weka
	 * que se obtiene de evidencias y luego transformarlo en
	 * reglas drl. en otras palabras para el motor de regla. 
	 * 
	 * Busca en la tabla evidencias todas las evdencias correspondientes
	 * a la asignatura y curso. estos son metidos al weka
	 * en cual corre el algoritmo nnge y luego las parsea en reglas
	 * drl con la clase herramientaWeka y la clase herramientaDrools
	 * este retorna la direccion del archivo que luego se carga en la base
	 * de datos con reglas drl
	 * Este archivo queda en una clase llamada drl que contiene el archivo
	 * drl que luego se usa en el drools
	 * @param id asignatura y idCurso
	 * @return un clase drl nueva y retorna la direccion del archivo en si. 
	 * @throws AppException 
	 **/
	public String guardarReglasDrl(
			Long idAsig,
			 Long idCurso,HttpServletRequest httpRequest) throws AppException  {
		
		/**Aqui crea el archivo que necesita weka.**/
		String nombreArchivo = new String();
		nombreArchivo = evidenciaService.escribirArchivoWekaEvidencia(idAsig, idCurso);
		
		/***Se le pasa el nombre del archivo para ejecutar el algoritmo nnge y trasformarlo a drl
		 ***/
		Drl d = new Drl();
		HerramientasWeka hw = new HerramientasWeka(nombreArchivo);
		hw.ejecutar();
		System.out.println(hw.getDrl());
		String drl = hw.getDrl();
		d.setArchivoDrl(drl);
		
		// se inserta el archivo drl
		d = insertarDrl(d, httpRequest);
		
		
		return nombreArchivo;
		
	}
	
	
	/**
	 * Inicia un herramienta drools. Osea inicia el motor de reglas. 
	 **/
	public HerramientasDrools iniciarDrools(String drl) throws AppException {
		HerramientasDrools hd = null;
		try {
			hd = new HerramientasDrools(drl);
			hd.iniciarBaseConocimiento();
			return hd;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}
	
}
