package utils;

import java.util.HashMap;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonParseException;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Exception> {
	
	@Context
	private UriInfo uriInfo;

	@Context
	private Request request;

	@Override
	public Response toResponse(Exception e) {
		
		e.printStackTrace();
		
		if (e instanceof AppException) {
			
			AppException appException = (AppException) e;
			
			if (appException.getStatus() == 400 || appException.getStatus() == 404) {
				
				HashMap<String, Object> respuesta = new HashMap<String, Object>();
				respuesta.put("exitoso", false);
				respuesta.put("mensaje", appException.getMessage());
				return Response.status(200).type(MediaType.APPLICATION_JSON)
						.entity(respuesta).build();
				
			}
			
			return Response.status(appException.getStatus())
					.type(MediaType.APPLICATION_JSON)
					.entity(new AppResponse(appException.getStatus(), appException.getMessage()))
					.build();
			
		} else if (e instanceof JsonParseException) {
			HashMap<String, Object> respuesta = new HashMap<String, Object>();
			respuesta.put("exitoso", false);
			respuesta.put("mensaje", "Parametro invalido : " + e.getMessage());
			return Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(respuesta).build();
			
		} else if (e instanceof NotFoundException) {
			return Response.status(404)
					.type(MediaType.APPLICATION_JSON)
					.build();
		}
		
		else if (e instanceof NotAllowedException) {
			return Response.status(404)
					.type(MediaType.APPLICATION_JSON)
					.entity(new AppResponse(404, "Servicio no encontrado"))
					.build();
		}
		
		return Response.status(500)
				.type(MediaType.APPLICATION_JSON)
				.entity(new AppResponse(500, e.getMessage()))
				.build();
		
	}

}
