package model;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonProperty;

import base.BaseEntity;

@Entity
@Table(name = "concepto")
@DynamicInsert
public class Concepto extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_concepto")
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apriori")
	private Double apriori;

	@Column(name = "peso")
	private Double peso;

	@JoinColumn(name = "tema", referencedColumnName = "id_tema")
	@ManyToOne
	private Tema tema;

	
	//este puse hou 7 de abril
	@JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "listaConceptos", cascade = {CascadeType.MERGE, CascadeType.REFRESH} )
	private List<Ejercicio> listaEjercicio;

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

	public Double getApriori() {
		return apriori;
	}

	public void setApriori(Double apriori) {
		this.apriori = apriori;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@JsonIgnore
	public Tema getTema() {
		return null;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	@JsonIgnore
	public List<Ejercicio> getListaEjercicio() {
		return listaEjercicio;
	}

	//este puse hoy 7 de abril
	@JsonProperty
	public void setListaEjercicio(List<Ejercicio> listaEjercicio) {
		this.listaEjercicio = listaEjercicio;
	}

}
