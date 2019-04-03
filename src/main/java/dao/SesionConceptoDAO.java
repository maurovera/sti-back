package dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Material;
import model.Sesion;
import model.SesionConcepto;
import base.BaseDAO;

@Stateless
public class SesionConceptoDAO extends BaseDAO<SesionConcepto> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return SesionConcepto.class;
	}

	/**
	 * Trae el SesionConcepto que tiene la Sesion y el idConcepto como entrada
	 * 
	 */
	public SesionConcepto sesionConceptoPorSesion(Long sesion, Long idConcepto) {

		SesionConcepto sesionRetorno = new SesionConcepto();
		Query q;
		/**
		 * Query que trae la lista de materiales disponibles Si no hay
		 * materiales disponibles aun. Se cambia nomas la consulta
		 ***/

		q = em.createQuery("SELECT sc FROM SesionConcepto sc " + "WHERE"
				+ " sc.sesion.id =:sesion " + "AND sc.idConcepto =:idConcepto");

		q.setParameter("sesion", sesion);
		q.setParameter("idConcepto", idConcepto);

		List results = q.getResultList();

		
		
		if (!results.isEmpty()) {

			sesionRetorno = (SesionConcepto) results.get(0);
		}else{
			System.out.println("no existe la sesion que queremos");
			
		}

		
		return sesionRetorno;

	}

}
