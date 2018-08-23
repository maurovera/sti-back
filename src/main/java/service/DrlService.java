package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import utils.AppException;
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
	
	
	
}
