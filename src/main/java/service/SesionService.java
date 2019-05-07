package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Ejercicio;
import model.Material;
import model.Sesion;
import model.Tarea;
import seguridad.Usuario;
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

	
	/**
	 * registrarSesionMaterial es lo mismo que este
	 * Se registra una sesion de ejercicios o materiales. 
	 * esto queda indistinto 
	 * **/
	public Sesion registrarSesion(Long idAlumno, Long idTarea, HttpServletRequest httpRequest)
			throws AppException {
		Sesion sesion = new Sesion();
		try {
			
			Usuario user = getCurrentUser();
			sesion.setFechaCreacion(new Date());
			sesion.setUsuarioCreacion(user.getId());
			sesion.setIpCreacion(httpRequest.getRemoteAddr());
			sesion.setEntrada(new Date(System.currentTimeMillis()));
			sesion.setEstadoTerminado(false);
			sesion.setTestFinal(false);
			sesion.setCantidadEjerciciosResueltos(0);
			sesion.setCantidadIntentos(0);
			Alumno alumno = alumnoService.obtener(idAlumno);
			Tarea tarea = tareaService.obtener(idTarea);
			sesion.setAlumno(alumno);
			sesion.setTarea(tarea);
			System.out.println("llegue aqui antes del validate");
			validate(sesion);
			dao.insert(sesion);
			
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

		return sesion;
	}

	
	/**
	 * Queda como sesionMaterialAnterior
	 * es lo mismo porque trae toda la sesion en si
	 * **/
	public Sesion sesionAnterior(Long idAlumno, Long idTarea)
			throws AppException {
		try {
			return dao.sesionAnterior(idAlumno, idTarea);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

	}
	
	
	/**
	 * Queda como sesionMaterialAnterior
	 * es lo mismo porque trae toda la sesion en si
	 * **/
	public Sesion sesionAnteriorConNuevo(Long idAlumno, Long idTarea, HttpServletRequest httpRequest)
			throws AppException {
		
			Sesion sesion =  dao.sesionAnterior(idAlumno, idTarea);
			if(sesion == null){
				sesion = registrarSesion(idAlumno, idTarea, httpRequest);			
			}
			
			return sesion;
		
	}
	
	/**
	 * registrar la sesion final. donde varia en que se llama solo en un lugar 
	 * y tambien que test final es true
	 * **/
	public Sesion registrarSesionFinal(Long idAlumno, Long idTarea, HttpServletRequest httpRequest)
			throws AppException {
		Sesion sesion = new Sesion();
		try {
			
			Usuario user = getCurrentUser();
			sesion.setFechaCreacion(new Date());
			sesion.setUsuarioCreacion(user.getId());
			sesion.setIpCreacion(httpRequest.getRemoteAddr());
			sesion.setEntrada(new Date(System.currentTimeMillis()));
			sesion.setEstadoTerminado(false);
			sesion.setTestFinal(true);
			sesion.setCantidadEjerciciosResueltos(0);
			sesion.setCantidadIntentos(0);
			Alumno alumno = alumnoService.obtener(idAlumno);
			Tarea tarea = tareaService.obtener(idTarea);
			sesion.setAlumno(alumno);
			sesion.setTarea(tarea);
			System.out.println("llegue aqui antes del validate");
			validate(sesion);
			dao.insert(sesion);
			
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

		return sesion;
	}
	


	/**
	 * Inserta un ejercicio resuelto
	 **/
	public void insertarEjercicioResuelto(Long idSesion, Sesion sesion,
			Ejercicio ejercicioResuelto) throws AppException {
		try {
			
			System.out.println("Insertar ejercicio resuelto service: "+ ejercicioResuelto.getId());
			sesion.setFechaModificacion(new Date());
			sesion.setUsuarioModificacion(userId);
			sesion.setIpModificacion("127.1.1.1");// por el momento esto porque no se llama de afuera
			sesion.addEjercicioResuelto(ejercicioResuelto);
			

			getDao().insertarEjerciciosResueltos(idSesion, sesion);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

	}
	
	
	
	/**
	 * inserta un material visto en la sesion
	 **/
	public void insertarMaterialVisto(Long idSesion, Sesion sesion,
			Material material) throws AppException {
		try {
			
			System.out.println("Insertar material visto service");
			sesion.setFechaModificacion(new Date());
			sesion.setUsuarioModificacion(userId);
			sesion.setIpModificacion("127.1.1.1");// por el momento esto porque no se llama de afuera
			sesion.addMaterialVisto(material);
			

			getDao().insertarMaterialVisto(idSesion, sesion);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

}
