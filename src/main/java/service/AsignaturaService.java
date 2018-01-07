package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Asignatura;
import model.Concepto;
import base.BaseServiceImpl;
import dao.AsignaturaDAO;
import dao.ConceptoDAO;

@Stateless
public class AsignaturaService extends BaseServiceImpl<Asignatura, AsignaturaDAO> {

	@Inject
	private AsignaturaDAO dao;

	@Override
	public AsignaturaDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
