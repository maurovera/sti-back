package dao;

import java.util.ArrayList;
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
			Regla regla) {

		/** Lista de retorno **/
		List<Material> listaRetorno = new ArrayList<Material>();
		Material materialR = new Material();
		Query q;
		/**
		 * Query que trae la lista de materiales disponibles
		 * Si no hay materiales disponibles aun. Se cambia nomas la consulta
		 ***/
		if (listaMateriales.isEmpty() || listaMateriales == null) {

			q = em.createQuery("SELECT distinct m FROM Material m " + "WHERE"
					+ " m.concepto =:concepto " + "AND m.nivel =:nivel "
					+ "AND m.idAsignatura =:asignatura "
					+ "AND m.estilo =:estilo");

			q.setParameter("concepto", regla.getConcepto());
			q.setParameter("nivel", regla.getNivel());
			q.setParameter("estilo", regla.getEstilo());
			q.setParameter("asignatura", regla.getIdAsignatura());
			
		} else {
			q = em.createQuery("SELECT distinct m FROM Material m " + "WHERE"
					+ " m not in :listaMateriales "
					+ "AND m.concepto =:concepto " + "AND m.nivel =:nivel "
					+ "AND m.idAsignatura =:asignatura "
					+ "AND m.estilo =:estilo");

			q.setParameter("listaMateriales", listaMateriales);
			q.setParameter("concepto", regla.getConcepto());
			q.setParameter("nivel", regla.getNivel());
			q.setParameter("estilo", regla.getEstilo());
			q.setParameter("asignatura", regla.getIdAsignatura());
			
		}
		
		listaRetorno = q.getResultList();
		
		if (listaRetorno == null || listaRetorno.isEmpty()) {
			materialR = null;
			System.out.println("lista retorno vacia o lista retorno nula");
			//throw new AppException(404, "Not Found lista materiales");
		}else{
			System.out.println("lista retorno no nula. ver que valor trae esa mierda: ");
			materialR = listaRetorno.get(0);
			System.out.println("material seleccionado: " + materialR.getId());
		}

	
		// for (Material m : listaRetorno) {
		// System.out.println(m.getId());
		// }
		
		return materialR;

	}

}