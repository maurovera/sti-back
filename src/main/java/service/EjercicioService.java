package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityListeners;
import javax.servlet.http.HttpServletRequest;

import model.Concepto;
import model.Ejercicio;
import model.Respuesta;
import utils.AppException;
import utils.EjercicioView;
import base.BaseServiceImpl;
import base.ListaResponse;
import dao.EjercicioDAO;

@Stateless
public class EjercicioService extends BaseServiceImpl<Ejercicio, EjercicioDAO> {

	@Inject
	private EjercicioDAO dao;
	
	@Inject
	private ConceptoService conceptoService;
	
	// private SessionService session;
	final private long userId = 1;

	@Override
	public EjercicioDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	/**
	 * @{@inheritDoc
	 */
	@Override
	public Ejercicio insertar(Ejercicio entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("implements de servicio Ejercicio");
			entity.setId(null);
			entity.setFechaCreacion(new Date());
			entity.setUsuarioCreacion(userId);
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			System.out.println("primero elemento de la lista de conceptos id:");
			System.out.println(entity.getListaConceptos().get(0).getId());
		
			
			// datos de respuesta
			for (Respuesta r : entity.getListaRespuesta()) {
				r.setId(null);
				r.setFechaCreacion(new Date());
				r.setUsuarioCreacion(userId);
				r.setIpCreacion(httpRequest.getRemoteAddr());
			}
			
			
			int respuestaNumero = Integer.valueOf(entity.getRespuestaCorrecta());
			System.out.println("respuesta Correcta: "+ respuestaNumero);
			switch (respuestaNumero) {
			case 1:
				entity.setRespuesta(entity.getListaRespuesta().get(0));
				break;
			case 2:
				entity.setRespuesta(entity.getListaRespuesta().get(1));
				break;
			case 3:
				entity.setRespuesta(entity.getListaRespuesta().get(2));
				break;
			case 4:
				entity.setRespuesta(entity.getListaRespuesta().get(3));
				break;
			}
		
			System.out.println("creada lista respuesta");
			
			
			
			
			getDao().insert(entity);
			
			
			
			
			
			
			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}
	
	
	/**
	 * @{@inheritDoc
	 */
	@Override
	public void modificar(Long id, Ejercicio entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("Modificar de ejercicio service");
			entity.setFechaModificacion(new Date());			
			entity.setUsuarioModificacion(userId);
			entity.setIpModificacion(httpRequest.getRemoteAddr());
			
			
			/***Conceptos**/
			entity.getListaConceptos().clear();
			for(String c : entity.getConceptosAsociados()) {
				Concepto datos = new Concepto();
				datos = conceptoService.obtener(Long.valueOf(c));
				entity.addConceptos(datos);
			};
			
			/**Respuestas**/
			for(Respuesta r : entity.getListaRespuesta()) {
				r.setFechaModificacion(new Date());			
				r.setUsuarioModificacion(userId);
				r.setIpModificacion(httpRequest.getRemoteAddr());
			};
			
			int respuestaNumero = Integer.valueOf(entity.getRespuestaCorrecta());
			System.out.println("respuesta Correcta Modificada: "+ respuestaNumero);
			switch (respuestaNumero) {
			case 1:
				entity.setRespuesta(entity.getListaRespuesta().get(0));
				break;
			case 2:
				entity.setRespuesta(entity.getListaRespuesta().get(1));
				break;
			case 3:
				entity.setRespuesta(entity.getListaRespuesta().get(2));
				break;
			case 4:
				entity.setRespuesta(entity.getListaRespuesta().get(3));
				break;
			}
			
		
			getDao().modify(id, entity);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}


	/**Lista ejercicio service.
	 * Para el view del select en tarea detalle
	 **/
	public ListaResponse<EjercicioView> listarEjercicio() {
		ListaResponse<EjercicioView> res = getDao().listarEjercicio();
		return res;
	}

}
