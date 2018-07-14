package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Alumno;
import model.Ejercicio;
import model.Sesion;
import model.Tarea;
import utils.AppException;
import base.BaseServiceImpl;
import dao.SesionDAO;

@Stateless
public class SesionService extends BaseServiceImpl<Sesion, SesionDAO> {

	@Inject
	private SesionDAO dao;

	@Inject
	private AlumnoService alumnoService;

	@Inject
	private TareaService tareaService;
	
	final private long userId = 1;

	@Override
	public SesionDAO getDao() {

		return dao;
	}

	public Long registrarSesion(Long idAlumno, Long idTarea)
			throws AppException {
		Sesion sesion = new Sesion();
		try {
			
			sesion.setFechaCreacion(new Date());
			sesion.setUsuarioCreacion(userId);
			sesion.setIpCreacion("127.1.1.1");
			

			sesion.setEntrada(new Date(System.currentTimeMillis()));
			sesion.setEstadoTerminado(false);
			sesion.setCantidadEjerciciosResueltos(0);
			Alumno alumno = alumnoService.obtener(idAlumno);
			Tarea tarea = tareaService.obtener(idTarea);
			sesion.setAlumno(alumno);
			sesion.setTarea(tarea);

			dao.insert(sesion);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

		return sesion.getId();
	}

	public Sesion sesionAnterior(Long idAlumno, Long idTarea)
			throws AppException {
		try {
			return dao.sesionAnterior(idAlumno, idTarea);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

	}

	public void insertarEjercicioResuelto(Long idSesion, Sesion sesion,
			Ejercicio ejercicioResuelto) throws AppException {
		try {
			
			System.out.println("Insertar ejercicio resuelto service");
			sesion.setFechaModificacion(new Date());
			sesion.setUsuarioModificacion(userId);
			sesion.setIpModificacion("127.1.1.1");// por el momento esto porque no se llama de afuera
			sesion.addEjercicioResuelto(ejercicioResuelto);
			

			getDao().insertarEjerciciosResueltos(idSesion, sesion);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

	}

}
