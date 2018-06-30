package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Ejercicio;
import service.EjercicioService;
import utils.EjercicioView;
import base.BaseResource;
import base.ListaResponse;

@Path("/ejercicio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EjercicioResource extends
		BaseResource<Ejercicio, EjercicioService> {

	@Inject
	private EjercicioService service;

	@Override
	public EjercicioService getService() {
		// TODO Auto-generated method stub
		return service;
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
	public Ejercicio insertar(Ejercicio dto) {
		try {
			System.out.println("Ejercicio resource insertar");
			System.out.println(dto.getListaConceptos().get(0));
			System.out.println("Ejercicio resource. Lista de respuesta");
			System.out.println(dto.getListaRespuesta().get(0));
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
	public Ejercicio modificar(@PathParam("id") Long id,Ejercicio dto) {
		try {
			System.out.println("Ejercicio resource modificar");
			System.out.println(dto.getListaConceptos().get(0));
			getService().modificar(id, dto, httpRequest);
			return dto;
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**Se encarga de traer una lista de ejercicios
	 * En un futuro proximo. Pasandole el id de curso
	 * trae todo los ejercicios relacionados a la asignatura ligada
	 * al curso.
	 **/
	@GET
	@Path("/listaEjercicio")
	@Produces(MediaType.APPLICATION_JSON)
	public ListaResponse<EjercicioView> listarEjercicio() throws NoSuchFieldException {

		return getService().listarEjercicio();
	}


}
