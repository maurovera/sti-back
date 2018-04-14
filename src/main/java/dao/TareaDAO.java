package dao;

import javax.ejb.Stateless;

import model.Tarea;
import base.BaseDAO;

@Stateless
public class TareaDAO extends BaseDAO<Tarea> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Tarea.class;
	}

}