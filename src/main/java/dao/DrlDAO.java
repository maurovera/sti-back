package dao;

import javax.ejb.Stateless;

import model.Drl;
import base.BaseDAO;

@Stateless
public class DrlDAO extends BaseDAO<Drl>  {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Drl.class;
	}

}
