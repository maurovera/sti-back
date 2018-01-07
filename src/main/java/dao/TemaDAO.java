package dao;

import javax.ejb.Stateless;

import model.Tema;
import base.BaseDAO;

@Stateless
public class TemaDAO extends BaseDAO<Tema> {
	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Tema.class;
	}

}
