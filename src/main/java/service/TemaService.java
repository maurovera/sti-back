package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Tema;
import base.BaseServiceImpl;
import dao.TemaDAO;

@Stateless
public class TemaService extends BaseServiceImpl<Tema, TemaDAO>{

	@Inject
	private TemaDAO dao;

	@Override
	public TemaDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
