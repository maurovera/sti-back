package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "camino")
@DynamicInsert
public class Camino extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_camino")
	private Long id;
	
	@Column(name = "id_alumno")
	private Long idAlumno;

	@Column(name = "id_asignatura")
	private Long idAsignatura;

	@Column(name = "id_curso")
	private Long idCurso;

	@Column(name = "id_tarea")
	private Long idTarea;

	@Column(name = "concepto")
	private String nombreConcepto;

	@Column(name = "id_concepto")
	private Long idConcepto;

	@Column(name = "nivel_inicial")
	private Double nivelInicial;
	
	@Column(name = "nivel_final")
	private Double nivelFinal;

	@Column(name = "nivel")
	private String nivel;

	@Column(name = "estilo")
	private String estilo;

	/*** MidMaterial ***/
	@Column(name = "material")
	private String materialAMostrar;

	/** Ambos separados por un guion intermedio **/
	@Column(columnDefinition = "text", name = "sec_mat")
	private String secuenciaMaterial = new String();

	@Column(columnDefinition = "text", name = "sec_eje")
	private String secuenciaEjercicio = new String();

	@Column(name = "ejer_valido")
	private String ejercicioValido;

	/** recibe N: ninguno, M: material, E: ejercicio */
	@Column(name = "anterior")
	private String anterior;

	/** recibe N: ninguno, M: material, E: ejercicio */
	@Column(name = "actual")
	private String actual;

	/** recibe N: ninguno, M: material, E: ejercicio */
	@Column(name = "esEjercicio")
	private Boolean esEjercicio;

	@Column(name = "parar")
	private Boolean parar;

	@Transient
	private List<Long> secMaterial = new ArrayList<Long>();

	@Transient
	private List<Long> secEje = new ArrayList<Long>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdAsignatura() {
		return idAsignatura;
	}

	public void setIdAsignatura(Long idAsignatura) {
		this.idAsignatura = idAsignatura;
	}

	public Long getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}

	public Long getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(Long idTarea) {
		this.idTarea = idTarea;
	}

	public String getNombreConcepto() {
		return nombreConcepto;
	}

	public void setNombreConcepto(String nombreconcepto) {
		nombreConcepto = nombreconcepto;
	}

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public Double getNivelInicial() {
		return nivelInicial;
	}

	public void setNivelInicial(Double nivelInicial) {
		this.nivelInicial = nivelInicial;
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

	public String getMaterialAMostrar() {
		return materialAMostrar;
	}

	public void setMaterialAMostrar(String materialAMostrar) {
		this.materialAMostrar = materialAMostrar;
	}

	public String getSecuenciaMaterial() {
		return secuenciaMaterial;
	}

	public void setSecuenciaMaterial(String secuenciaMaterial) {
		this.secuenciaMaterial += secuenciaMaterial + "-";
	}

	public String getSecuenciaEjercicio() {
		return secuenciaEjercicio;
	}

	public void setSecuenciaEjercicio(String secuenciaEjercicio) {
		this.secuenciaEjercicio += secuenciaEjercicio + "-";
	}

	public String getEjercicioValido() {
		return ejercicioValido;
	}

	public void setEjercicioValido(String ejercicioValido) {
		this.ejercicioValido = ejercicioValido;
	}

	public String getAnterior() {
		return anterior;
	}

	public void setAnterior(String anterior) {
		this.anterior = anterior;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public Boolean getEsEjercicio() {
		return esEjercicio;
	}

	public void setEsEjercicio(Boolean esEjercicio) {
		this.esEjercicio = esEjercicio;
	}

	public Boolean getParar() {
		return parar;
	}

	public void setParar(Boolean parar) {
		this.parar = parar;
	}

	public void addMaterial(Long idMaterial) {
		this.secMaterial.add(idMaterial);
	}

	public void addEjercicio(Long idEjercicio) {
		this.secEje.add(idEjercicio);
	}

	public void cargarMaterialYEjercicio() {
		String[] ejercicios = this.secuenciaEjercicio.split("-");
		for (String string : ejercicios) {
			if ( !string.isEmpty() )
				addEjercicio(Long.valueOf(string));
		}

		String[] materiales = this.secuenciaMaterial.split("-");
		for (String material : materiales) {
			if ( !material.isEmpty() )
				addMaterial(Long.valueOf(material));
		}

	}

	public List<Long> getSecMaterial() {
		return secMaterial;
	}

	public void setSecMaterial(List<Long> secMaterial) {
		this.secMaterial = secMaterial;
	}

	public List<Long> getSecEje() {
		return secEje;
	}

	public void setSecEje(List<Long> secEje) {
		this.secEje = secEje;
	}

	public Long getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(Long idAlumno) {
		this.idAlumno = idAlumno;
	}
	
	/** Setea el nivel ***/
	public void setNivelEvidencia(Double nivel) {

		Double bajo = new Double("0.1");
		Double medio = new Double("0.5");
		Double alto = new Double("0.9");
		if (nivel < medio) {
			this.setNivel("bajo");
		} else if (nivel >= medio && nivel < alto) {
			this.setNivel("medio");

		} else {
			this.setNivel("alto");
		}
		
	this.setNivelFinal(nivel);	

	}

	public Double getNivelFinal() {
		return nivelFinal;
	}

	public void setNivelFinal(Double nivelFinal) {
		this.nivelFinal = nivelFinal;
	}
	
	
	

}
