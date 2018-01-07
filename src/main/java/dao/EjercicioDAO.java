package dao;

import javax.ejb.Stateless;

import model.Ejercicio;
import base.BaseDAO;

@Stateless
public class EjercicioDAO extends BaseDAO<Ejercicio>{

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Ejercicio.class;
	}
}
