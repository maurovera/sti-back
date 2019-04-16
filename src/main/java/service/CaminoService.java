package service;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Camino;
import model.Concepto;
import seguridad.Usuario;
import utils.AppException;
import base.AdministracionBase;
import base.BaseServiceImpl;
import dao.CaminoDAO;

@Stateless
public class CaminoService extends BaseServiceImpl<Camino, CaminoDAO> {

	@Inject
	private CaminoDAO dao;
	@Inject
	AdministracionBase adm;
	@Inject
	private ConceptoService conceptoService;


	@Override
	public CaminoDAO getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	/**
	 * Queda como sesionMaterialAnterior es lo mismo porque trae toda la sesion
	 * en si
	 * 
	 * @throws AppException
	 * **/
	public Camino caminoAnterior(Long idAlumno, Long idTarea, Long idConcepto,
			Long idAsig, HttpServletRequest httpRequest) throws AppException {

		Camino camino = dao.caminoAnterior(idAlumno, idTarea, idConcepto,
				idAsig);
		if (camino == null) {
			Usuario user = getCurrentUser();
			camino = new Camino();
			System.out.println("camino nulo. Se crea un nuevo camino");
			camino.setActual("E");
			camino.setAnterior("N");
			camino.setEsEjercicio(true);
			camino.setFechaCreacion(new Date());
			camino.setUsuarioCreacion(user.getId());
			camino.setIpCreacion(httpRequest.getRemoteAddr());
			camino.setParar(false);
			/**Aqui es estatico**/
			camino.setEstilo("visual");
			camino.setIdAlumno(idAlumno);
			camino.setIdAsignatura(idAsig);
			camino.setIdConcepto(idConcepto);
			Concepto c = conceptoService.obtener(idConcepto);
			camino.setIdTarea(idTarea);
			camino.setNombreConcepto(c.getNombre());
			Double valorNodo = adm
					.getValorNodoRedDouble(c.getNombre(), idAsig, idAlumno);
			camino.setNivelEvidencia(valorNodo);
			camino.setNivelInicial(valorNodo);
			System.out.println("llegue aqui antes del validate");
			validate(camino);
			dao.insert(camino);
			
		}else{
			System.out.println("existe ya este camino.");
		}

		return camino;

	}
}
