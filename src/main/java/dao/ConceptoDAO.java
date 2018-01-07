package dao;

import javax.ejb.Stateless;

import model.Concepto;
import base.BaseDAO;

@Stateless
public class ConceptoDAO extends BaseDAO<Concepto> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Concepto.class;
	}

}
