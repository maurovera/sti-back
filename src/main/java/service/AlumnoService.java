package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import seguridad.Usuario;
import seguridad.UsuarioService;
import utils.AppException;
import model.Alumno;
import base.BaseServiceImpl;
import dao.AlumnoDAO;

@Stateless
public class AlumnoService extends BaseServiceImpl<Alumno, AlumnoDAO> {

	@Inject
	private AlumnoDAO dao;

	@Inject
	private UsuarioService usuarioService;

	@Override
	public AlumnoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	/** Se modifica para que reciba el insert **/
	@Override
	public Alumno insertar(Alumno entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("implements de servicio de insert de alumno");
			Usuario user = getCurrentUser();
			entity.setFechaCreacion(new Date());
			entity.setUsuarioCreacion(user.getId());
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			validate(entity);
			getDao().insert(entity);

			/**
			 * Insert de usuario*
			 */
			Usuario u = usuarioService.insertarAlumno(entity, httpRequest);
			System.out.println("inserte usuario: "+ u.getId());
			
			entity.setUsuario(u.getId());
			getDao().modify(entity.getId(), entity);
			
			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

}
