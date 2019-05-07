package seguridad;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Alumno;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import service.AlumnoService;
import shiro.Credenciales;
import shiro.LoginResponse;
import utils.AppException;
import utils.Mensajes;
import utils.Respuesta;
import utils.Utils;

@Path("/session")
public class SessionController {

	@EJB
	UsuarioService usuarioService;

	@EJB
	AlumnoService alumnoService;

	@EJB
	RolPermisoService rolPermisoService;

	@GET
	@Path("/unauthorized")
	public Response unauthorized() {
		return Response.status(403).build();
	}

	/*** Metodo para loguearse **/
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response session(Credenciales credenciales) throws AppException {

		Subject currentUser = SecurityUtils.getSubject();
		System.out.println(credenciales.toString());

		if (!validarParametros(credenciales)) {
			return badRequest(Mensajes.PARAMETROS_INVALIDO);
		}

		if (!currentUser.isAuthenticated()) {
			return autenticar(credenciales);
		} else {
			Usuario usuario = Utils.obtenerUsuarioAutenticado();
			List<String> permisos = new ArrayList<String>();
			if (usuario.getRol() != null) {
				permisos = rolPermisoService.getPermisos(usuario.getRol());
			}

			/**
			 * Obtenemos el logeo del alumno, para profesor siempre retornara
			 * true.
			 */
			Boolean mostrar = true;
			if (usuario.getIdAlumno() != null) {
				Alumno alumno = alumnoService.obtener(usuario.getIdAlumno());
				if (alumno.getEstilo().getId() != null) {
					mostrar = false;
				}
			}

			LoginResponse resp = new LoginResponse(usuario.getId(),
					usuario.getNombre(), permisos, usuario.getRol(),
					usuario.getIdAlumno(), usuario.getIdProfesor(), mostrar);
			return ok(resp);
		}
	}

	/** Metodo para cerrar session **/
	@POST
	@Path("/cerrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cerrarSesion() {

		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			currentUser.getSession().removeAttribute("usuario");
			currentUser.logout();
		}
		Respuesta resp = new Respuesta(true, "");
		return ok(resp);
	}

	private Response autenticar(Credenciales credenciales) throws AppException {

		try {
			
			String username = credenciales.getUsername();
            String password = credenciales.getPassword();

            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            Usuario usuario = usuarioService.findByName(username);
            SecurityUtils.getSubject().getSession().setAttribute("usuario", usuario);
            token.setRememberMe(true);
			
			

			/*String username = credenciales.getUsername();
			String password = credenciales.getPassword();
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username,
					password);
			// this is all you have to do to support 'remember me' (no config -
			// built in!):
			Usuario usuario = usuarioService.findByName(username);
			if(usuario !=null)
            	System.out.println("usuario salido de service: "+ usuario.getId());

			Session session = currentUser.getSession();
			session.setAttribute("usuario", usuario);
			token.setRememberMe(true);
			currentUser.login(token);
			*/
            
			Respuesta res = new Respuesta(false, Mensajes.USUARIO_NO_ENCONTRADO);
			if (usuario != null) {

				Long idAlu = null;
				Integer rol = null;
				Long idProfe = null;
				Boolean mostrar = true;

				System.out.println("usuario salido de service: "
						+ usuario.getId() + "rol numero: " + usuario.getRol()
						+ " idalumno: " + usuario.getIdAlumno());

				/** Aqui guardamos **/
				rol = usuario.getRol();
				idAlu = usuario.getIdAlumno();
				idProfe = usuario.getIdProfesor();

				/** siempre sera true para profesor y alumno **/

				if (usuario.getIdAlumno() != null) {
					Alumno alumno = alumnoService.obtener(idAlu);
					if (alumno.getEstilo() != null) {
						mostrar = false;
					}
				}

				System.out.println("usuario es distinto de null");

				List<String> permisos = new ArrayList<String>();
				if (usuario.getRol() != null) {
					permisos = rolPermisoService.getPermisos(usuario.getRol());
				}
				res = new LoginResponse(usuario.getId(), usuario.getNombre(),
						permisos, rol, idAlu, idProfe, mostrar);
				return ok(res);
			}
			
			return unauthorized(res);

		} catch (IncorrectCredentialsException e) {
			Respuesta res = new Respuesta(false, Mensajes.USUARIO_NO_VALIDO);
			return unauthorized(res);
		}
	}

	private boolean validarParametros(Credenciales credenciales) {

		if (credenciales == null || credenciales.getUsername() == null
				|| credenciales.getPassword() == null
				|| credenciales.getUsername().isEmpty()
				|| credenciales.getPassword().isEmpty()) {
			return false;
		}
		return true;
	}

	protected Response ok(Object resp) {
		return Response.ok().entity(resp).build();
	}

	protected Response unauthorized(Object resp) {
		return Response.status(401).entity(resp).build();
	}

	protected Response badRequest(String mensaje) {
		Respuesta resp = new Respuesta(false, mensaje);
		return Response.status(400).entity(resp).build();
	}
}
