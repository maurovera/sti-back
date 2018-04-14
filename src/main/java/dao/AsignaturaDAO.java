package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Asignatura;
import model.Ejercicio;
import utils.AsignaturaView;
import base.BaseDAO;
import base.ListaResponse;

@Stateless
public class AsignaturaDAO extends BaseDAO<Asignatura> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Asignatura.class;
	}

	public List<Ejercicio> listarMauro() {
		Query query = em.createQuery("SELECT e FROM Concepto c "
				+ "JOIN c.listaEjercicio e " + "WHERE c.id in :listaConcepto");
		List<Long> listaConcepto = new ArrayList<Long>();
		listaConcepto.add(new Long(1));
		// listaConcepto.add(new Long(2));
		query.setParameter("listaConcepto", listaConcepto);

		return (List<Ejercicio>) query.getResultList();

	}

	/**
	 * Lista los ejercicios asociados a la asignatura que viene enn filtros.
	 * 
	 * @param inicio
	 * @param cantidad
	 * @param orderBy
	 * @param odrerDir
	 * @param filtros
	 * @return
	 */
	public ListaResponse<Ejercicio> listarEjercicio(int inicio, int cantidad,
			String orderBy, String odrerDir, HashMap<String, Object> filtros) {

		/**
		 * Traemos los conceptos Asociados a la asignatura
		 ***/
		List<Long> listaConceptos = null;
		listaConceptos = conceptos(filtros.get("asignatura"));
		System.out.println(listaConceptos.getClass());
		for (Long long1 : listaConceptos) {
			System.out.println(long1);
		}

		/**
		 * Query que lista los ejercicios asociados
		 ***/
		Query q = em.createQuery("SELECT distinct e FROM Concepto c "
				+ "JOIN c.listaEjercicio e " + "WHERE c.id in :listaConcepto");

		// List<Long> listaConcepto = new ArrayList<Long>();
		// listaConcepto.add(new Long(4));
		q.setParameter("listaConcepto", listaConceptos);

		// setParametrers(q, filtros);
		q.setFirstResult(inicio).setMaxResults(cantidad);
		List<Ejercicio> list = q.getResultList();
		for (Ejercicio e : list) {
			System.out.println(e.getEnunciado());
		}
		// int total = countEjercicios(listaConceptos);
		/** Se calcula el total por la lista nada mas. **/
		int total = 0;
		if (list != null)
			total = list.size();

		// se construye la respuesta
		ListaResponse<Ejercicio> res = new ListaResponse<Ejercicio>();
		res.setRows(list);
		res.setCount(total);
		return res;

	}

	/**
	 * Cuenta cuantos resultados devuelve la consulta de lista de ejercicios
	 * asociados.
	 * 
	 * @param filtros
	 * @return
	 */
	private int countEjercicios(List<Long> lista) {
		Query q = em.createQuery("SELECT count(*) FROM Concepto c "
				+ "JOIN c.listaEjercicio e " + "WHERE c.id in :listaConcepto");

		// List<Long> listaConcepto = new ArrayList<Long>();
		// listaConcepto.add(new Long(1));
		q.setParameter("listaConcepto", lista);
		return ((Long) q.getSingleResult()).intValue();
	}

	/**
	 * Método que retorna la lista de id de conceptos de una asignatura
	 * 
	 * @param idAsignatura
	 *            . Id correspondiente a la asignatura
	 * @return Lista de long de conceptos
	 */
	private List<Long> conceptos(Object idAsignatura) {

		// Se parsea a long el id de la asignatura.
		idAsignatura = Long.parseLong(idAsignatura.toString());

		// Consulta de los conceptos que pertenecen a la asignatura
		Query q = em
				.createQuery("SELECT c.id FROM Concepto c where c.tema in "
						+ "(Select t.id from Tema t where t.asignatura.id = :idAsignatura)");
		q.setParameter("idAsignatura", idAsignatura);
		List<Long> lista = q.getResultList();

		return lista;
	}

	/**
	 * Lista de asignaturas para el select de asignatura en la vista de curso.
	 * 
	 * @param inicio
	 * @param cantidad
	 * @param orderBy
	 * @param odrerDir
	 * @param filtros
	 * @return
	 */
	public ListaResponse<AsignaturaView> listarAsignatura() {

		// se construye la respuesta
		List<AsignaturaView> res1 = new ArrayList<AsignaturaView>();
		ListaResponse<AsignaturaView> res = new ListaResponse<AsignaturaView>();

		String jpql = "SELECT a.id, a.nombre FROM Asignatura a";
		Query query = em.createQuery(jpql);
		List<Object[]> resultado = query.getResultList();

		for (int i = 0; i < resultado.size(); i++) {
			AsignaturaView as = new AsignaturaView();
			Long arg0 = (Long) resultado.get(i)[0];
			String arg1 = (String) resultado.get(i)[1];
			as.setId(arg0);
			as.setNombre(arg1);
			res1.add(as);
		}

		int total = 0;
		if(resultado != null)
			total = resultado.size();

		res.setRows(res1);
		res.setCount(total);
		return res;

	}

}
