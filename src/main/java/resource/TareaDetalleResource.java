package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.TareaDetalle;
import service.TareaDetalleService;
import base.BaseResource;

@Path("/tareaDetalle")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TareaDetalleResource extends BaseResource<TareaDetalle, TareaDetalleService> {
	
	@Inject
    private TareaDetalleService service;
	
	@Override
	public TareaDetalleService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}

