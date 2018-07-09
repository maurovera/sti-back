package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import model.Ejercicio;
import utils.AppException;
import utils.EjercicioView;
import base.AdministracionBase;
import base.BaseDAO;
import base.ListaResponse;

@Stateless
public class EjercicioDAO extends BaseDAO<Ejercicio>{

	
	@Inject
	AdministracionBase adm;
	
	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Ejercicio.class;
	}
	
	
	/**
	 * Funcion para insertar un ejercicio
	 ***/
	public void insert(Ejercicio entity) throws AppException {
		System.out.println("inserte nuevo ejercicio");
		Long idAsignatura = entity.getIdAsignatura();
		em.persist(entity);
		/**Funcion para insertar un ejercicio*/
		adm.agregarEjercicioRed(entity, idAsignatura);
	}
	
	
	
	public void modify(Long id, Ejercicio dto) throws AppException {
		System.out.println("Modify de ejercicio Dao");
		Ejercicio entity = (Ejercicio) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
		em.merge(dto);
	}

	
	/**
	 * Lista de ejercicios para el select 
	 * de tarea detalle en la vista de tareadetalle.
	 * 
	 */
	public ListaResponse<EjercicioView> listarEjercicio() {

		// se construye la respuesta
		List<EjercicioView> res1 = new ArrayList<EjercicioView>();
		ListaResponse<EjercicioView> res = new ListaResponse<EjercicioView>();

		String jpql = "SELECT e.id, e.enunciado FROM Ejercicio e";
		Query query = em.createQuery(jpql);
		List<Object[]> resultado = query.getResultList();

		for (int i = 0; i < resultado.size(); i++) {
			EjercicioView eje = new EjercicioView();
			Long arg0 = (Long) resultado.get(i)[0];
			String arg1 = (String) resultado.get(i)[1];
			eje.setId(arg0);
			eje.setEnunciado(arg1);
			res1.add(eje);
		}

		int total = 0;
		if(resultado != null)
			total = resultado.size();

		res.setRows(res1);
		res.setCount(total);
		return res;

	}
	
	
}
