package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.TareaDetalle;
import base.BaseServiceImpl;
import dao.TareaDetalleDAO;

@Stateless
public class TareaDetalleService extends BaseServiceImpl<TareaDetalle, TareaDetalleDAO>{

	@Inject
	private TareaDetalleDAO dao;

	@Override
	public TareaDetalleDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
