package shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * Allows access if current user has at least one role of the specified list.
 * <br/>
 * Basically, it's the same as {@link RolesAuthorizationFilter} but using
 * {@literal OR} instead of {@literal AND} on the specified roles.
 *
 * @see RolesAuthorizationFilter
 * @author Andy Belsky
 */
public class AnyOfRolesAuthorizationFilter extends RolesAuthorizationFilter {


    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response,
            Object mappedValue) throws IOException {

        final Subject subject = getSubject(request, response);
        final String[] rolesArray = (String[]) mappedValue;
        
        if (!(request instanceof HttpServletRequest)) {
            throw new IOException("Can only process HttpServletRequest");
        }
        
        if (!subject.isAuthenticated()) {
        	System.out.println("no estoy autenticado puto en anyofroles");
            WebUtils.redirectToSavedRequest(request, response, "/api/session/unauthorized");
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        String metodo = httpRequest.getMethod();
        System.out.println("path: "+ path);
        System.out.println("metodo: " + metodo);
        // La peticion no requiere de roles
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
         
        if (metodo.equals("POST") && path.contains("/sti/api/usuarios")) {
            System.out.println("entre post true");
            return true;
        	
        }else {
            System.out.println("entre en false en un metodo que no es post ni insertar usuario");
        }
        
        System.out.println("jamas entre a ver si era post ese puto. y si era de usuarios");
        for (String roleName : rolesArray) {
            String accion = "";
            if (metodo.equals("GET")) {
                accion = "LISTAR_";
            } else if (metodo.equals("POST")) {
                accion = "CREAR_";
            } else if (metodo.equals("PUT")) {
                accion = "EDITAR_";
            } else if (metodo.equals("DELETE")) {
                accion = "ELIMINAR_";
            }
            
            System.out.println("accion + roleName: " + accion + roleName);
            // al pedo convierto el rol a mayuscula
            if (subject.hasRole(accion + roleName.toUpperCase())) {
                return true;
            }
        }

        return false;

    }
    
    
    
}
