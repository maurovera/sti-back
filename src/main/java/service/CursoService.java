package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Curso;
import base.BaseServiceImpl;
import dao.CursoDAO;

@Stateless
public class CursoService extends BaseServiceImpl<Curso, CursoDAO> {

	@Inject
	private CursoDAO dao;

	@Override
	public CursoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
