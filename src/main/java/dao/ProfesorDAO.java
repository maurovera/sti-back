package dao;

import javax.ejb.Stateless;

import model.Profesor;
import base.BaseDAO;

@Stateless
public class ProfesorDAO extends BaseDAO<Profesor> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Profesor.class;
	}
	
	
	

}
