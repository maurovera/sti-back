package dao;

import javax.ejb.Stateless;
import javax.inject.Inject;

import utils.AppException;
import model.Concepto;
import model.Tema;
import base.AdministracionBase;
import base.BaseDAO;

@Stateless
public class ConceptoDAO extends BaseDAO<Concepto> {

	@Inject
	AdministracionBase adm;

	@Inject
	TemaDAO temaDao;

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Concepto.class;
	}

	/**
	 * Se agrega al arbol asignatura un Concepto.
	 * 
	 * @param entity
	 * @throws AppException
	 */
	public void insert(Concepto entity) throws AppException {
		System.out.println("insert concepto");
		em.persist(entity);
		Tema tema = (Tema) em.find(Tema.class, entity.getTema().getId());
		System.out.println("el id asig: " + tema.getAsignatura().getId());
		Long idAsignatura = tema.getAsignatura().getId();
		/**
		 * Se le llama al administradorBase Para agregar concepto a la red
		 * bayesiana.
		 */
		//adm.agregarConceptoRed(entity, idAsignatura);
	}

	/**
	 * Modificar Concepto. Con red bayesiana
	 * 
	 * @param id
	 * @param dto
	 * @throws AppException
	 *             OBS: Para tener en cuenta. Se vuelve a calcular todo aqui.
	 *             Porque se cambia el tema de apriori y demas cosas. Afecta a
	 *             los ejercicios. Por el momento no se toma en cuenta eso.
	 */
	public void modify(Long id, Concepto dto) throws AppException {
		System.out.println("modify concepto");
		Concepto entity = (Concepto) em.find(getEntity(), id);

		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		/** Nombre viejo para modificar la red bayesiana **/
		String tituloViejo = entity.getNombre();
		em.merge(dto);

		// / Se obtiene el id del tema para obtener el id de asignatura
		/*Tema tema = (Tema) em.find(Tema.class, dto.getTema().getId());
		System.out.println("el id asig: " + tema.getAsignatura().getId());
		Long idAsignatura = tema.getAsignatura().getId();
		*/
		/** Modificacion en la red bayesiana **/
		//adm.modificarConceptoRed(dto, tituloViejo, idAsignatura);

	}

	/**
	 * Eliminar un concepto de la red bayesiana
	 * 
	 * @param id
	 * @throws AppException
	 *             Obs. Pasa lo mismo con eliminar aqui. Se tiene que ver la
	 *             lista de ejercicios. y demas cosas. Por el momento no se
	 *             cambia nada.
	 * 
	 */
	public void delete(Long id) throws AppException {
		Concepto entity = (Concepto) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		em.remove(entity);
		/**
		 * Elimina el tema de la red
		 **/
		// / Se obtiene el id del tema para obtener el id de asignatura
		/*Tema tema = (Tema) em.find(Tema.class, entity.getTema().getId());
		System.out.println("el id asig: " + tema.getAsignatura().getId());
		Long idAsignatura = tema.getAsignatura().getId();
		*/
		//adm.eliminarConceptoRed(entity, idAsignatura);
	}

}
