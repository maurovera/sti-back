package service;


import javax.ejb.Stateless;
import javax.inject.Inject;

import utils.AppException;
import model.Sesion;
import model.SesionConcepto;
import base.BaseServiceImpl;
import dao.SesionConceptoDAO;

@Stateless
public class SesionConceptoService extends BaseServiceImpl<SesionConcepto, SesionConceptoDAO> {

	@Inject
	private SesionConceptoDAO dao;
	
	@Inject
	private SesionService sesionService;

	@Override
	public SesionConceptoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	/**busca sesion concepto por Sesion y idConcepto y devuelve el concepto
	 * @throws AppException 
	 ***/
	public SesionConcepto buscarSesionConceptoPor(Long idSesion, Long idConcepto) throws AppException{
		SesionConcepto retorno = new SesionConcepto();
		
		Sesion sesion = sesionService.obtener(idSesion);
		retorno = getDao().sesionConceptoPorSesion(idSesion, idConcepto);
		
		if(retorno != null)
			System.out.println("sesionConcepto numero: " + retorno.getId());
	
		return retorno;	
	}


}