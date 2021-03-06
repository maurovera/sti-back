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

	/**
	 * Lista de curso en el cual el alumno con idAlumno esta inscripto
	 **/
	public ListaResponse<CursoView> listarCursoPorAlumno(Long idAlumno) {
		System.out.println("listar curso alumno service");
		return dao.listarCursoPorAlumno(idAlumno);
	}

	/**
	 * Lista Curso service. Lista de curso disponibles Para la lista de su
	 * nombre y descripcion y id
	 **/
	public ListaResponse<CursoView> listarCurso(Long idAlumno) {
		System.out.println("listar curso service");
		ListaResponse<CursoView> res = getDao().listarCurso(idAlumno);
		return res;
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

			//Se agrega el alumno en cuestion
			Alumno al = new Alumno();
			Long idAlumno = entity.getAlumno();
			al = alumnoService.obtener(idAlumno);
			entity.agregarAlumno(al);

			getDao().inscribirse(id, entity, idAlumno);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	
	/**Tarea una lista de tareas**/
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
