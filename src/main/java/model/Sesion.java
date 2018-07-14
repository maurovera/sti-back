package model;

/*
 * Sesion */
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonBackReference;

import base.BaseEntity;

@Entity
@Table(name = "sesion")
@DynamicInsert
public class Sesion extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sesion")
	private Long id;

	@Basic(optional = false)
	@NotNull
	@Column(name = "entrada")
	@Temporal(TemporalType.TIMESTAMP)
	private Date entrada;
	// @Basic(optional = false)
	@Column(name = "salida")
	@Temporal(TemporalType.TIMESTAMP)
	private Date salida;
	
	@Size(max = 10)
	@Column(name = "estado_animo")
	private String estadoAnimo;
	
	@Column(name = "estado_terminado")
	private Boolean estadoTerminado;
	
	@JoinColumn(name = "tarea", referencedColumnName = "id_tarea")
	@ManyToOne
	private Tarea tarea;
	
	@JoinColumn(name = "alumno", referencedColumnName = "id_alumno")
	@ManyToOne
	private Alumno alumno;
	
	@Column(name = "cantidad_ejercicios_resueltos")
	private Integer cantidadEjerciciosResueltos;
	
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH })
	@JoinTable(name = "sesion_ejercicio", 
			joinColumns = { @JoinColumn(name = "id_sesion", 
			referencedColumnName = "id_sesion") }, 
			inverseJoinColumns = { @JoinColumn(name = "id_ejercicio", 
			referencedColumnName = "id_ejercicio") })
	private List<Ejercicio> listaEjercicio = new ArrayList<Ejercicio>();

	public Sesion() {
	}

	public Sesion(Long idSesion) {
		this.id = idSesion;
	}

	public Sesion(Long idSesion, Date entrada, Date salida) {
		this.id = idSesion;
		this.entrada = entrada;
		this.salida = salida;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long idSesion) {
		this.id = idSesion;
	}

	public Integer getcantidadEjerciciosResueltos() {
		return cantidadEjerciciosResueltos;
	}

	public void setCantidadEjerciciosResueltos(
			Integer cantidadEjerciciosResueltos) {
		this.cantidadEjerciciosResueltos = cantidadEjerciciosResueltos;
	}

	public Date getEntrada() {
		return entrada;
	}

	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}

	public Date getSalida() {
		return salida;
	}

	public void setSalida(Date salida) {
		this.salida = salida;
	}

	public String getEstadoAnimo() {
		return estadoAnimo;
	}

	public void setEstadoAnimo(String estadoAnimo) {
		this.estadoAnimo = estadoAnimo;
	}

	public Boolean getEstadoTerminado() {
		return estadoTerminado;
	}

	public void setEstadoTerminado(Boolean estadoAnimo) {
		this.estadoTerminado = estadoAnimo;
	}

	@JsonBackReference(value="tarea-sesion")
	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	@JsonBackReference(value="alumno-sesion")
	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	
	//@JsonBackReference(value="ejercicios-sesion")
	public List<Ejercicio> getListaEjercicio() {
		return listaEjercicio;
	}

	public void setListaEjercicio(List<Ejercicio> listaEjercicio) {
		this.listaEjercicio = listaEjercicio;
	}

	public Integer getCantidadEjerciciosResueltos() {
		return cantidadEjerciciosResueltos;
	}
	
	public void addEjercicioResuelto(Ejercicio ejercicio){
		this.listaEjercicio.add(ejercicio);
	}
	

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Sesion)) {
			return false;
		}
		Sesion other = (Sesion) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id
						.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.Sesion[ idSesion=" + id + " ]";
	}

}
