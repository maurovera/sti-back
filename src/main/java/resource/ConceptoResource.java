package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Concepto;
import service.ConceptoService;
import base.BaseResource;

@Path("/concepto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConceptoResource extends BaseResource<Concepto, ConceptoService> {
	
	@Inject
    private ConceptoService service;
	
	@Override
	public ConceptoService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}
