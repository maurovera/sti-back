package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Resuelto;
import base.BaseServiceImpl;
import dao.ResueltoDAO;

@Stateless
public class ResueltoService extends BaseServiceImpl<Resuelto, ResueltoDAO> {

	@Inject
	private ResueltoDAO dao;

	@Override
	public ResueltoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	

}
