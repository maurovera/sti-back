package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Evidencia;
import base.BaseServiceImpl;
import dao.EvidenciaDAO;

@Stateless
public class EvidenciaService extends BaseServiceImpl<Evidencia, EvidenciaDAO> {
	
	@Inject
	private EvidenciaDAO dao;

	@Override
	public EvidenciaDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
