package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Tema;
import service.TemaService;
import base.BaseResource;

@Path("/tema")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemaResource extends BaseResource<Tema, TemaService> {

	@Inject
	private TemaService service;

	@Override
	public TemaService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}
