package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Tarea;
import base.BaseServiceImpl;
import dao.TareaDAO;

@Stateless
public class TareaService extends BaseServiceImpl<Tarea, TareaDAO> {

	@Inject
	private TareaDAO dao;

	@Override
	public TareaDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
