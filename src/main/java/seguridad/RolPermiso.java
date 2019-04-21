package seguridad;

import java.io.Serializable;

import javax.enterprise.inject.Model;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author konecta
 */
@Entity
@Model
@Table(name = "rol_permiso")
public class RolPermiso implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected RolPermisoPK rolPermisoPK;
    
    @JoinColumn(name = "id_permiso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Permiso permiso;

    public RolPermiso() {
    }

    public RolPermiso(RolPermisoPK rolUsuarioPK) {
        this.rolPermisoPK = rolUsuarioPK;
    }

    public RolPermiso(int idPermiso, int idRol) {
        this.rolPermisoPK = new RolPermisoPK(idPermiso, idRol);
    }

    public RolPermisoPK getRolPermisoPK() {
        return rolPermisoPK;
    }

    public void setRolPermisoPK(RolPermisoPK rolPermisoPK) {
        this.rolPermisoPK = rolPermisoPK;
    }
    
    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolPermisoPK != null ? rolPermisoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolPermisoPK)) {
            return false;
        }
        RolPermiso other = (RolPermiso) object;
        if ((this.rolPermisoPK == null && other.rolPermisoPK != null) 
        		|| (this.rolPermisoPK != null && !this.rolPermisoPK.equals(other.rolPermisoPK))) {
            return false;
        }
        return true;
    }
    
}
