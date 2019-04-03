package resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Material;
import model.SesionConcepto;
import service.SesionConceptoService;
import utils.AppException;
import utils.Regla;
import base.BaseResource;


@Path("/sesionConcepto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SesionConceptoResource extends BaseResource<SesionConcepto, SesionConceptoService> {

	@Inject
	private SesionConceptoService service;

	@Override
	public SesionConceptoService getService() {
		// TODO Auto-generated method stub
		return service;
	}
	
	
	/**
	 * trae sesionConcepto con idSesion y idConcepto
	 * **/
	@GET
	@Path("/obtener/{idSesion}/{idConcepto}")
	@Produces(MediaType.APPLICATION_JSON)
	public SesionConcepto obtenerSesionConcepto(@PathParam("idSesion") Long idSesion, @PathParam("idConcepto") Long idConcepto) throws NoSuchFieldException, AppException {
		System.out.println("obtener Sesion concepto ");
		return service.buscarSesionConceptoPor(idSesion, idConcepto);
	}
	
	
	

}
