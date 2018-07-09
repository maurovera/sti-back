package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import model.Alumno;
import model.Asignatura;
import model.Curso;
import utils.AppException;
import utils.AsignaturaView;
import utils.CursoView;
import base.AdministracionBase;
import base.BaseDAO;
import base.ListaResponse;

@Stateless
public class CursoDAO extends BaseDAO<Curso> {

	
	@Inject
	AdministracionBase adm;

	
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
		Query query = em.createQuery("SELECT c.id, a.id, c.nombre, c.descripcion FROM Curso c "
				+ "left join c.listaAlumno a where a.id !=:id or a.id is null"
				);
		
		query.setParameter("id", idAlumno);

		List<Object[]> resultado = query.getResultList();

		for (int i = 0; i < resultado.size(); i++) {
			CursoView as = new CursoView();
			Long arg0 = (Long) resultado.get(i)[0];
			Long arg1 = (Long) resultado.get(i)[1];
			String arg2 = (String) resultado.get(i)[2];
			String arg3 = (String) resultado.get(i)[3];
			as.setId(arg0);
			as.setAlumno(arg1);
			as.setNombre(arg2);
			as.setDescripcion(arg3);
			res1.add(as);
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
		Query query = em.createQuery("SELECT c.id, a.id, c.nombre, c.descripcion FROM Curso c "
				+ "join c.listaAlumno a where a.id =:id"
				);
		
		query.setParameter("id", idAlumno);

		List<Object[]> resultado = query.getResultList();

		for (int i = 0; i < resultado.size(); i++) {
			CursoView as = new CursoView();
			Long arg0 = (Long) resultado.get(i)[0];
			Long arg1 = (Long) resultado.get(i)[1];
			String arg2 = (String) resultado.get(i)[2];
			String arg3 = (String) resultado.get(i)[3];
			as.setId(arg0);
			as.setAlumno(arg1);
			as.setNombre(arg2);
			as.setDescripcion(arg3);
			res1.add(as);
		}
		
		int total = 0;
		if (resultado != null)
			total = resultado.size();

		res.setRows(res1);
		res.setCount(total);
		return res;

	}
	
	
	
	/**
	 *
	 * @param id
	 * @param dto
	 * @throws AppException
	 */
	public void inscribirse(Long id, Curso dto, Long idAlumno) throws AppException {
		/**Inscribirse al curso.
		 ***/
		System.out.println("inscribirseDAO");
		Curso entity = (Curso) em.find(getEntity(), id);
		if (entity == null) {
			throw new AppException(404, "Not Found");
		}
	
		em.merge(dto);
		
		Asignatura asig = dto.getAsignatura();
		Long idAsignatura = asig.getId();
		adm.calcularProbabilidades(asig);
		adm.crearRedAlumno(idAsignatura, idAlumno);
	
	}


}
