package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.EstiloAprendizaje;
import seguridad.Usuario;
import utils.AppException;
import base.BaseServiceImpl;
import dao.EstiloAprendizajeDAO;

@Stateless
public class EstiloAprendizajeService extends BaseServiceImpl<EstiloAprendizaje, EstiloAprendizajeDAO> {

	@Inject
	private EstiloAprendizajeDAO dao;
	
	@Inject
	private AlumnoService alumnoService;

	@Override
	public EstiloAprendizajeDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	

	/**
	 * @{@inheritDoc
	 */
	@Override
	public EstiloAprendizaje insertar(EstiloAprendizaje entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("implements de servicio estilo aprendizaje");
			Usuario user = getCurrentUser();
			entity.setFechaCreacion(new Date());
			entity.setUsuarioCreacion(user.getId());
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			validate(entity);
			getDao().insert(entity);
			
			/**Obtenemos el id del alumno y modificamos*/
			Long idAlu = entity.getIdAlumno();
			Alumno alumno = alumnoService.obtener(idAlu);
			alumno.setEstilo(entity);
			alumno.setEstiloActual(entity.getPrimerEstilo());
			alumnoService.modificar(idAlu, alumno, httpRequest);
			
			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

}