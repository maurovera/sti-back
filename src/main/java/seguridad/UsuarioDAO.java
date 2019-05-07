package seguridad;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Material;
import utils.AppException;
import base.BaseDAO;

/**
 *
 * @author konecta
 */
@Stateless
public class UsuarioDAO extends BaseDAO<Usuario> {

    /**
     *
     * @{@inheritDoc}
     */
    @Override
    public Class getEntity() {
        return Usuario.class;
    }

    public Usuario findByName(String username) throws AppException {

        try {

            String sql = "SELECT u FROM Usuario u WHERE u.username = :username";
            System.out.println("sql de username: "+sql);
            System.out.println(username);
            Query q = em.createQuery(sql);
            q.setParameter("username", username);
            List<Usuario> res = q.getResultList();
            if (res == null || res.size() == 0) {
                return null;
            }
            
            return res.get(0);

        } catch (Exception e) {
            throw new AppException(500, "Error interno del servidor");
        }
    }

    public Usuario findByNombrePassword(Usuario user) {

    	List<Usuario> listaRetorno = new ArrayList<Usuario>();
    		Usuario usuario = new Usuario();
            System.out.println("user: "+ user.toString());
            Query q = em.createQuery("SELECT u FROM Usuario u "
                    + "WHERE u.username = :username AND u.password = :password ");
            System.out.println("consulta:"+ q);
            q.setParameter("username", user.getUsername());
            q.setParameter("password", user.getPassword());
            listaRetorno = q.getResultList();

            if (listaRetorno == null || listaRetorno.isEmpty()) {
    			usuario = null;
    			System.out.println("lista retorno vacia o lista retorno nula");
    			
    		}else{
    			System.out.println("lista retorno no nula.  ");
    			usuario = listaRetorno.get(0);
    			
    		}
            return usuario;

        
    }

}
