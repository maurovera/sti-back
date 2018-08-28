package dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.SesionMaterial;
import utils.AppException;
import base.BaseDAO;


@Stateless
public class SesionMaterialDAO extends BaseDAO<SesionMaterial> {
	

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return SesionMaterial.class;
	}

	/** Trae la sesion anterior del alumno en cuanto a la tarea en si **/
	public SesionMaterial sesionMaterialAnterior(Long idAlumno, Long idTarea) throws AppException  {
		System.out.println("Sesion Material anterior dao");
		
		// Query para traer la sesion anterior

		Query query = em.createQuery("Select s from SesionMaterial s where s.alumno.id = :alumno and s.tarea.id = :tarea  order by s.id desc");
		query.setParameter("alumno", idAlumno);
		query.setParameter("tarea", idTarea);
		query.setMaxResults(1);
		SesionMaterial sesionAnterior = (SesionMaterial) query.getSingleResult();
		
		//if (sesionAnterior == null) {
			//throw new AppException(404, "Not Found");
		//}
		
		
		return sesionAnterior;

	}
	
	
	/**
	 * Modificamos la sesion agregandole un material visto.	
	 * @param id
	 * @param dto
	 * @throws AppException
	 */
	public void insertarMaterialVisto(Long id, SesionMaterial dto) throws AppException {
		/**Inscribirse al curso.
		 ***/
		System.out.println("Insertar material visto DAO");
		
		SesionMaterial entity = (SesionMaterial) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
	
		em.merge(dto);
	
	}

}
