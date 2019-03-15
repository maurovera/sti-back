package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import model.Ejercicio;
import model.Sesion;
import service.EjercicioService;
import service.SesionService;
import base.BaseResource;

@Path("/sesion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SesionResource extends BaseResource<Sesion, SesionService> {

	@Inject
	private SesionService service;

	@Inject
	private EjercicioService serviceEje;

	@Override
	public SesionService getService() {

		return service;
	}

	/**
	 * Se encarga de insertar un nuevo registro. recibe un id_alumno, id_tarea
	 */
	@POST
	@Path("/registrar/{idTarea}/{idAlumno}")
	@Produces(MediaType.APPLICATION_JSON)
	public Sesion registrarSesion(@PathParam("idTarea") Long idTarea,
			@PathParam("idAlumno") Long idAlumno) {
		try {
			System.out.println("base resource de registrar sesion");
			return getService().registrarSesion(idAlumno, idTarea, httpRequest);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}

	/**
	 * Se encarga de insertar un nuevo registro. recibe un id_alumno, id_tarea
	 */
	@GET
	@Path("/sesionAnt")
	@Produces(MediaType.APPLICATION_JSON)
	public Sesion SesionAnterior() {
		try {
			System.out.println("sesion anterior");
			Sesion s1 = getService().registrarSesion(new Long(3), new Long(1),
					httpRequest);
			System.out.println("S1: " + s1.getId());
			Sesion s = getService().sesionAnterior(new Long(3), new Long(1));
			System.out.println("id de la sesion: " + s.getId());

			Ejercicio eje = serviceEje.obtener(new Long(1));
			System.out.println("ejercicio 1: " + eje.getId());

			if (!s.getListaEjercicio().isEmpty()) {

				for (Ejercicio e : s.getListaEjercicio()) {
					System.out.println(e.getId());
				}

				if (!s.getListaEjercicio().contains(eje)) {
					System.out
							.println("no funciona. y entra aca y carga en la lista");
				}
			}

			return s;
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}

	/**
	 * Se encarga comprobar si existe una sesion anterior. En caso que exista
	 * esa sesion anterior. retorna la sesion anterior. o crea una nueva sesion
	 */
	@GET
	@Path("/comprobarSesion")
	@Produces(MediaType.APPLICATION_JSON)
	public Sesion comprobarSesion(@QueryParam("idAlumno") Long idAlumno,
			@QueryParam("idTarea") Long idTarea) {
		try {

			System.out.println("comprobarSesion");
			// datos de la sesion
			//Long idAlumno = new Long(16);
			//Long idTarea = new Long(3);

			// se llama a la sesion anterior. En caso que exista
			Sesion sesion = getService().sesionAnterior(idAlumno,
					idTarea);
			// si no existe, se registra
			if (sesion == null) {
				System.out.println("#####REGISTRO NUEVA SESION");
				sesion = getService().registrarSesion(idAlumno, idTarea,
						httpRequest);
			} else {
				System.out.println("#########NO REGISTRO SESION ANTERIOR. NO NUEVA");
			}

			System.out.println("id de la sesion: " + sesion.getId());

			return sesion;
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}
}
