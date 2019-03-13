package seguridad;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author konecta
 */
@Entity
@Table(name = "rol_usuario")
public class RolUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected RolUsuarioPK rolUsuarioPK;

    public RolUsuario() {
    }

    public RolUsuario(RolUsuarioPK rolUsuarioPK) {
        this.rolUsuarioPK = rolUsuarioPK;
    }

    public RolUsuario(long idUsuario, int idRol) {
        this.rolUsuarioPK = new RolUsuarioPK(idUsuario, idRol);
    }

    public RolUsuarioPK getRolUsuarioPK() {
        return rolUsuarioPK;
    }

    public void setRolUsuarioPK(RolUsuarioPK rolUsuarioPK) {
        this.rolUsuarioPK = rolUsuarioPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolUsuarioPK != null ? rolUsuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolUsuario)) {
            return false;
        }
        RolUsuario other = (RolUsuario) object;
        if ((this.rolUsuarioPK == null && other.rolUsuarioPK != null) || (this.rolUsuarioPK != null && !this.rolUsuarioPK.equals(other.rolUsuarioPK))) {
            return false;
        }
        return true;
    }
    
}
