package dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import utils.AppException;
import model.Asignatura;
import model.Curso;
import model.Sesion;
import base.BaseDAO;

@Stateless
public class SesionDAO extends BaseDAO<Sesion> {
	
	final String SesionAnteriorQuery = "";

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Sesion.class;
	}

	/** Trae la sesion anterior del alumno en cuanto a la tarea en si **/
	public Sesion sesionAnterior(Long idAlumno, Long idTarea) throws AppException  {
		System.out.println("Sesion anterior dao");
		
		// Query para traer la sesion anterior

		Query query = em.createQuery("Select s from Sesion s where s.alumno.id = :alumno and s.tarea.id = :tarea  order by s.id desc");
		query.setParameter("alumno", idAlumno);
		query.setParameter("tarea", idTarea);
		query.setMaxResults(1);
		Sesion sesionAnterior = (Sesion) query.getSingleResult();
		
		//if (sesionAnterior == null) {
			//throw new AppException(404, "Not Found");
		//}
		
		
		return sesionAnterior;

	}
	
	
	/**
	 * Modificamos la sesion agregandole ejercicios resultos.	
	 * @param id
	 * @param dto
	 * @throws AppException
	 */
	public void insertarEjerciciosResueltos(Long id, Sesion dto) throws AppException {
		/**Inscribirse al curso.
		 ***/
		System.out.println("Insertar ejercicios resueltos DAO");
		
		Sesion entity = (Sesion) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
	
		em.merge(dto);
	
	}


}
