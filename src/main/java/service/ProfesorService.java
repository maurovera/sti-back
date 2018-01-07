package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Profesor;
import base.BaseServiceImpl;
import dao.ProfesorDAO;

@Stateless
public class ProfesorService extends BaseServiceImpl<Profesor, ProfesorDAO> {

	@Inject
	private ProfesorDAO dao;

	@Override
	public ProfesorDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
