package dao;

import javax.ejb.Stateless;

import model.TareaDetalle;
import base.BaseDAO;

@Stateless
public class TareaDetalleDAO extends BaseDAO<TareaDetalle> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return TareaDetalle.class;
	}

}

