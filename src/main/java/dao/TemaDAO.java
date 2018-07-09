package dao;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Tema;
import utils.AppException;
import base.AdministracionBase;
import base.BaseDAO;

@Stateless
public class TemaDAO extends BaseDAO<Tema> {
	
	@Inject
	AdministracionBase adm;
	
	
	
	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Tema.class;
	}
	
	
	/**
	 * Funcion que agrega un tema.
	 * @param entity
	 * @throws AppException
	 */
	public void insert(Tema entity) throws AppException {
		System.out.println("insert tema");
		em.persist(entity);
		
		/** Se le llama al administradorBase Para agregar tema 
		 * a la red bayesiana.*/
		adm.agregarTemaRed(entity);
		
	}
	
	/**
	 * Modificar tema. Con red bayesiana
	 * @param id
	 * @param dto
	 * @throws AppException
	 */
	public void modify(Long id, Tema dto) throws AppException {
		Tema entity = (Tema) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		/**Nombre viejo para modificar la red bayesiana**/
		String tituloViejo = entity.getNombre();

		/**Como siempre va a settear la misma asignatura. nos aseguramos
		 * que en front no provoque errores**/
		dto.setAsignatura(entity.getAsignatura());
		System.out.println("Asignatura Asignada: "+dto.getAsignatura().getId());
		
		em.merge(dto);
		/**Modificacion en la red bayesiana**/
		adm.modificarTemaRed(dto,tituloViejo);
		
	}
	
	
	/**
	 * Eliminar un tema de la red bayesiana
	 * @param id
	 * @throws AppException
	 * Obs.: eliminar tambien los conceptos por debajo. Cuando Elimina un tema
	 * No se si agregarle. 
	 * hay un for en administracionBean
	 */
	public void delete(Long id) throws AppException {
		Tema entity = (Tema) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		em.remove(entity);
		/**Elimina el tema de la red
		 **/
		adm.eliminarTemaRed(entity);
	}

}
