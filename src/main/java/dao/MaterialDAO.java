package dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.w3c.dom.ls.LSInput;

import model.Material;
import utils.AppException;
import utils.Regla;
import base.BaseDAO;

@Stateless
public class MaterialDAO extends BaseDAO<Material> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Material.class;
	}

	/**
	 * 
	 * Lista de materiales que aun no se han usado dentro de una sesionMaterial
	 * donde la sesion material esta compuesta por el alumno y la tarea en si.
	 * 
	 * 
	 * @param listaMateriales
	 *            Materiales de la sesion anterior.
	 * @param regla
	 *            . Donde viene el concepto, estilo y nivel
	 * @return Puede ser una lista de materiales disponibles o en todo caso un
	 *         solo material al azar dentro de esos materiales.
	 * @throws AppException
	 * 
	 */
	public Material materialesDisponibles(List<Material> listaMateriales,
			Regla regla) throws AppException {

		/** Lista de retorno **/
		List<Material> listaRetorno = null;
		Material materialR = null;
		Query q;
		/**
		 * Query que trae la lista de materiales disponibles
		 * Si no hay materiales disponibles aun. Se cambia nomas la consulta
		 ***/
		if (listaMateriales.isEmpty() || listaMateriales == null) {

			q = em.createQuery("SELECT distinct m FROM Material m " + "WHERE"
					+ " m.concepto =:concepto " + "AND m.nivel =:nivel "
					+ "AND m.estilo =:estilo");

			q.setParameter("concepto", regla.getConcepto());
			q.setParameter("nivel", regla.getNivel());
			q.setParameter("estilo", regla.getEstilo());
			
		} else {
			q = em.createQuery("SELECT distinct m FROM Material m " + "WHERE"
					+ " m not in :listaMateriales "
					+ "AND m.concepto =:concepto " + "AND m.nivel =:nivel "
					+ "AND m.estilo =:estilo");

			q.setParameter("listaMateriales", listaMateriales);
			q.setParameter("concepto", regla.getConcepto());
			q.setParameter("nivel", regla.getNivel());
			q.setParameter("estilo", regla.getEstilo());
			
		}
		
		listaRetorno = q.getResultList();
		materialR = listaRetorno.get(0);


		if (materialR == null) {
			throw new AppException(404, "Not Found lista materiales");
		}

		// for (Material m : listaRetorno) {
		// System.out.println(m.getId());
		// }
		System.out.println("material seleccionado: " + materialR.getId());
		return materialR;

	}

}