package dao;

import javax.ejb.Stateless;

import model.Asignatura;
import model.Profesor;
import base.BaseDAO;

@Stateless
public class AsignaturaDAO extends BaseDAO<Asignatura> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Asignatura.class;
	}

}
