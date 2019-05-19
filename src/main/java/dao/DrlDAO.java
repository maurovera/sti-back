package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import utils.Regla;
import model.Drl;
import model.Material;
import model.Sesion;
import base.BaseDAO;

@Stateless
public class DrlDAO extends BaseDAO<Drl> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Drl.class;
	}

	/**Trae el ultimo drl por asignatura*/
	public Drl ultimoDrl(Long idAsignatura) {

		/** Lista de retorno **/
		List<Drl> listaRetorno = new ArrayList<Drl>();
		Drl drl = new Drl();
		Query q;

		q = em.createQuery("SELECT d FROM Drl d " + "WHERE"
				+ " d.asignatura =:asignatura order by d.id desc");

		q.setParameter("asignatura", idAsignatura);

		listaRetorno = q.getResultList();

		if (!listaRetorno.isEmpty()) {

			drl = (Drl) listaRetorno.get(0);
			System.out.println("drl numero: "+ drl.getId());
		}


		return drl;

	}

}
