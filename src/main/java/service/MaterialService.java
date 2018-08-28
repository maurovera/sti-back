package service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Material;
import base.BaseServiceImpl;
import dao.MaterialDAO;

@Stateless
public class MaterialService extends BaseServiceImpl<Material, MaterialDAO> {

	@Inject
	private MaterialDAO dao;

	@Override
	public MaterialDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

}
