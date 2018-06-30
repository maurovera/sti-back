package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.EstiloAprendizaje;
import service.EstiloAprendizajeService;
import base.BaseResource;

@Path("/estiloAprendizaje")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstiloAprendizajeResource extends BaseResource<EstiloAprendizaje, EstiloAprendizajeService> {

	@Inject
	private EstiloAprendizajeService service;

	@Override
	public EstiloAprendizajeService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}