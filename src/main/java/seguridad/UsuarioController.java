package seguridad;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import base.BaseResource;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController extends BaseResource<Usuario, UsuarioService> {

	@Inject
	private SessionService session;

	@Inject
	private UsuarioService service;

	@Override
	public UsuarioService getService() {
		return service;
	}

	
	/**Datos de ususario logueado*/
	@GET
	@Path("/usuario-logueado")
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario getUsuarioLogueado() {

		return session.getCurrentUser();
	}

	
	/**Insertar usuarios
	 * @return **/
	@POST
	@Path("/insertar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Usuario insertarUsuario(Usuario dto) {
		try {
			System.out.println("Usuario resource insert. llegue a resource");
			
			
			Usuario u= getService().insertar(dto, httpRequest);
			httpRequest.getHeader("header");
			System.out.println("aqui vuelve con usuario: "+ u.getCedula());
			return u;
		
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}
}
