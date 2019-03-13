package shiro;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import seguridad.Usuario;
import seguridad.UsuarioService;
import utils.Constantes;

/**
 *
 * @author Konecta
 */
@Dependent
public class KRealm extends AuthorizingRealm {

    @EJB
    UsuarioService service;

    /**
     * Obtiene los datos de autenticacion del usuario(username y password)
     * 
     * @param token
     * @return
     * @throws AuthenticationException 
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
    	
    	System.out.println("estoy en krealm authenticationinfo");
        
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        return new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection prins) {

    	System.out.println("estoy en krealm authrizationinfo");
        SimpleAuthorizationInfo info = null;        
        
        try {

            String username = prins.getPrimaryPrincipal().toString();
            Context ctx = new InitialContext();
            service = (UsuarioService) ctx.lookup(Constantes.EJB_JNDI_USUARIO_SERVICE);            
            Usuario usuario = service.findByName(username);
            if (usuario != null) {
                List<String> listaPermisos = service.getPermisos(usuario.getRol());
                Set<String> permisos = new TreeSet<String>();
                if (listaPermisos != null) {
                    for (String permiso : listaPermisos) {
                        permisos.add(permiso);
                    }
                }
                info = new SimpleAuthorizationInfo(permisos);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return info;
    }
}
