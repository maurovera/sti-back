package shiro;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

import seguridad.Usuario;
import seguridad.UsuarioService;
import utils.Constantes;

/**
 *
 * @author Konecta
 */
public class KCredentialsMatcher extends SimpleCredentialsMatcher {

    @Inject
    UsuarioService service;

    /**
     * Verifica las credenciales del usuario(username y password)
     *
     * @param tok tok.getPrincipal().toString() contiene el nombreusuario,
     * tok.getCredentials() el pass que ingreso
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken tok, AuthenticationInfo info) {

    	System.out.println("estoy en kcredentiasmacher en docrediant");
    	
        String principal = tok.getPrincipal().toString();
        System.out.println("username: "+principal);
        String encryptedToken = new Md5Hash(new String((char[]) tok.getCredentials()), principal).toString();
        System.out.println("pass : "+encryptedToken);
        try {
            
            Usuario user = new Usuario();
            user.setUsername(principal);
            user.setPassword(encryptedToken);
            Context ctx = new InitialContext();
            service = (UsuarioService) ctx.lookup(Constantes.EJB_JNDI_USUARIO_SERVICE);
            Usuario usuario = new Usuario();
            usuario = service.findByNombrePassword(user);
           
            if (usuario != null) {
            	System.out.println("usuario no nulo");
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
		return false;

    }
}
