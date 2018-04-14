package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Tarea;
import service.TareaService;
import base.BaseResource;

@Path("/tarea")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TareaResource extends BaseResource<Tarea, TareaService> {

	@Inject
	private TareaService service;

	@Override
	public TareaService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}