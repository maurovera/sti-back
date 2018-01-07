package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import base.BaseEntity;


@Entity
@Table(name = "tema")
@DynamicInsert
public class Tema extends BaseEntity implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tema")
	private Long id;
	
	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "peso")
	private Integer peso;

	@JoinColumn(name = "asignatura", referencedColumnName = "id_asignatura")
	@ManyToOne
	private Asignatura asignatura;

	
	@OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)
	private List<Concepto> listaConceptos = new ArrayList<Concepto>();


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


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Integer getPeso() {
		return peso;
	}


	public void setPeso(Integer peso) {
		this.peso = peso;
	}


	public Asignatura getAsignatura() {
		return asignatura;
	}


	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}


	@Transient
	public List<Concepto> getListaConceptos() {
		return listaConceptos;
	}


	public void setListaConceptos(List<Concepto> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}

	
	

}
