package dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Sesion;
import utils.AppException;
import base.BaseDAO;

@Stateless
public class SesionDAO extends BaseDAO<Sesion> {
	
	final String SesionAnteriorQuery = "";

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Sesion.class;
	}

	/** Trae la sesion anterior del alumno en cuanto a la tarea en si 
	 * Replanear esta funcion porque ndoikoi**/
	public Sesion sesionAnterior(Long idAlumno, Long idTarea) throws AppException  {
		System.out.println("Sesion anterior dao");
		
		// Query para traer la sesion anterior
		Sesion sesionAnterior = null;
		Query query = em.createQuery("Select s from Sesion s where s.alumno.id = :alumno and s.tarea.id = :tarea  order by s.id desc");
		query.setParameter("alumno", idAlumno);
		query.setParameter("tarea", idTarea);
		query.setMaxResults(1);
		//Sesion sesionAnterior = (Sesion) query.getSingleResult();
		List results = query.getResultList();
		
		if (!results.isEmpty()){
			
			sesionAnterior = (Sesion) results.get(0);
		}
		
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
	
	
	/* Modificamos la sesion agregandole un material visto.
	 * Se repite la modificacion mas arriba. pero es a modo 
	 * de ser organizado
	 *  o en caso en el service cambiamos.
	 * 
	 * @param id
	 * @param dto
	 * @throws AppException
	 */
	public void insertarMaterialVisto(Long id, Sesion dto)
			throws AppException {
		/**
		 * Inscribirse al curso.
		 ***/
		System.out.println("Insertar material visto DAO");

		Sesion entity = (Sesion) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}

		em.merge(dto);
	}
}
