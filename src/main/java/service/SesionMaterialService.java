package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Material;
import model.SesionMaterial;
import model.Tarea;
import utils.AppException;
import base.BaseServiceImpl;
import dao.SesionMaterialDAO;

@Stateless
public class SesionMaterialService extends BaseServiceImpl<SesionMaterial, SesionMaterialDAO> {

	@Inject
	private SesionMaterialDAO dao;

	@Inject
	private AlumnoService alumnoService;

	@Inject
	private TareaService tareaService;
	
	final private long userId = 1;

	@Override
	public SesionMaterialDAO getDao() {

		return dao;
	}

	public SesionMaterial registrarSesionMaterial(Long idAlumno, Long idTarea, HttpServletRequest httpRequest)
			throws AppException {
		SesionMaterial sesion = new SesionMaterial();
		try {
			
			sesion.setFechaCreacion(new Date());
			sesion.setUsuarioCreacion(userId);
			sesion.setIpCreacion(httpRequest.getRemoteAddr());
			

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

		return sesion;
	}

	public SesionMaterial sesionMaterialAnterior(Long idAlumno, Long idTarea)
			throws AppException {
		try {
			return dao.sesionMaterialAnterior(idAlumno, idTarea);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

	}

	public void insertarMaterialVisto(Long idSesion, SesionMaterial sesion,
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

