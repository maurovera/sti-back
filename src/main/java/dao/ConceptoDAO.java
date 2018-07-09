package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import utils.AppException;
import model.Concepto;
import model.Ejercicio;
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

		Long idAsignatura = entity.getIdAsignatura();
		em.persist(entity);
		/**
		 * Se le llama al administradorBase Para agregar concepto a la red
		 * bayesiana.
		 */
		adm.agregarConceptoRed(entity, idAsignatura);
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
		Long idAsignatura = dto.getIdAsignatura();
		String tituloViejo = entity.getNombre();

		/**
		 * Como en concepto nunca se va a modificar el puto tema entonces se le
		 * asigna manualmente aqui
		 **/
		dto.setTema(entity.getTema());
		System.out.println("Puto temaasignado: " + dto.getTema().getId());
		em.merge(dto);

		/** Modificacion en la red */
		adm.modificarConceptoRed(dto, tituloViejo, idAsignatura);

	}

	/**
	 * Eliminar un concepto de la red bayesiana
	 * 
	 * @param id
	 * @throws AppException
	 *             Obs. Pasa lo mismo con eliminar aqui. Se tiene que ver la
	 *             lista de ejercicios. y demas cosas. Por el momento no se
	 *             cambia nada.
	 * @since 1.2 Volvemos por el 1.2 Donde vamos a eliminar los ejercicios que
	 *        estan por debajo. La gran puta que dificil esta mierda. Es muy
	 *        engorroso.
	 * 
	 */
	public void delete(Long id) throws AppException {
		Concepto entity = (Concepto) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		// id asignatura para remover el concepto en al red bayesiana
		Long idAsignatura = entity.getTema().getAsignatura().getId();
		/******************** Since 1.2 Esto se agrega. 
		 * Aqui lo que se hace es eliminar los ejercicios asociados
		 **************************/
		
		Ejercicio ejercicio = null;

		
		Query query = em.createQuery("SELECT e.id FROM Ejercicio e "
				+ "left join e.listaConceptos c where c.id =:id"
				);
		
		query.setParameter("id", id);

		List<Long> resultado = query.getResultList();

		ArrayList<Ejercicio> ejercicios = new ArrayList<Ejercicio>();
		/** Es para borrar los ejercicios del concepto a eliminar***/
		for (Long idEjercicio : resultado) {

			ejercicio = em.find(Ejercicio.class, idEjercicio);
			ejercicio.getListaConceptos().remove(entity);
			em.persist(ejercicio);
			ejercicios.add(em.find(Ejercicio.class, idEjercicio));

		}
		
		/** re calcula los ejercicios asociados a los otros conceptos.*/
		adm.reCalcularEjercicios(ejercicios, idAsignatura);

		/** fin since 1.2 **/
		em.remove(entity);
		/**
		 * Elimina el tema de la red
		 **/
		adm.eliminarConceptoRed(entity, idAsignatura);
	}

}
