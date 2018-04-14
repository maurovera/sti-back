package service;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import model.Asignatura;
import model.Ejercicio;
import utils.AsignaturaView;
import base.BaseServiceImpl;
import base.ListaResponse;
import dao.AsignaturaDAO;

@Stateless
public class AsignaturaService extends BaseServiceImpl<Asignatura, AsignaturaDAO> {

	@Inject
	private AsignaturaDAO dao;

	@Override
	public AsignaturaDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	
	
	public List<Ejercicio> listarMauro(){
		return dao.listarMauro();
	}
	
	
	

	public ListaResponse<Ejercicio> listarEjercicio(Integer inicio, Integer cantidad,
			String orderBy, String orderDir, HashMap<String, Object> filtros) {
		ListaResponse<Ejercicio> res = getDao().listarEjercicio(inicio, cantidad, orderBy,
				orderDir, filtros);
		return res;
	}

	public ListaResponse<Ejercicio> listarEjercicio(HashMap<String, Object> filtros) {
		return this.listarEjercicio(null, null, "id", "asc", filtros);
	}

	public ListaResponse<Ejercicio> listarEjercicio() {
		return this.listarEjercicio(null);
	}	
	
	
	/**Lista asignatura service.
	 * Para el view del select en curso
	 **/
	public ListaResponse<AsignaturaView> listarAsignatura() {
		ListaResponse<AsignaturaView> res = getDao().listarAsignatura();
		return res;
	}

	
	
}
