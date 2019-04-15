package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Curso;
import model.Tarea;
import utils.AppException;
import utils.CursoView;
import base.BaseServiceImpl;
import base.ListaResponse;
import dao.CursoDAO;

@Stateless
public class CursoService extends BaseServiceImpl<Curso, CursoDAO> {

	@Inject
	private CursoDAO dao;

	@Inject
	private AlumnoService alumnoService;

	final private long userId = 1;

	@Override
	public CursoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	/** BORRAR
	 * Lista de curso en el cual el alumno con idAlumno esta inscripto
	 **/
	public ListaResponse<CursoView> listarCursoPorAlumno(Long idAlumno) {
		System.out.println("listar curso alumno service");
		return dao.listarCursoPorAlumno(idAlumno);
	}

	/** BORRAR
	 * Lista Curso service. Lista de curso disponibles Para la lista de su
	 * nombre y descripcion y id
	 **/
	public ListaResponse<CursoView> listarCurso(Long idAlumno) {
		System.out.println("listar curso service");
		ListaResponse<CursoView> res = getDao().listarCurso(idAlumno);
		return res;
	}

	/**
	 * Lista Curso disponible service. Lista de curso disponibles Para la lista
	 * de su nombre y descripcion y id
	 * 
	 * @throws AppException
	 **/
	public ListaResponse<CursoView> listarCursoDisponible(Long idAlumno) throws AppException {
		System.out.println("listar curso disponible service");
		/** Lista de cursos view a ser devueltos. */
		List<CursoView> cursos = new ArrayList<CursoView>();
		ListaResponse<CursoView> respuesta = new ListaResponse<CursoView>();

		/***Trae todo los cursos sin distincion*/
		List<Curso> res = getDao().listarTodosLosCursos();

		if (!res.isEmpty()) {
			for (Curso curso : res) {
				System.out.println("Curso numero: " + curso.getId() + " - "
						+ curso.getDescripcion());
			}
		} else {
			System.out.println("curso vacio. No existe ningun curso");
		}

		/** se obtiene el alumno. */
		Alumno alumno = alumnoService.obtener(idAlumno);

		/**
		 * Por cada curso donde el alumno no este es un curso disponible y
		 * viceversa
		 */
		Integer total = 0;
		for (Curso curso : res) {
			if (!curso.getListaAlumno().contains(alumno)) {
				System.out.println("curso que no contiene al alumno es: "
						+ curso.getId() + "-" + curso.getDescripcion());

				CursoView as = new CursoView();
				as.setId(curso.getId());
				as.setAlumno(idAlumno);
				as.setNombre(curso.getNombre());
				as.setDescripcion(curso.getDescripcion());
				cursos.add(as);
				total++;
			} else {
				System.out.println("el alumno :" + idAlumno
						+ " esta contenido en el curso de: "
						+ curso.getDescripcion());
			}
		}
		
		/**parametros de la lista**/
		respuesta.setRows(cursos);
		respuesta.setCount(total);
		
		return respuesta;
	}


	/**
	 * Lista de cursos en el que el alumno esta inscripto
	 * 
	 * @throws AppException
	 **/
	public ListaResponse<CursoView> listarCursoInscriptos(Long idAlumno) throws AppException {
		System.out.println("listar curso disponible service");
		/** Lista de cursos view a ser devueltos. */
		List<CursoView> cursos = new ArrayList<CursoView>();
		ListaResponse<CursoView> respuesta = new ListaResponse<CursoView>();

		/***Trae todo los cursos sin distincion*/
		List<Curso> res = getDao().listarTodosLosCursos();

		if (!res.isEmpty()) {
			for (Curso curso : res) {
				System.out.println("Curso numero: " + curso.getId() + " - "
						+ curso.getDescripcion());
			}
		} else {
			System.out.println("curso vacio. No existe ningun curso");
		}

		/** se obtiene el alumno. */
		Alumno alumno = alumnoService.obtener(idAlumno);

		/**
		 * Por cada curso donde el alumno no este es un curso disponible y
		 * viceversa
		 */
		Integer total = 0;
		for (Curso curso : res) {
			if (!curso.getListaAlumno().contains(alumno)) {
				System.out.println("curso que no contiene al alumno es: "
						+ curso.getId() + "-" + curso.getDescripcion());
			} else {
				System.out.println("el alumno :" + idAlumno
						+ " esta contenido en el curso de: "
						+ curso.getDescripcion());
				CursoView as = new CursoView();
				as.setId(curso.getId());
				as.setAlumno(idAlumno);
				as.setNombre(curso.getNombre());
				as.setDescripcion(curso.getDescripcion());
				cursos.add(as);
				total++;
			}
		}
		
		/**parametros de la lista**/
		respuesta.setRows(cursos);
		respuesta.setCount(total);
		
		return respuesta;
	}

	
	
	
	
	/**
	 * Agregar alumno
	 */

	public void agregarAlumno(Long id, Curso entity,
			HttpServletRequest httpRequest) throws AppException {
		try {
			System.out.println("Modificar de curso service agregar alumno");
			entity.setFechaModificacion(new Date());
			entity.setUsuarioModificacion(userId);
			entity.setIpModificacion(httpRequest.getRemoteAddr());

			// Se agrega el alumno en cuestion
			Alumno al = new Alumno();
			Long idAlumno = entity.getAlumno();
			al = alumnoService.obtener(idAlumno);
			entity.agregarAlumno(al);

			getDao().inscribirse(id, entity, idAlumno);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/** Tarea una lista de tareas **/
	public List<Tarea> listaTarea(Long id) throws AppException {
		List<Tarea> lista = new ArrayList<Tarea>();
		try {

			lista = getDao().listaTarea(id);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
		return lista;
	}

}
