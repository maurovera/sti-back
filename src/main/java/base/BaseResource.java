package base;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author mbaez
 * @param <G>
 * @param <S>
 */
public abstract class BaseResource<G extends BaseEntity, S extends BaseService<G>>
		 {

	@Context
	protected HttpServletRequest httpRequest;

	/**
	 * Retorna la referencia al service que es utilizado para obtener los datos.
	 *
	 * @return
	 */
	public abstract S getService();

	/**
	 * Mapper utilizado para parsear el json de filtros.
	 */
	protected ObjectMapper mapper = new ObjectMapper();

	/**
	 * Obtiene la lista páginada de los recursos.
	 *
	 * @param pagina
	 *            número de página que se esta consultando, por defecto es igual
	 *            a 1;
	 * @param cantidad
	 *            el total de registros. Por defecto es igual a 20. Si se le
	 *            pasa -1 retorna todos los registros
	 * @param orderBy
	 *            columna por la cual se realizará el ordenado. Por defecto es
	 *            igual a id.
	 * @param orderDir
	 *            dirección de ordenado. Por defecto es igual a ASC.
	 * @param json
	 *            el json que corresponde a los criterios de filtrado. Por
	 *            defecto es igual a una cadena vacía.
	 * @return La lista paginada de los recursos.
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<G> listar(
			@QueryParam("page") @DefaultValue("1") Integer pagina,
			@QueryParam("count") @DefaultValue("20") Integer cantidad,
			@QueryParam("sortBy") @DefaultValue("id") String orderBy,
			@QueryParam("sortOrder") @DefaultValue("DESC") String orderDir,
			@QueryParam("filters") String json) throws NoSuchFieldException {

		// se calcula el inicio de la grilla
		pagina = pagina > 0 ? pagina : 1;
		Integer inicio = (pagina - 1) * cantidad;

		// se parsea el json para consutrir el filtro
		HashMap<String, Object> filtros = null;
		if (json != null && json.trim().length() > 2) {
			System.out.println("json: puto" + json);
			try {
				filtros = mapper.readValue(json,
						new TypeReference<HashMap<String, Object>>() {
						});
				//filtros = setearFiltros(filtros, httpRequest.getPathInfo());
			} catch (Exception e) {
				throw new WebApplicationException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		return getService()
				.listar(inicio, cantidad, orderBy, orderDir, filtros);
	}

	/**
	 * Este método se encarga de obtener un recurso por su id.
	 *
	 * @param id
	 *            Idenfiticador del recurso.
	 * @return el dto del recurso en formato json.
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public G obtener(@PathParam("id") Long id) {
		G dto = null;
		try {
			dto = getService().obtener(id);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (dto == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return dto;
	}

	/**
	 * Se encarga de insertar un nuevo registro.
	 *
	 * @param dto
	 *            el DTO que se desea insertar en la base de datos.
	 * @return el dto del recurso en formato json. Este DTO ya cuenta con el id
	 *         asignado por la secuencia.
	 */
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public G insertar(G dto) {
		try {
			System.out.println("base resource de tema");
			return getService().insertar(dto, httpRequest);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}

	/**
	 * Se encarga de actualizar un recurso ya existente.
	 *
	 * @param id
	 * @param dto
	 *            el DTO que se desea actualizar en la base de datos.
	 * @return
	 *
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public G modificar(@PathParam("id") Long id,G dto) {
		try {
			getService().modificar(id, dto, httpRequest);
			return dto;
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Se encarga de eliminar un registro de la base de datos.
	 *
	 * @param id
	 *            el primary key del registro a eliminar
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void eliminar(@PathParam("id") Long id) {
		try {
			getService().eliminar(id);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}

	protected Response unauthorized(Object resp) {
		return Response.status(401).entity(resp).build();
	}
}
