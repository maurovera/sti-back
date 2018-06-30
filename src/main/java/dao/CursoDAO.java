package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Alumno;
import model.Curso;
import utils.CursoView;
import base.BaseDAO;
import base.ListaResponse;

@Stateless
public class CursoDAO extends BaseDAO<Curso> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Curso.class;
	}

	/**
	 * Lista de cursos al cual no esta inscripto el alumno con idAlumno.
	 * 
	 * @serialData 26062018
	 * @since 1
	 * @return listaCurso con id de alumnos asociados.
	 * @param idAlumno
	 ***/

	public ListaResponse<CursoView> listarCurso(Long idAlumno) {
		System.out.println("Listar curso no inscripto dao");
		// se construye la respuesta
		List<CursoView> res1 = new ArrayList<CursoView>();
		ListaResponse<CursoView> res = new ListaResponse<CursoView>();
		// Query para traer la lista de curso
		Query query = em.createQuery("SELECT c FROM Alumno a "
				+ "JOIN a.listaCurso c " + "WHERE a.id not in :lista");
		List<Long> lista = new ArrayList<Long>();
		lista.add(idAlumno);
		System.out.println("es el numero: " +  idAlumno);
		query.setParameter("lista", lista);
		List<Curso> resultado = query.getResultList();
		System.out.println("lista " +  resultado);
		for (Curso curso : resultado) {
			CursoView cu = new CursoView();
			cu.setId(curso.getId());
			cu.setNombre(curso.getNombre());
			cu.setDescripcion(curso.getDescripcion());
			for (Alumno alumno : curso.getListaAlumno()) {
				cu.addAlumnoId(alumno.getId());
			}

			res1.add(cu);
		}

		int total = 0;
		if (resultado != null)
			total = resultado.size();

		res.setRows(res1);
		res.setCount(total);
		return res;

	}

	/**
	 * Lista de cursos al cual el alumno con idAlumno esta inscripto.
	 * 
	 * @serialData 26062018
	 * @since 1
	 * @return listaCurso con id de alumnos asociados.
	 * @param idAlumno
	 ***/
	public ListaResponse<CursoView> listarCursoPorAlumno(Long idAlumno) {
		System.out.println("Lista de curso que el alumno esta inscripto");
		List<CursoView> res1 = new ArrayList<CursoView>();
		ListaResponse<CursoView> res = new ListaResponse<CursoView>();
		// Query para traer la lista de curso
		Query query = em.createQuery("SELECT c FROM Alumno a "
				+ "JOIN a.listaCurso c " + "WHERE a.id in :lista");
		List<Long> lista = new ArrayList<Long>();
		lista.add(idAlumno);
		query.setParameter("lista", lista);
		List<Curso> resultado = query.getResultList();

		for (Curso curso : resultado) {
			CursoView cu = new CursoView();
			cu.setId(curso.getId());
			cu.setNombre(curso.getNombre());
			cu.setDescripcion(curso.getDescripcion());
			for (Alumno alumno : curso.getListaAlumno()) {
				cu.addAlumnoId(alumno.getId());
			}

			res1.add(cu);
		}

		int total = 0;
		if (resultado != null)
			total = resultado.size();

		res.setRows(res1);
		res.setCount(total);
		return res;

	}

}
