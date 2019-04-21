/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguridad;

import javax.ejb.Stateless;

import org.apache.shiro.SecurityUtils;

import com.google.gson.Gson;

/**
 *
 * @author mbaez
 */
@Stateless
public class SessionService {

    /**
     * Se ecarga de recuperar los datos del usuario logeado.
     *
     * @return
     */
    public Usuario getCurrentUser() {
        Usuario usuario = null;
        Object user = SecurityUtils.getSubject().getSession().getAttribute("usuario");
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        usuario = gson.fromJson(userJson, Usuario.class);
        return usuario;
    }
}
