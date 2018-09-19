package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import utils.AppException;
import model.Evidencia;
import model.Log;
import base.BaseDAO;

@Stateless
public class LogDAO extends BaseDAO<Log>{

	@Override
	public Class getEntity() {
	
		return Log.class;
	}
	
	
	
	/**Lista de log
	 **/
	public List<Log> listaLog() throws AppException {

		System.out.println("Lista de log");
		List<Log> lista = new ArrayList<Log>();
		
		Query q = em.createQuery("SELECT l FROM Log l ");
		lista = q.getResultList();


		if (lista == null) {
			throw new AppException(404, "Not Found lista log");
		}

		return lista;
	}

}
