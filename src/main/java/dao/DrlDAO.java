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

	/** Trae el ultimo drl por asignatura */
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
			System.out.println("drl numero: " + drl.getId());
		}

		return drl;

	}

	/** iguales */
	public String iguales() {

		/** Lista de retorno **/
		List<Drl> listaRetorno = new ArrayList<Drl>();
		String drl = "iguales";
		Query q;

		q = em.createQuery("SELECT d FROM Drl d " + " order by d.id desc");

		listaRetorno = q.getResultList();

		if (!listaRetorno.isEmpty()) {

			for (int i = 0; i < 7; i++) {
				Drl drl1 = listaRetorno.get(i);
				for (int j = 0; j < 8; j++) {
					Drl drl2 = listaRetorno.get(j);
					if(drl1.getArchivoDrl()==drl2.getArchivoDrl()){
						System.out.println("son iguales "+ i + " y "+ j);
					}else{
						System.out.println("son distintos: "+i + " y "+ j);
					}
						
				}
			}
		}

		return drl;

	}

}
