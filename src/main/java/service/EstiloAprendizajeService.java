package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.EstiloAprendizaje;
import base.BaseServiceImpl;
import dao.EstiloAprendizajeDAO;

@Stateless
public class EstiloAprendizajeService extends BaseServiceImpl<EstiloAprendizaje, EstiloAprendizajeDAO> {

	@Inject
	private EstiloAprendizajeDAO dao;

	@Override
	public EstiloAprendizajeDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}