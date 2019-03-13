package seguridad;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;

import base.BaseEntity;

/**
 *
 * @author konecta
 */
@Entity
@Table(name = "usuario")
@DynamicInsert
public class Usuario extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "apellido")
    private String apellido;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;

    @Basic(optional = false)
    @NotNull
    @Column(name = "recibir_notificacion")
    private boolean recibirNotificacion;

    @Size(max = 20)
    @Column(name = "cedula")
    private String cedula;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    
    @Transient
    private String password2;

    @Column(name = "rol")
    private Integer rol;

    @Basic(optional = false)
    @NotNull
    @Column(name = "interno")
    private Boolean interno;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "publico")
    private Boolean publico;

    /**Define si es alumno o no**/
    @Transient
    private Boolean esAlumno;
    
    @Column(name = "id_profesor")
    private Long idProfesor;
    
    @Column(name = "id_alumno")
    private Long idAlumno;
    
    
    public Usuario() {
    }

  /* public Usuario(RegistroOnLine datos, String username, String password) {
        this.cedula = datos.getCedula();
        this.nombre = datos.getNombre();
        this.email = datos.getEmail();
        this.username = username;
        this.password = password;
        this.recibirNotificacion = datos.getRecibirNotificacion();
    }*/

    /*public Usuario(CrearUsuarioDto datos, String username, String password) {
        this.cedula = datos.getCedula();
        this.nombre = datos.getNombre();
        this.email = datos.getEmail();
        this.username = username;
        this.password = password;
        this.recibirNotificacion = datos.getRecibirNotificacion();
    }*/

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(Long id, String nombre, String email, boolean recibirNotificacion, boolean interno) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.recibirNotificacion = recibirNotificacion;
        this.interno = interno;
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getRecibirNotificacion() {
        return recibirNotificacion;
    }

    public void setRecibirNotificacion(boolean recibirNotificacion) {
        this.recibirNotificacion = recibirNotificacion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public Boolean isPublico() {
        return publico;
    }

    public void setPublico(Boolean publico) {
        this.publico = publico;
    }
    
    
    public Boolean getEsAlumno() {
		return esAlumno;
	}

	public void setEsAlumno(Boolean esAlumno) {
		this.esAlumno = esAlumno;
	}
	
	public Long getIdProfesor() {
		return idProfesor;
	}

	public void setIdProfesor(Long idProfesor) {
		this.idProfesor = idProfesor;
	}

	public Long getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(Long idAlumno) {
		this.idAlumno = idAlumno;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario[ id=" + id + " ]";
    }

}
