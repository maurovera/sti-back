package seguridad;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import utils.AppException;
import utils.Constantes;

/**
 *
 * @author konecta
 */
@Stateless
public class RolPermisoDao {
	
	
    @PersistenceContext(unitName = "stiPU")
    protected EntityManager em;

    public Integer getRolPublico() throws AppException {
        
        try {
            
            Query q = em.createQuery("SELECT r.id FROM Rol r WHERE r.codigo = :codigo");
            
            @SuppressWarnings("unchecked")
            List<Integer> res = q.setParameter("codigo", Constantes.ROL_PUBLICO).getResultList();
            if (res != null && res.size() > 0) {
                return res.get(0);
            }
            return 0;

        } catch (Exception e) {
            throw new AppException(500, e.getMessage());
        }
    }
    
    public List<String> getPermisos(Integer rol) throws AppException {

        try {
            Query q = em.createQuery("SELECT r.permiso.nombre FROM RolPermiso r "
                    + "WHERE r.rolPermisoPK.idRol = :idRol");
            
            @SuppressWarnings("unchecked")
            List<String> permisos = q.setParameter("idRol", rol).getResultList();
            return permisos;

        } catch (Exception e) {
            throw new AppException(500, e.getMessage());
        }
    }
    
    
    
    public Integer getRolAlumno() throws AppException {
        
        try {
            
            Query q = em.createQuery("SELECT r.id FROM Rol r WHERE r.codigo = :codigo");
            
            @SuppressWarnings("unchecked")
            List<Integer> res = q.setParameter("codigo", Constantes.ROL_ALUMNO).getResultList();
            if (res != null && res.size() > 0) {
                return res.get(0);
            }
            return 0;

        } catch (Exception e) {
            throw new AppException(500, e.getMessage());
        }
    }
    
    
    public Integer getRolProfesor() throws AppException {
        
        try {
            
            Query q = em.createQuery("SELECT r.id FROM Rol r WHERE r.codigo = :codigo");
            
            @SuppressWarnings("unchecked")
            List<Integer> res = q.setParameter("codigo", Constantes.ROL_PROFESOR).getResultList();
            if (res != null && res.size() > 0) {
                return res.get(0);
            }
            return 0;

        } catch (Exception e) {
            throw new AppException(500, e.getMessage());
        }
    }

    
}
