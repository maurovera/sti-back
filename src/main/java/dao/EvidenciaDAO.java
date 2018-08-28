package dao;

import javax.ejb.Stateless;

import model.Evidencia;
import base.BaseDAO;

@Stateless
public class EvidenciaDAO extends BaseDAO<Evidencia> {
	
	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Evidencia.class;
	}

}
