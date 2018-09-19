package service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Material;
import utils.AppException;
import utils.Regla;
import base.BaseServiceImpl;
import dao.MaterialDAO;

@Stateless
public class MaterialService extends BaseServiceImpl<Material, MaterialDAO> {

	@Inject
	private MaterialDAO dao;

	@Override
	public MaterialDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	
	
	/**Lista de materiales que estan disponibles**/
	public Material materialesDisponibles(List<Material> materiales, 
			Regla regla) throws AppException {
		
		//List<Material> lista = new ArrayList<Material>();
		Material material = null;
		try {
			
			material = getDao().materialesDisponibles(materiales, regla);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
		return material;
	}

}
