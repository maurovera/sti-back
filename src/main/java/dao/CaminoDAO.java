package dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Camino;
import utils.AppException;
import base.BaseDAO;


@Stateless
public class CaminoDAO extends BaseDAO<Camino> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Camino.class;
	}
	
	/** Trae el camino anterior*/
	public Camino caminoAnterior(Long idAlumno, Long idTarea, Long idConcepto, Long idAsig) throws AppException  {
		System.out.println("camino anterior dao");
		
		// Query para traer el camino anterior
		Camino caminoAnterior = null;
		Boolean parar = false;
		Query query = em.createQuery("Select c from Camino c where"
				+ " c.idAlumno = :alumno and "
				+ "c.idTarea = :tarea and "
				+ "c.idConcepto = :concepto and "
				+ "c.idAsignatura = :asignatura and "
				+ "c.parar = :parar "
				+ "order by c.id desc");
		query.setParameter("alumno", idAlumno);
		query.setParameter("tarea", idTarea);
		query.setParameter("concepto", idConcepto);
		query.setParameter("asignatura", idAsig);
		query.setParameter("parar", parar);
		query.setMaxResults(1);
		
		List results = query.getResultList();
		
		if (!results.isEmpty()){
			
			caminoAnterior = (Camino) results.get(0);
		}
		
		return caminoAnterior;

	}

}