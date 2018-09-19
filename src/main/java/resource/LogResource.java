package resource;

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

import model.Log;
import service.LogService;
import base.BaseResource;


@Path("/log")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogResource  extends BaseResource<Log, LogService>  {
	
	
	@Inject
	private LogService service;

	@Override
	public LogService getService() {
		// TODO Auto-generated method stub
		return service;
	}
	
	
	/***
	 * Crea el archivo log
	 **/
	@GET
	@Path("/archivoLog")
	@Produces(MediaType.APPLICATION_JSON)
	public String archivolog() {
		System.out.println("Crear un archivo log resource");

		String archivo = null;
		try {
			archivo = getService().escribirArchivoLog();
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
