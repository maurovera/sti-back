package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Alumno;
import base.BaseServiceImpl;
import dao.AlumnoDAO;

@Stateless
public class AlumnoService extends BaseServiceImpl<Alumno, AlumnoDAO> {

	@Inject
	private AlumnoDAO dao;

	@Override
	public AlumnoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
