package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import base.BaseEntity;

@Entity
@Table(name = "resueltos")
@DynamicInsert
public class Resuelto extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_resuelto")
	private Long id;
	
	private Long idAlumno;
	
	private Long idTarea;
	
	private Long idAsignatura;
	
	private Long idEjercicio;
	
	private Long idMaterial;
	
	private Boolean esMaterial;
	
	@Column(columnDefinition = "text", name = "respuesta")
	private String respuesta;
	
	private Boolean esCorrecto;
	
	private Boolean testFinal;
	
	private Long idConcepto;
	
	@Column(name = "es_primer_test")
	private Boolean esPrimerTest;
	
	@Column(name = "nivel_inicial")
	private Double nivelInicial;
	
	@Column(name = "nivel_final")
	private Double nivelFinal;
	
	@Column(name = "es_regla")
	private Boolean esRegla;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(Long idAlumno) {
		this.idAlumno = idAlumno;
	}

	public Long getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(Long idTarea) {
		this.idTarea = idTarea;
	}

	public Long getIdAsignatura() {
		return idAsignatura;
	}

	public void setIdAsignatura(Long idAsignatura) {
		this.idAsignatura = idAsignatura;
	}

	public Long getIdEjercicio() {
		return idEjercicio;
	}

	public void setIdEjercicio(Long idEjercicio) {
		this.idEjercicio = idEjercicio;
	}

	public Long getIdMaterial() {
		return idMaterial;
	}

	public void setIdMaterial(Long idMaterial) {
		this.idMaterial = idMaterial;
	}

	public Boolean getEsMaterial() {
		return esMaterial;
	}

	public void setEsMaterial(Boolean esMaterial) {
		this.esMaterial = esMaterial;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Boolean getEsCorrecto() {
		return esCorrecto;
	}

	public void setEsCorrecto(Boolean esCorrecto) {
		this.esCorrecto = esCorrecto;
	}

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public Boolean getEsPrimerTest() {
		return esPrimerTest;
	}

	public void setEsPrimerTest(Boolean esPrimerTest) {
		this.esPrimerTest = esPrimerTest;
	}

	public Double getNivelInicial() {
		return nivelInicial;
	}

	public void setNivelInicial(Double nivelInicial) {
		this.nivelInicial = nivelInicial;
	}

	public Double getNivelFinal() {
		return nivelFinal;
	}

	public void setNivelFinal(Double nivelFinal) {
		this.nivelFinal = nivelFinal;
	}

	public Boolean getTestFinal() {
		return testFinal;
	}

	public void setTestFinal(Boolean testFinal) {
		this.testFinal = testFinal;
	}

	public Boolean getEsRegla() {
		return esRegla;
	}

	public void setEsRegla(Boolean esRegla) {
		this.esRegla = esRegla;
	}
	
	
	
	
}
