package resource;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Asignatura;
import model.Ejercicio;

import org.codehaus.jackson.type.TypeReference;

import service.AsignaturaService;
import utils.AsignaturaView;
import base.BaseResource;
import base.ListaResponse;

@Path("/asignatura")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AsignaturaResource extends
		BaseResource<Asignatura, AsignaturaService> {

	@Inject
	private AsignaturaService service;

	@Override
	public AsignaturaService getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@GET
	@Path("/ejercicioMauro")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ejercicio> listarMauro() {
		// category - object with needed id

		return service.listarMauro();

	}

	@GET
	@Path("/listaEjercicio")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<Ejercicio> listarEjercicio(
			// )throws NoSuchFieldException {
			@QueryParam("page") @DefaultValue("1") Integer pagina,
			@QueryParam("count") @DefaultValue("20") Integer cantidad,
			@QueryParam("sortBy") @DefaultValue("id") String orderBy,
			@QueryParam("sortOrder") @DefaultValue("DESC") String orderDir,
			@QueryParam("filters") String json) throws NoSuchFieldException {

		// Integer pagina = 1;
		// Integer cantidad = 20;
		// String orderBy = new String("id");
		// String orderDir = new String("DESC");
		// String json = new String("{\"asignatura\":1}");

		// se calcula el inicio de la grilla
		pagina = pagina > 0 ? pagina : 1;
		Integer inicio = (pagina - 1) * cantidad;

		// se parsa el json para consutrir el filtro
		HashMap<String, Object> filtros = null;
		if (json != null && json.trim().length() > 2) {
			try {
				filtros = mapper.readValue(json,
						new TypeReference<HashMap<String, Object>>() {
						});
			} catch (Exception e) {
				throw new WebApplicationException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		// System.out.println("Soy el hasmap");
		// System.out.println(filtros.get("temas").getClass());
		// List lista = (ArrayList) filtros.get("temas");
		// System.out.println(filtros.get("asignatura"));
		return getService().listarEjercicio(inicio, cantidad, orderBy,
				orderDir, filtros);
	}

	
	@GET
	@Path("/listaAsignatura")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<AsignaturaView> listarAsignatura() throws NoSuchFieldException {

		return getService().listarAsignatura();
	}

	
	
	
	
	
	
	
}
