package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import seguridad.Usuario;
import seguridad.UsuarioService;
import utils.AppException;
import model.Alumno;
import model.Profesor;
import base.BaseServiceImpl;
import dao.ProfesorDAO;

@Stateless
public class ProfesorService extends BaseServiceImpl<Profesor, ProfesorDAO> {

	@Inject
	private ProfesorDAO dao;

	@Inject
	private UsuarioService usuarioService;
	
	@Override
	public ProfesorDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	
	/** Se modifica para que reciba el insert **/
	@Override
	public Profesor insertar(Profesor entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			Usuario usuario = new Usuario();
			usuario.setApellido(entity.getApellido());
			usuario.setNombre(entity.getNombre());
			usuario.setEmail(entity.getEmail());
			usuario.setCedula(entity.getCedula());
			usuario.setEdad(entity.getEdad());
			usuario.setGenero(entity.getGenero());
			
			usuario.setUsername("profesorcambiar123");
			usuario.setPassword("cambiar123");
			usuario.setInterno(true);
			usuario.setPublico(false);
			usuario.setEsAlumno(false);
			usuario.setRecibirNotificacion(false);
			
			
			
			
			usuarioService.insertar(usuario, httpRequest);
			
			
			
			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}


}
