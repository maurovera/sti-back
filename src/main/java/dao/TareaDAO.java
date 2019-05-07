package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Camino;
import model.Tarea;
import base.BaseDAO;
import base.ListaResponse;

@Stateless
public class TareaDAO extends BaseDAO<Tarea> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Tarea.class;
	}
	
	/**
	 * Lista de tareas de alumno.
	 * 
	 ***/
	public List<Tarea> listarTareaAlumno(Long idCurso) {
		System.out.println("Lista de tarea de alumno  DAO");
		List<Tarea> res1 = new ArrayList<Tarea>();
		
		Query query = em
				.createQuery("SELECT t FROM Tarea t "
						+ " where"
						+ " t.curso.id =:idCurso "
						+ " order by t.id"
						);

		query.setParameter("idCurso", idCurso);
		
		
		res1 = query.getResultList();

		return res1;

	}

}