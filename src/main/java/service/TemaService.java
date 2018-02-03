package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Tema;
import utils.AppException;
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
