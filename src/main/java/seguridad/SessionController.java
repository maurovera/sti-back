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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

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
			LoginResponse resp = new LoginResponse(usuario.getId(),
					usuario.getNombre(), permisos, usuario.getRol(),
					usuario.getIdAlumno(), usuario.getIdProfesor());
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
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username,
					password);
			// this is all you have to do to support 'remember me' (no config -
			// built in!):
			Usuario usuario = usuarioService.findByName(username);
			if (usuario != null)
				System.out.println("usuario salido de service: "
						+ usuario.getId() + "rol numero: " + usuario.getRol()
						+ " idalumno: " + usuario.getIdAlumno());

			/**Aqui guardamos**/
			Integer rol = usuario.getRol();
			Long idAlu = usuario.getIdAlumno();
			Long idProfe = usuario.getIdProfesor();
			
			Session session = currentUser.getSession();
			session.setAttribute("usuario", usuario);
			token.setRememberMe(true);
			currentUser.login(token);

			/*
			 * UsernamePasswordToken token = new UsernamePasswordToken(username,
			 * password);
			 * 
			 * 
			 * System.out.println("token :"+ token.toString()); Subject
			 * currentUser = SecurityUtils.getSubject();
			 * System.out.println("current :"+ currentUser.hashCode());
			 * currentUser.login(token); Usuario usuario =
			 * usuarioService.findByName(username); if(usuario == null){
			 * System.out.println("usuarionulo"); }
			 * System.out.println("usuario :"+ usuario.getUsername());
			 * SecurityUtils.getSubject().getSession().setAttribute("usuario",
			 * usuario); token.setRememberMe(true);
			 */

			Respuesta res = new Respuesta(false, Mensajes.USUARIO_NO_ENCONTRADO);
			if (usuario != null) {
				System.out.println("usuario es distinto de null");
				List<String> permisos = new ArrayList<String>();
				if (usuario.getRol() != null) {
					permisos = rolPermisoService.getPermisos(usuario.getRol());
				}
				res = new LoginResponse(usuario.getId(), usuario.getNombre(),
						permisos, rol, idAlu,idProfe);
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
