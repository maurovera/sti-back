package seguridad;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Alumno;
import model.Profesor;

import org.apache.shiro.crypto.hash.Md5Hash;

import utils.AppException;
import base.BaseServiceImpl;
import dao.AlumnoDAO;
import dao.ProfesorDAO;

@Stateless
public class UsuarioService extends BaseServiceImpl<Usuario, UsuarioDAO> {

	@Inject
	private UsuarioDAO dao;

	@Inject
	private AlumnoDAO alumnoDao;
	@Inject
	private ProfesorDAO profesorDao;

	@Inject
	private RolPermisoDao rolPermisoDao;

	/**
	 *
	 * @{@inheritDoc
	 */
	@Override
	public UsuarioDAO getDao() {
		return dao;
	}

	public UsuarioService() {
	}

	@Override
	public Usuario insertar(Usuario entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("llegue al insert de usuario");
			String encryptedToken = new Md5Hash(entity.getPassword(),
					entity.getCedula()).toString();
			String nick = entity.getUsername();
			entity.setUsername(entity.getCedula());
			entity.setPassword(encryptedToken);
			entity.setFechaCreacion(new Date());
			/** se usa 0l para crear usuario */
			entity.setUsuarioCreacion(0L);
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			entity.setInterno(false);
			/**
			 * Aca yo uso estatico, usuario debe tener un campo donde le indique
			 * que sea alumno o profesor que sea transient
			 ***/
			System.out.println("antes de roles");
			Integer rol;
			if (entity.getEsAlumno()) {
				rol = rolPermisoDao.getRolAlumno();
			} else {
				rol = rolPermisoDao.getRolProfesor();
			}

			entity.setRol(rol);
			validate(entity);
			System.out.println("despues de validate");
			getDao().insert(entity);
			System.out.println("entity guardado: " + entity.getCedula());

			/** Aqui guardaremos alumno o profesor **/
			if (entity.getEsAlumno()) {
				Alumno alu = new Alumno();
				alu.setApellidos(entity.getApellido());
				alu.setNombres(entity.getNombre());
				alu.setEdad(entity.getEdad());
				alu.setFechaCreacion(entity.getFechaCreacion());
				alu.setUsuarioCreacion(entity.getUsuarioCreacion());
				alu.setIpCreacion(entity.getIpCreacion());
				// alu.setFechaNacimiento(fechaNacimiento);
				alu.setCedula(entity.getCedula());
				alu.setEmail(entity.getEmail());
				alu.setInterno(true);
				alu.setPublico(false);
				alu.setPassword(entity.getPassword());
				alu.setTipo(new Double(0.5));
				alu.setUsername(nick);
				alu.setGenero(entity.getGenero());
				alu.setUsuario(entity.getId());
				alumnoDao.insert(alu);
				entity.setIdAlumno(alu.getId());

			} else {
				Profesor pro = new Profesor();
				pro.setApellido(entity.getApellido());
				pro.setNombre(entity.getNombre());
				pro.setEdad(entity.getEdad());
				pro.setFechaCreacion(entity.getFechaCreacion());
				pro.setUsuarioCreacion(entity.getUsuarioCreacion());
				pro.setIpCreacion(entity.getIpCreacion());
				// alu.setFechaNacimiento(fechaNacimiento);
			    pro.setGenero(entity.getGenero());
				pro.setUsuarioCreacion(entity.getId());
				pro.setUsuario(entity.getId());
				profesorDao.insert(pro);
				entity.setIdProfesor(pro.getId());
			}

			// por ultimo hacemos el update de usuario
			getDao().modify(entity.getId(), entity);

			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	public Usuario findByName(String username) throws AppException {
		return getDao().findByName(username);
	}

	public Usuario findByNombrePassword(Usuario user) {
		
		return getDao().findByNombrePassword(user);
	}

	public List<String> getPermisos(Integer rol) throws AppException {
		return rolPermisoDao.getPermisos(rol);
	}

	/**
	 * Recibe un alumno y le crea al usuario y se asocia
	 **/
	public Usuario insertarAlumno(Alumno alumno, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("llegue a asociar el alumno");
			Usuario entity = new Usuario();
			entity.setApellido(alumno.getApellidos());
			entity.setNombre(alumno.getNombres());
			String encryptedToken = new Md5Hash(alumno.getPassword(),
					alumno.getCedula()).toString();
			entity.setUsername(alumno.getCedula());
			entity.setPassword(encryptedToken);
			entity.setFechaCreacion(new Date());
			/** se usa 0l para crear usuario */
			entity.setUsuarioCreacion(0L);
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			entity.setInterno(false);
			entity.setIdAlumno(alumno.getId());
			entity.setEmail(alumno.getEmail());
			entity.setPublico(alumno.getPublico());
			/**
			 * Aca yo uso estatico, usuario debe tener un campo donde le indique
			 * que sea alumno o profesor que sea transient
			 ***/
			Integer rol;
			rol = rolPermisoDao.getRolAlumno();
			entity.setRol(rol);

			//validate(entity);
			System.out.println("despues de validate");
			getDao().insert(entity);
			System.out.println("entity guardado: " + entity.getCedula());

			

			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

}
