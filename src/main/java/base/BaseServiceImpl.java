/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import utils.AppException;

/**
 *
 * @author mbaez
 * @param <G>
 * @param <D>
 */
public abstract class BaseServiceImpl<G extends BaseEntity, D extends BaseDAO<G>>
		implements BaseService<G> {

	// @Inject
	// private SessionService session;
	final private long userId = 1;
	/**
	 * Se encarga de retornar la instancia del dato a ser utilizado para las
	 * operaciones.
	 *
	 * @return
	 */
	public abstract D getDao();

	/**
	 * Se ecarga de recuperar los datos del usuario logeado.
	 *
	 * @return
	 */
	/**
	 * public Usuario getCurrentUser() { return session.getCurrentUser(); }
	 */

	/**
	 * Se encarga de disaprar los beans validations
	 *
	 * @param obj
	 */
	/**
	 * public void validate(G obj) { try { BeanValidatorUtils.validate(obj); }
	 * catch (IllegalArgumentException | BusinessException e) { throw new
	 * RuntimeException(e.getMessage()); } }
	 */

	/**
	 * @{@inheritDoc
	 */
	@Override
	public G obtener(Long id) throws AppException {
		try {
			return getDao().get(id);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * @{@inheritDoc
	 */
	@Override
	public void modificar(Long id, G entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			// Usuario user = getCurrentUser();
			entity.setFechaModificacion(new Date());			
			entity.setUsuarioModificacion(userId);
			entity.setIpModificacion(httpRequest.getRemoteAddr());
			// validate(entity);

			getDao().modify(id, entity);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * @{@inheritDoc
	 */
	@Override
	public G insertar(G entity, HttpServletRequest httpRequest)
			throws AppException {
		try {
			System.out.println("implements de servicio tema");
			// Usuario user = getCurrentUser();
			entity.setFechaCreacion(new Date());
			entity.setUsuarioCreacion(userId);
			entity.setIpCreacion(httpRequest.getRemoteAddr());
			// validate(entity);
			getDao().insert(entity);
			return entity;
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eliminar(Long id) throws AppException {
		try {
			getDao().delete(id);
		} catch (Exception e) {
			throw new AppException(500, e.getMessage());
		}
	}

	/**
	 *
	 * @{@inheritDoc
	 */
	@Override
	public ListaResponse<G> listar(Integer inicio, Integer cantidad,
			String orderBy, String orderDir, HashMap<String, Object> filtros) {
		ListaResponse<G> res = getDao().listar(inicio, cantidad, orderBy,
				orderDir, filtros);
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListaResponse<G> listar(HashMap<String, Object> filtros) {
		return this.listar(null, null, "id", "asc", filtros);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListaResponse<G> listar() {
		return this.listar(null);
	}
}
