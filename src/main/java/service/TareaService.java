package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import utils.AppException;
import model.Concepto;
import model.Ejercicio;
import model.Respuesta;
import model.Tarea;
import base.BaseServiceImpl;
import dao.TareaDAO;

@Stateless
public class TareaService extends BaseServiceImpl<Tarea, TareaDAO> {

	// private SessionService session;
	final private long userId = 1;
	
	@Inject
	private ConceptoService conceptoService;
	
	@Inject
	private TareaDAO dao;

	@Override
	public TareaDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	
	
	/**
	 * @{@inheritDoc
	 */
	@Override
	public void modificar(Long id, Tarea entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("Modificar de Tarea service");
			entity.setFechaModificacion(new Date());			
			entity.setUsuarioModificacion(userId);
			entity.setIpModificacion(httpRequest.getRemoteAddr());
			
			
			/***Conceptos**/
			entity.getListaConceptosTarea().clear();
			for(String c : entity.getConceptosAsociados()) {
				Concepto datos = new Concepto();
				datos = conceptoService.obtener(Long.valueOf(c));
				entity.addConceptos(datos);
			};
			
			getDao().modify(id, entity);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

}
