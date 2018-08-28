package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import utils.AppException;
import utils.HerramientasDrools;
import model.Drl;
import base.BaseServiceImpl;
import dao.DrlDAO;

@Stateless
public class DrlService extends BaseServiceImpl<Drl, DrlDAO> {

	
	final private long userId = 1;
	
	@Inject
	private DrlDAO dao;

	@Override
	public DrlDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	
	@Transactional
	public Drl insertarDrl(Drl entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("implements de servicio tema");
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
