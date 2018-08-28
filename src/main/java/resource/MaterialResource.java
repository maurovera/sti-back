package resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Material;
import service.MaterialService;
import utils.AppException;
import utils.Regla;
import base.BaseResource;

@Path("/material")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialResource extends BaseResource<Material, MaterialService> {

	@Inject
	private MaterialService service;

	@Override
	public MaterialService getService() {
		// TODO Auto-generated method stub
		return service;
	}
	
	
	
	/***Lista de materiales disponible. Queda pendiente si ver 
	 * que tenga en cuenta el id de la asignatura
	 * Ejemplo
	 * **/
	@GET
	@Path("/materiales")
	@Produces(MediaType.APPLICATION_JSON)
	public Material listarMaterialesDisponibles() throws NoSuchFieldException, AppException {
		System.out.println("Listar materiales disponibles");
		Material m = service.obtener(new Long(1));
		Material m2 = service.obtener(new Long(2));
		List<Material> materiales = new ArrayList<Material>();
		materiales.add(m);
		materiales.add(m2);
		Regla regla = new Regla();
		regla.setConcepto("division");
		regla.setNivel("bajo");
		regla.setEstilo("visual");
		return getService().materialesDisponibles(materiales, regla);
	}
}
