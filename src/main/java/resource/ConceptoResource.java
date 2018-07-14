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
import javax.ws.rs.core.MediaType;

import model.Concepto;
import model.Ejercicio;
import service.ConceptoService;
import utils.AppException;
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
	
	
	@GET
	@Path("/listaEjercicio")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ejercicio> listaEjercicioConcepto(
			@QueryParam("idConcepto") @DefaultValue("20") Long idConcepto) throws NoSuchFieldException {
		System.out.println("Listar ejercicio concepto resource");
		Concepto concepto;
		List<Ejercicio> lista = new ArrayList<Ejercicio>();
		try {
			concepto = service.obtener(idConcepto);	
			lista =  getService().listaEjercicioConcepto(concepto);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}


}
