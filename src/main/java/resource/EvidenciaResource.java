package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Evidencia;
import service.EvidenciaService;
import base.BaseResource;


@Path("/evidencia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvidenciaResource extends BaseResource<Evidencia, EvidenciaService>{
	
	@Inject
	private EvidenciaService service;

	@Override
	public EvidenciaService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}
