package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Concepto;
import base.BaseServiceImpl;
import dao.ConceptoDAO;

@Stateless
public class ConceptoService extends BaseServiceImpl<Concepto, ConceptoDAO>{

	@Inject
	private ConceptoDAO dao;

	@Override
	public ConceptoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
