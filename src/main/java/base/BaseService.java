package base;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import utils.AppException;

/**
 * Capa intermedia de interacción con la base de datos. Establece los métodos
 * CRUD para los recursos.
 *
 * @author mbaez
 * @param <G> Recurso al cual se aplicarán los metodos CRUD
 *
 */
public interface BaseService<G extends BaseEntity> {

    /**
     * Se encarga de obtener un recurso por su id
     *
     * @param id Idenfificardor del recuro
     * @return Recurso que cumple con el criterio de filtrado. Si no existe
     * ningun registro retorna null.
     * @throws py.com.ceamso.utils.AppException
     */
    public G obtener(Long id) throws AppException;

    /**
     * Se encarga de insertar un nuevo recurso
     *
     * @param entity Recurso a agregar.
     * @param httpRequest
     * @return El recurso actual. Si no existe ningun registro retorna null.
     * @throws py.com.ceamso.utils.AppException
     */
    public G insertar(G entity, HttpServletRequest httpRequest) throws AppException;

    /**
     * Se encarga de eliminar un recurso.
     *
     * @param id identificador del recurso a eliminar
     * @throws py.com.ceamso.utils.AppException
     */
    public void eliminar(Long id) throws AppException;

    /**
     * Se encarga de modificar un recurso.
     *
     * @param id identificador del recurso
     * @param entity El recurso a modificar.
     * @param httpRequest
     * @throws py.com.ceamso.utils.AppException
     */
    public void modificar(Long id, G entity, HttpServletRequest httpRequest) throws AppException;

    /**
     * Se encarga de recuperar la lista paginada y ordenado de recursos.
     *
     * @param inicio número de registro a partir del cual se obtendran los
     * datos.
     * @param cantidad cantidad de registros a obtener. Si es -1 se retorna la
     * lista completa.
     * @param orderBy nombre de la columna de ordenado
     * @param orderDir direccion de ordenado.
     * @param filtros el map de los atributos de filtrado.
     * @return La lista que cumple con los criterios de filtrado y ordenado.
     */
    public ListaResponse<G> listar(Integer inicio, Integer cantidad, String orderBy, String orderDir,
            HashMap<String, Object> filtros);

    /**
     * Se encarga de recuperar la lista paginada y ordenado de recursos.
     *
     * @param filtros el map de los atributos de filtrado.
     * @return La lista que cumple con los criterios de filtrado y ordenado.
     */
    public ListaResponse<G> listar(HashMap<String, Object> filtros);

    /**
     * Se encarga de recuperar la lista paginada y ordenado de recursos.
     *
     * @return La lista que cumple con los criterios de filtrado y ordenado.
     */
    public ListaResponse<G> listar();

}
