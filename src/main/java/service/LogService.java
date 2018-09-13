package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Log;
import base.BaseServiceImpl;
import dao.LogDAO;

@Stateless
public class LogService extends BaseServiceImpl<Log, LogDAO> {

	@Inject
	private LogDAO dao;

	@Override
	public LogDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
}
