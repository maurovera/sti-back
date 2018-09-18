package resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Log;
import service.LogService;
import base.BaseResource;


@Path("/log")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogResource  extends BaseResource<Log, LogService>  {
	
	
	@Inject
	private LogService service;

	@Override
	public LogService getService() {
		// TODO Auto-generated method stub
		return service;
	}

}
