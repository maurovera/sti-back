package dao;

import javax.ejb.Stateless;

import model.Curso;
import base.BaseDAO;

@Stateless
public class CursoDAO extends BaseDAO<Curso> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Curso.class;
	}

}
