package dao;

import javax.ejb.Stateless;

import model.Simulacion;
import base.BaseDAO;

@Stateless
public class SimulacionDAO extends BaseDAO<Simulacion> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Simulacion.class;
	}
}
