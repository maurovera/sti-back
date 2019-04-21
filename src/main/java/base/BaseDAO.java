package base;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import utils.AppException;

/**
 *
 * @author mbaez
 * @param <G>
 */
public abstract class BaseDAO<G extends BaseEntity> {

	@PersistenceContext(unitName = "stiPU")
	protected EntityManager em;

	/**
	 * Retorna el class de Entity utilizado para las operaciones del tipo CRUD.
	 *
	 * @return el class.
	 */
	public abstract Class getEntity();

	/**
	 *
	 * @param sb
	 * @param orderBy
	 * @param orderDir
	 */
	public void buildOrder(StringBuilder sb, String orderBy, String orderDir) {
		if (orderBy != null && !orderBy.isEmpty()) {
			sb.append(" ORDER BY c.").append(orderBy).append(" ")
					.append(orderDir);
		}
	}

	/**
	 *
	 * @param sb
	 * @param filtros
	 */
	public void buildWhere(StringBuilder sb, HashMap<String, Object> filtros) {
		if (filtros == null || filtros.isEmpty()) {
			return;
		}
		int tokens = filtros.keySet().size();
		int token = 1;
		sb.append(" WHERE ");

		for (String key : filtros.keySet()) {

			/**
			 * if (filtros.get(key) instanceof String){
			 * System.out.println("soy string"); sb.append(key).append(
			 * "= :").append(key); }
			 **/
			if (filtros.get(key) instanceof String) {
				System.out.println("soy string");
				sb.append(" LOWER(c.").append(key).append(") LIKE LOWER(:")
						.append(key).append(")");
			} else {
				System.out.println("soy un interger: " + key);
				sb.append(" c.").append(key).append(".id").append(" = :").append(key);
			}
			// se añade el 'AND' si hay más caracteres.
			if (token < tokens) {
				sb.append(" AND ");
			}
			token++;
		}
	}

	/**
	 *
	 * @param q
	 * @param filtros
	 */
	public void setParametrers(Query q, HashMap<String, Object> filtros) {
		if (filtros == null) {
			return;
		}
		for (String key : filtros.keySet()) {
			Object value = filtros.get(key);
			if (filtros.get(key) instanceof String) {
				value = "%" + value + "%";

			} else {
				// no agrego nada si es number . mauro cambio
				System.out.println("entre aqui porque no soy string " + key + ": "
						+ value);
				// value = ((Long)value).longValue();
				//value = Long.valueOf(value);
				/** Ojo. : Siempre va a machear a long**/
				value = Long.parseLong(value.toString());
				
			}
			
			q.setParameter(key,  value);
		}
	}

	/**
	 *
	 * @param filtros
	 * @return
	 */
	private int count(HashMap<String, Object> filtros) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(c) FROM ")
				.append(getEntity().getCanonicalName()).append(" c");
		buildWhere(query, filtros);
		Query q = em.createQuery(query.toString());
		setParametrers(q, filtros);
		return ((Long) q.getSingleResult()).intValue();
	}

	/**
	 *
	 * @param inicio
	 * @param cantidad
	 * @param orderBy
	 * @param odrerDir
	 * @param filtros
	 * @return
	 */
	public ListaResponse<G> listar(int inicio, int cantidad, String orderBy,
			String odrerDir, HashMap<String, Object> filtros) {

		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM ").append(getEntity().getCanonicalName())
				.append(" c");
		buildWhere(query, filtros);
		System.out.println("query generado  :" + query.toString());
		Query q = em.createQuery(query.toString());
		System.out.println("query generado q:"+ q.toString());
		setParametrers(q, filtros);
		q.setFirstResult(inicio).setMaxResults(cantidad);
		List<G> list = q.getResultList();
		int total = count(filtros);
		// se construye la respuesta
		ListaResponse<G> res = new ListaResponse<G>();
		res.setRows(list);
		res.setCount(total);
		return res;

	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public G get(Long id) throws AppException {
		try {
			return (G) em.find(getEntity(), id);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 *
	 * @param id
	 * @param dto
	 * @throws AppException
	 */
	public void modify(Long id, G dto) throws AppException {
		G entity = (G) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		em.merge(dto);
	}

	/**
	 *
	 * @param entity
	 * @throws AppException
	 */
	public void insert(G entity) throws AppException {
		System.out.println("inserte de base dao");
		em.persist(entity);
		
	}

	/**
	 *
	 * @param id
	 * @throws AppException
	 */
	public void delete(Long id) throws AppException {
		G entity = (G) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		em.remove(entity);
	}

	/**
	 * @param id
	 * @throws AppException
	 */
	public G verificarConstraint(Long id, StringBuilder query)
			throws AppException {
		try {
			Query q = em.createQuery(query.toString());
			q.setParameter("id", id);
			List<G> rows = q.getResultList();
			if (rows.size() > 0) {
				return rows.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(500, e.getMessage());
		}
	}
}
