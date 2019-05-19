package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import base.BaseEntity;

@Entity
@Table(name = "material")
@DynamicInsert
public class Material extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_material")
	private Long id;

	/**
	 * Contiene el material las imagenes pueden ser guardados en el servidor y
	 * tipo ser /src/home/main/java/imagenes/numero.jpg
	 */
	@Column(columnDefinition = "text", name = "url_material")
	private String urlMaterial;

	/** Tipo para identificar la asignatura **/
	@Column(name = "id_asig")
	private Long idAsignatura;

	/** Nombre del archivo del arbol bayesiano ***/
	@Column(name = "arbol_bayesiano")
	private String arbolBayesiano;

	/** Lista de conceptos separados por coma **/
	@Column(columnDefinition = "text", name = "concepto")
	private String concepto;

	/** Nivel fijado por algun rango en especifico ***/
	@Column(name = "nivel")
	private String nivel;

	@Column(name = "estilo")
	private String estilo;

	@Column(name = "estrategia")
	private String estrategia;
	
	@Column(name = "fuente")
	private Boolean fuente;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "nombre_enlace")
	private String nombreEnlace;
	
	
	@Column(columnDefinition = "text", name = "origen")
	private String origen;
	
	
	@Transient
	private Boolean esRegla;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrlMaterial() {
		return urlMaterial;
	}

	public void setUrlMaterial(String urlMaterial) {
		this.urlMaterial = urlMaterial;
	}

	public Long getIdAsignatura() {
		return idAsignatura;
	}

	public void setIdAsignatura(Long idAsignatura) {
		this.idAsignatura = idAsignatura;
	}

	public String getArbolBayesiano() {
		return arbolBayesiano;
	}

	public void setArbolBayesiano(String arbolBayesiano) {
		this.arbolBayesiano = arbolBayesiano;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getEstilo() {
		return estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public Boolean getEsRegla() {
		return esRegla;
	}

	public void setEsRegla(Boolean esRegla) {
		this.esRegla = esRegla;
	}

	public Boolean getFuente() {
		return fuente;
	}

	public void setFuente(Boolean fuente) {
		this.fuente = fuente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getNombreEnlace() {
		return nombreEnlace;
	}

	public void setNombreEnlace(String nombreEnlace) {
		this.nombreEnlace = nombreEnlace;
	}
	
	
	

}
