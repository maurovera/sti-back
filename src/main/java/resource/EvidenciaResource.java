package resource;

import java.util.ArrayList;
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

import model.Concepto;
import model.Ejercicio;
import model.Evidencia;
import service.EvidenciaService;
import utils.AppException;
import base.BaseResource;

@Path("/evidencia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvidenciaResource extends
		BaseResource<Evidencia, EvidenciaService> {

	@Inject
	private EvidenciaService service;

	@Override
	public EvidenciaService getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@GET
	@Path("/lista")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Evidencia> listaEvidenciaPorAsignatura(
			@QueryParam("idAsig") @DefaultValue("1") Long idAsig,
			@QueryParam("idCurso") @DefaultValue("1") Long idCurso)
			throws NoSuchFieldException {
		System.out.println("Listar evidencia por asignatura resource");

		List<Evidencia> lista = new ArrayList<Evidencia>();
		try {
			lista = getService().listaEvidencia(idAsig, idCurso);
		} catch (AppException e) {

			e.printStackTrace();
		}
		return lista;
	}

	/***
	 * Crea el archivo weka de las evidencias registradas filtradas por el
	 * idAsignatura y tambien el idCurso
	 **/
	@GET
	@Path("/archivoWeka")
	@Produces(MediaType.APPLICATION_JSON)
	public String archivoWeka(
			@QueryParam("idAsig") @DefaultValue("1") Long idAsig,
			@QueryParam("idCurso") @DefaultValue("1") Long idCurso) {
		System.out.println("Crear un archivo weka resource");

		String archivo = null;
		try {
			archivo = getService()
					.escribirArchivoWekaEvidencia(idAsig, idCurso);
		} catch (Exception e) {
			throw new WebApplicationException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if (archivo == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		
		return archivo;
	}
	

}
