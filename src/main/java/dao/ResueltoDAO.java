package dao;

import javax.ejb.Stateless;

import model.Resuelto;
import base.BaseDAO;


@Stateless
public class ResueltoDAO extends BaseDAO<Resuelto> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Resuelto.class;
	}

}
