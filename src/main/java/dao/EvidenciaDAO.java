package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Concepto;
import model.Ejercicio;
import model.Evidencia;
import utils.AppException;
import base.BaseDAO;

@Stateless
public class EvidenciaDAO extends BaseDAO<Evidencia> {
	
	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Evidencia.class;
	}
	
	
	
	/**Lista de evidencia por id asignatura, idCurso
	 **/
	public List<Evidencia> listaEvidencia(Long idAsignatura, Long idCurso) throws AppException {

		System.out.println("Lista de evidencias por idAsignatura");
		List<Evidencia> lista = new ArrayList<Evidencia>();
		
		/**
		 * Query que lista los ejercicios asociados
		 ***/
		Query q = em.createQuery("SELECT e FROM Evidencia e "+
				"WHERE e.idAsignatura =:idAsignatura ");
				//+"e.idCurso" =:idCurso);

		q.setParameter("idAsignatura", idAsignatura);
		//q.setParameter("idCurso", idCurso);


		lista = q.getResultList();


		if (lista == null) {
			throw new AppException(404, "Not Found lista evidencia");
		}

		return lista;
	}


}
