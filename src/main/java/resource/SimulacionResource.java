package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.Simulacion;
import service.SimulacionService;
import utils.AppException;
import base.BaseResource;

@Path("/simulacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacionResource extends BaseResource<Simulacion, SimulacionService>{

	@Inject
	private SimulacionService service;

	@Override
	public SimulacionService getService() {
		
		return service;
	}
	
	
	
	/**
	 * Prueba lista curso por alumno inscriptos al curso.
	 * 
	 * @param idAlumno
	 * @throws AppException
	 * **/
	@GET
	@Path("/simulacion01")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacion()
			throws NoSuchFieldException, AppException {
		System.out.println("simulacion ");
		return service.simulacion(httpRequest);
	}
	
	
	/**
	 * Simula el tutor.
	 * 
	 * @param idAlumno
	 * @throws AppException
	 * **/
	@GET
	@Path("/simulacion02Tutor/{idAsig}/{idAlu}/{idTarea}/{idArchivo}")
	@Produces(MediaType.APPLICATION_JSON)
	public String simulacionTutor(@PathParam("idAsig") Long idAsig,
			@PathParam("idAlu") Long idAlu,
			@PathParam("idTarea") Long idTarea,
			@PathParam("idArchivo") Long idArchivo
			)
			throws NoSuchFieldException, AppException {
		System.out.println("simulacion tutor ");
		return service.simulacionTutor(httpRequest,idAsig,idAlu,idTarea, idArchivo);
	}
}
