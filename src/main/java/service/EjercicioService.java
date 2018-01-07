package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Ejercicio;
import base.BaseServiceImpl;
import dao.EjercicioDAO;

@Stateless
public class EjercicioService extends BaseServiceImpl<Ejercicio, EjercicioDAO> {

	@Inject
	private EjercicioDAO dao;

	@Override
	public EjercicioDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
