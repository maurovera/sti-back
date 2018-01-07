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
@Table(name = "estilo_aprendizaje")
@DynamicInsert
public class EstiloAprendizaje extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estilo")
	private Long id;
	
	@Column(name = "estilo_aprendizaje")
    private String estiloAprendizaje;
    
	//ESTILO VISUAL
    @Column(name = "resul_v")
    private Integer resulV;
    
	//ESTILO AUDITIVO
    @Column(name = "resul_a")
    private Integer resulA;
    
  //ESTILO LECTOR
    @Column(name = "resul_r")
    private Integer resulR;
    
  //ESTILO KINESTESICO
    @Column(name = "resul_k")
    private Integer resulK;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEstiloAprendizaje() {
		return estiloAprendizaje;
	}

	public void setEstiloAprendizaje(String estiloAprendizaje) {
		this.estiloAprendizaje = estiloAprendizaje;
	}

	public Integer getResulV() {
		return resulV;
	}

	public void setResulV(Integer resulV) {
		this.resulV = resulV;
	}

	public Integer getResulA() {
		return resulA;
	}

	public void setResulA(Integer resulA) {
		this.resulA = resulA;
	}

	public Integer getResulR() {
		return resulR;
	}

	public void setResulR(Integer resulR) {
		this.resulR = resulR;
	}

	public Integer getResulK() {
		return resulK;
	}

	public void setResulK(Integer resulK) {
		this.resulK = resulK;
	}

}
