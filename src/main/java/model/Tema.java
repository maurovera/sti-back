package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javassist.expr.NewArray;

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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonProperty;

import base.BaseEntity;

@Entity
@Table(name = "tema")
@DynamicInsert
public class Tema extends BaseEntity implements Serializable {

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

	
	//agregado 9 de abril de 2018
	@Transient
	private Integer datoCalculado;
	
	// agregado 9 de abril de 2018
	//@Transient 
	//private List<String> listaCalculada = new ArrayList<String>();
	
	
	/**Guarda el id de los conceptos
	 **/
	/*public List<String> getListaCalculada() {
		
		String cadena = null;
		for ( Concepto c: this.listaConceptos) {
			cadena = String.valueOf(c.getId());
			this.listaCalculada.add(cadena);
		}
	
		return listaCalculada;
	}

	public void setListaCalculada(List<String> listaCalculada) {
		this.listaCalculada = listaCalculada;
	}*/

	public Integer getDatoCalculado() {
		return this.peso*2;
	}

	public void setDatoCalculado(Integer datoCalculado) {
		this.datoCalculado = datoCalculado;
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

	@JsonIgnore
	public Asignatura getAsignatura() {
		return null;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	@JsonIgnore
	public List<Concepto> getListaConceptos() {
		return listaConceptos;
	}
	@JsonProperty
	public void setListaConceptos(List<Concepto> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}

}
