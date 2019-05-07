package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import model.Concepto;
import model.Sesion;
import model.Tarea;
import utils.AppException;
import utils.TareaAlumno;
import base.BaseServiceImpl;
import base.ListaResponse;
import dao.TareaDAO;

@Stateless
public class TareaService extends BaseServiceImpl<Tarea, TareaDAO> {

	// private SessionService session;
	final private long userId = 1;

	@Inject
	private ConceptoService conceptoService;

	@Inject
	private SesionService sesionService;

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

			/*** Conceptos **/
			entity.getListaConceptosTarea().clear();
			for (String c : entity.getConceptosAsociados()) {
				Concepto datos = new Concepto();
				datos = conceptoService.obtener(Long.valueOf(c));
				entity.addConceptos(datos);
			}
			;

			getDao().modify(id, entity);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * Lista las tareas por alumno
	 * 
	 * @throws AppException
	 **/
	public ListaResponse<TareaAlumno> listarTareasAlumno(Long idAlumno,
			Long idCurso, HttpServletRequest httpRequest) throws AppException {
		System.out.println("listar tareas del alumno service");

		ListaResponse<TareaAlumno> respuesta = new ListaResponse<TareaAlumno>();
		List<Tarea> tareas = new ArrayList<Tarea>();
		List<TareaAlumno> respuestasTareas = new ArrayList<TareaAlumno>();
		/*** Trae todas las tareas sin distincion */
		tareas = getDao().listarTareaAlumno(idCurso);

		Sesion sesion;
		Long idTarea;

		for (Tarea tarea : tareas) {
			idTarea = tarea.getId();
			sesion = sesionService.sesionAnteriorConNuevo(idAlumno, idTarea,
					httpRequest);
			Integer cantidad = sesion.getcantidadEjerciciosResueltos();
			
			TareaAlumno tareaAlumno = new TareaAlumno(tarea, idAlumno, cantidad);
			tareaAlumno.setTestFinal(sesion.getTestFinal());
			respuestasTareas.add(tareaAlumno);
		}

		respuesta.setRows(respuestasTareas);

		int total = 0;
		if (respuestasTareas != null)
			total = respuestasTareas.size();

		respuesta.setCount(total);

		return respuesta;
	}

}
