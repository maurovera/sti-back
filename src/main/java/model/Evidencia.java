package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
@Table(name = "evidencia")
@DynamicInsert
public class Evidencia extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_evidencia")
	private Long id;

	@Column(name = "id_asignatura")
	private Long idAsignatura;

	@Column(name = "id_curso")
	private Long idCurso;

	/**
	 * Primera instancia el nombre del concepto, nombreDelNivel nombreDelEstilo
	 **/
	@Column(name = "concepto")
	private String concepto;

	@Column(name = "nivel")
	private String nivel;

	@Column(name = "estilo")
	private String estilo;

	/*** MidMaterial ***/
	@Column(name = "material")
	private String materialAMostrar;

	/** Ambos separados por un guion intermedio **/
	@Column(columnDefinition = "text", name = "sec_mat")
	private String secuenciaMaterial;

	@Column(columnDefinition = "text", name = "sec_eje")
	private String secuenciaEjercicio;

	@Column(name = "ejer_valido")
	private String ejercicioValido;

	public Evidencia() {

	}

	public Evidencia(Evidencia e) {
		super();
		this.setConcepto(e.getConcepto());
		this.setSecuenciaEjercicio(e.getSecuenciaEjercicio());
		this.setSecuenciaMaterial(e.getSecuenciaMaterial());
		this.setNivel(e.getNivel());
		this.setEstilo(e.getEstilo());
		this.setMaterialAMostrar(e.getMaterialAMostrar());
		this.setEjercicioValido(e.getEjercicioValido());
		this.setIdAsignatura(e.getIdAsignatura());
	}

	public Evidencia(Evidencia e, int numero) {
		super();
		this.setConcepto(e.getConcepto());
		this.setSecuenciaEjercicio(e.getSecuenciaEjercicio());
		this.setSecuenciaMaterial(e.getSecuenciaMaterial());
		this.setNivel(e.getNivel());
		this.setEstilo(e.getEstilo());
		this.setMaterialAMostrar(e.getMaterialAMostrar());
		this.setEjercicioValido(e.getEjercicioValido());
		this.setIdAsignatura(e.getIdAsignatura());
		this.setSecEje(e.getSecEje());
		this.setSecMaterial(e.getSecMaterial());
	}

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
		this.secuenciaMaterial = secuenciaMaterial;
	}

	public String getSecuenciaEjercicio() {
		return secuenciaEjercicio;
	}

	public void setSecuenciaEjercicio(String secuenciaEjercicio) {
		this.secuenciaEjercicio = secuenciaEjercicio;
	}

	public String getEjercicioValido() {
		return ejercicioValido;
	}

	public void setEjercicioValido(String ejercicioValido) {
		this.ejercicioValido = ejercicioValido;
	}

	public void addMaterial(Long idMaterial) {
		this.secMaterial.add(idMaterial);
	}

	public void addEjercicio(Long idEjercicio) {
		this.secEje.add(idEjercicio);
	}

	/** Setea el nivel ***/
	public void setNivelEvidencia(Double nivel) {

		Double bajo = new Double("0.1");
		Double medio = new Double("0.5");
		Double alto = new Double("0.9");
		if (nivel <= bajo) {
			this.setNivel("bajo");
		} else if (nivel > bajo && nivel <= medio) {
			this.setNivel("medio");

		} else {
			this.setNivel("alto");
		}

	}

	private void ordenar() {
		Collections.sort(this.secEje);
		Collections.sort(this.secMaterial);
	}

	public void formatearEvidencia() {

		String listaEjercicio = new String();
		String listaMaterial = new String();
		int longitudEje = this.secEje.size();
		int longitudMaterial = this.secMaterial.size();
		int ultimoEje = longitudEje - 1;
		int ultimoMaterial = longitudMaterial - 1;
		/** Se setea el material valido y el ejercicio valido */
		String materialAMostrar = "M" + this.secMaterial.get(ultimoMaterial);
		this.setMaterialAMostrar(materialAMostrar);
		String ejerValido = "E" + this.secEje.get(ultimoEje);
		this.setEjercicioValido(ejerValido);
		/**
		 * Quitamos el ejercicio que hizo bien de la lista. Tambien quitamos el
		 * material que hizo bien de la lista
		 **/
		this.secEje.remove(ultimoEje);
		this.secMaterial.remove(ultimoMaterial);

		/*** Se ordena la lista para guardarla **/
		ordenar();

		/*** Aca se comprueba si tiene mas de un elemento ***/
		longitudEje = this.secEje.size();
		longitudMaterial = this.secMaterial.size();
		/****
		 * Si es vacio. Se setea a vacio la lista si no se concatena todo el
		 * restante.
		 ***/
		if (this.secEje.isEmpty()) {
			this.setSecuenciaEjercicio("vacio");
		} else {
			ultimoEje = longitudEje - 1;
			for (int i = 0; i < longitudEje; i++) {
				String nombre = "E" + this.secEje.get(i);
				if (i < ultimoEje) {
					listaEjercicio += nombre + "_";
				} else {
					listaEjercicio += nombre;
				}
			}
			this.setSecuenciaEjercicio(listaEjercicio);
		}

		/**
		 * Lo mismo para material Si es vacio. Se setea vacio, si no se setea la
		 * concatecacion
		 ***/
		if (this.secMaterial.isEmpty()) {
			this.setSecuenciaMaterial("vacio");
		} else {
			ultimoMaterial = longitudMaterial - 1;
			for (int i = 0; i < longitudMaterial; i++) {
				String nombre = "M" + this.secMaterial.get(i);
				if (i < ultimoMaterial) {
					listaMaterial += nombre + "_";
				} else {
					listaMaterial += nombre;
				}
			}
			this.setSecuenciaMaterial(listaMaterial);
		}
	}

	public void formatearEvidenciaParaRegla() {

		/**
		 * En principio se setea vacio si no tiene nada. Tampoco se le quita el
		 * ultimo ejercicios ni tampoco se le quita el ultimo material porque es
		 * para machear con el motor de reglas solo necesita
		 * concepto,nivel,estilo,secMaterial,secEje
		 ***/
		//this.setSecuenciaEjercicio("vacio");
		//this.setSecuenciaMaterial("vacio");

		String listaEjercicio = new String();
		String listaMaterial = new String();
		int longitudEje = this.secEje.size();
		int longitudMaterial = this.secMaterial.size();
		int ultimoEje = longitudEje - 1;
		int ultimoMaterial = longitudMaterial - 1;
		
		/*** Se ordena la lista para guardarla **/
		ordenar();

		if (this.secEje.isEmpty()) {
			this.setSecuenciaEjercicio("vacio");
		} else {
			ultimoEje = longitudEje - 1;

			for (int i = 0; i < longitudEje; i++) {

				String nombre = "E" + this.secEje.get(i);

				if (i < ultimoEje) {
					listaEjercicio += nombre + "_";
				} else {
					listaEjercicio += nombre;
				}

			}
			this.setSecuenciaEjercicio(listaEjercicio);
		}

		/**
		 * Lo mismo para material Si es vacio. Se setea vacio, si no se setea la
		 * concatecacion
		 ***/
		if (this.secMaterial.isEmpty()) {
			this.setSecuenciaMaterial("vacio");
		} else {
			ultimoMaterial = longitudMaterial - 1;
			for (int i = 0; i < longitudMaterial; i++) {

				String nombre = "M" + this.secMaterial.get(i);

				if (i < ultimoMaterial) {
					listaMaterial += nombre + "_";
				} else {
					listaMaterial += nombre;
				}

			}
			this.setSecuenciaMaterial(listaMaterial);
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

	@Override
	public String toString() {
		String retorno = getConcepto() + "," + getNivel() + "," + getEstilo()
				+ "," + getMaterialAMostrar() + "," + getSecuenciaMaterial()
				+ "," + getSecuenciaEjercicio() + "," + getEjercicioValido();
		return retorno;

	}

}
