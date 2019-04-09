package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "sesion_concepto")
@DynamicInsert
public class SesionConcepto extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sesion_concepto")
	private Long id;

	@Column(name = "concepto")
	private Long idConcepto;

	private Integer intentos;

	private Boolean resuelto;

	private Double margen;

	private Integer Total;

	@JoinColumn(name = "sesion", referencedColumnName = "id_sesion")
	@ManyToOne
	private Sesion sesion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdConcepto() {
		return idConcepto;
	}

	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}

	public Integer getIntentos() {
		return intentos;
	}

	public void setIntentos(Integer intentos) {
		this.intentos = intentos;
	}

	public Boolean getResuelto() {
		return resuelto;
	}

	public void setResuelto(Boolean resuelto) {
		this.resuelto = resuelto;
	}

	public Double getMargen() {
		return margen;
	}

	public void setMargen(Double margen) {
		this.margen = margen;
	}

	public Integer getTotal() {
		return Total;
	}

	public void setTotal(Integer total) {
		Total = total;
	}

	@JsonBackReference()
	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}

	/** retorna 0 si no existe osino el valor del concepto */
	public Long existeConcepto(List<Concepto> lista) {
		Long valorActual = this.getIdConcepto();
		Long retorno = new Long(0);
		for (Concepto concepto : lista) {
			if (concepto.getId() == valorActual)
				retorno = valorActual;
		}
		return retorno;
	}

	

}
