package resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Drl;
import model.Material;
import service.DrlService;
import utils.AppException;
import utils.HerramientasDrools;
import utils.HerramientasWeka;
import utils.Regla;
import base.BaseResource;

@Path("/drl")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DrlResource extends BaseResource<Drl, DrlService> {

	@Inject
	private DrlService service;

	@Override
	public DrlService getService() {
		// TODO Auto-generated method stub
		return service;
	}

	/**
	 * Desde el archivo crea las reglas nnge luego pasa a las reglas drl y por
	 * ultimo guarda en la base de datos
	 * **/
	@GET
	@Path("/guardar")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertar01() {
		try {
			String resul = "guarda";
			Drl d = new Drl();
			// d.setArchivoDrl("Hola mundo pude meterllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll todo esto en laklsdflasdknflkasdjnflkjndsflkjnadsflkadsnlkfnasdlkfjnasdlkjfnasdlknfladskjfnlkasdjnflkasdnflkasjndflkajndflkasdjnflkajdnsflkjnadflkjnds base de datos");

			HerramientasWeka hw = new HerramientasWeka(
					"/home/mauro/proyectos/tesis/sti-back/src/main/resources/archivoWeka/weka_1.csv");
			hw.ejecutar();
			System.out.println(hw.getDrl());
			String drl = hw.getDrl();
			d.setArchivoDrl(drl);

			System.out.println("base resource insertar archivo drl");
			getService().insertarDrl(d, httpRequest);
			return resul;
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
	}

	/**
	 * Este método se encarga de obtener un recurso por su id.
	 *
	 * @param id
	 *            Idenfiticador del recurso.
	 * @return el dto del recurso en formato json.
	 */
	@GET
	@Path("drl/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Drl obtener(@PathParam("id") Long id) {
		Drl dto = null;
		try {
			dto = getService().obtener(id);
			String drl = dto.getArchivoDrl();
			HerramientasDrools hd = new HerramientasDrools(drl);
			hd.iniciarBaseConocimiento();

			hd.iniciarSession();
			Regla r = new Regla();
			r.setConcepto("division");
			r.setNivel("bajo");
			r.setEstilo("visual");
			r.setSecuenciaEjercicios("E105");
			r.setSecuenciaVideos("vacio");

			hd.ejecutarRegla(r);

			hd.terminarSession();
			System.out.println("resultado : " + r.getResultado());
			System.out.println("termineee cantidad resultados: "
					+ r.getListaRespuestaRegla().size());

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
	 * Este método se encarga de obtener un recurso por su id. para obtener el
	 * archivo drl generado por la reglas.
	 *
	 * @param id
	 *            Idenfiticador del recurso.
	 * @return el dto del recurso en formato json.
	 * 
	 */
	@GET
	@Path("archivo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String obtenerArchivo(@PathParam("id") Long id) {
		String drl = null;
		try {
			drl = getService().obtenerArchivo(id);

		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (drl == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return drl;
	}

	/**
	 * Metodo que se encarga de generar las reglas para el weka que se obtiene
	 * de evidencias y luego transformarlo en reglas drl. en otras palabras para
	 * el motor de regla.
	 * 
	 * @param id
	 *            asignatura y idCurso
	 * @return un clase drl nueva
	 **/

	@GET
	@Path("/generarReglasDrl/{idAsig}/{idCurso}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response guardarReglasDrl(
			@PathParam("idAsig") @DefaultValue("1") Long idAsig,
			@PathParam("idCurso") @DefaultValue("1") Long idCurso) {
		try {
			System.out.println("Resource insertar archivo drl");
			getService().guardarReglasDrl(idAsig, idCurso, httpRequest);

		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage());
		}
		return Response.status(Status.OK).build();
	}

	/** Obtener el ultimo drl por asignatura */
	@GET
	@Path("/ultimo/{idAsignatura}")
	@Produces(MediaType.APPLICATION_JSON)
	public Drl ultimoDrl(
			@PathParam("idAsignatura") @DefaultValue("5") Long idAsignatura)
			throws NoSuchFieldException, AppException {
		
		System.out.println("ultimo drl disponible por asignatura");

		return getService().ultimoDrl(idAsignatura);
	}

}
