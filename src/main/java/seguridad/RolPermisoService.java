package seguridad;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import utils.AppException;

@Stateless
public class RolPermisoService {

    @Inject
    private RolPermisoDao dao;

    public Integer getRolPublico() throws AppException {
        
        return dao.getRolPublico();
    }
    
    public List<String> getPermisos(Integer rol) throws AppException {

        return dao.getPermisos(rol);
    }

}
