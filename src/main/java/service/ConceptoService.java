package service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Concepto;
import model.Ejercicio;
import utils.AppException;
import base.BaseServiceImpl;
import dao.ConceptoDAO;

@Stateless
public class ConceptoService extends BaseServiceImpl<Concepto, ConceptoDAO>{

	@Inject
	private ConceptoDAO dao;

	@Override
	public ConceptoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	
	public List<Ejercicio> listaEjercicioConcepto(Concepto concepto)
			throws AppException {
		System.out.println("Lista de ejercicios conceptos service");
		try {
			return dao.listaEjerciciosConcepto(concepto);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}

	}
	
	
}
