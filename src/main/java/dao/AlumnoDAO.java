package dao;

import javax.ejb.Stateless;

import model.Alumno;
import base.BaseDAO;

@Stateless
public class AlumnoDAO extends BaseDAO<Alumno> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Alumno.class;
	}

}