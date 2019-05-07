package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import base.BaseEntity;

@Entity
@Table(name = "alumno")
@DynamicInsert
public class Alumno extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_alumno")
	private Long id;

	@OneToOne
	private EstiloAprendizaje estilo;

	@Column(name = "nivel_actual")
	private String nivelActual;

	@Column(name = "nombres")
	private String nombres;

	@Column(name = "apellidos")
	private String apellidos;

	@Size(max = 1)
	@Column(name = "genero")
	private String genero;

	@Column(name = "fecha_nacimiento")
	private Date fechaNacimiento;

	@Column(name = "edad")
	private Integer edad;
	
	
	@Column(name = "tipo")
	private Double tipo;
	

	@Column(name = "estilo_actual")
	private String estiloActual;

	
	/*campos agregados para user**/
    
    @Size(min = 1, max = 100)
    @Column(name = "email")
    private String email;

    @Column(name = "recibir_notificacion")
    private Boolean recibirNotificacion;

    @Size(max = 20)
    @Column(name = "cedula")
    private String cedula;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    
    @Column(name = "interno")
    private Boolean interno;
    
    @Column(name = "publico")
    private Boolean publico;

	
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "listaAlumno", cascade = {
			CascadeType.MERGE, CascadeType.REFRESH })
	private List<Curso> listaCurso;
	
	@Column(name = "usuario")
	private Long usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EstiloAprendizaje getEstilo() {
		return estilo;
	}

	public void setEstilo(EstiloAprendizaje estilo) {
		this.estilo = estilo;
	}

	public String getNivelActual() {
		return nivelActual;
	}

	public void setNivelActual(String nivelActual) {
		this.nivelActual = nivelActual;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	@JsonBackReference(value="curso-alumno")
	public List<Curso> getListaCurso() {
		return listaCurso;
	}

	
	public void setListaCurso(List<Curso> listaCurso) {
		this.listaCurso = listaCurso;
	}
	
	
	public Double getTipo() {
		return tipo;
	}

	public void setTipo(Double tipo) {
		this.tipo = tipo;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean isRecibirNotificacion() {
		return recibirNotificacion;
	}

	public void setRecibirNotificacion(Boolean recibirNotificacion) {
		this.recibirNotificacion = recibirNotificacion;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
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

	public Boolean getInterno() {
		return interno;
	}

	public void setInterno(Boolean interno) {
		this.interno = interno;
	}

	public Boolean getPublico() {
		return publico;
	}

	public void setPublico(Boolean publico) {
		this.publico = publico;
	}
	

	public String getEstiloActual() {
		return estiloActual;
	}

	public void setEstiloActual(String estiloActual) {
		this.estiloActual = estiloActual;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Alumno)) {
			return false;
		}
		Alumno other = (Alumno) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}
	
}
