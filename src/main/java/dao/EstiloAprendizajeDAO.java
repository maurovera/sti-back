package dao;

import javax.ejb.Stateless;

import model.EstiloAprendizaje;
import base.BaseDAO;

@Stateless
public class EstiloAprendizajeDAO extends BaseDAO<EstiloAprendizaje> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return EstiloAprendizaje.class;
	}

}
