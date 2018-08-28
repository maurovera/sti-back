package dao;

import javax.ejb.Stateless;

import model.Material;
import base.BaseDAO;

@Stateless
public class MaterialDAO extends BaseDAO<Material> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Material.class;
	}

}