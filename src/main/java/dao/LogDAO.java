package dao;

import javax.ejb.Stateless;

import model.Log;
import base.BaseDAO;

@Stateless
public class LogDAO extends BaseDAO<Log>{

	@Override
	public Class getEntity() {
	
		return Log.class;
	}
}
