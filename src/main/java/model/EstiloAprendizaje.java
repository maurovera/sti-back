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
	
	@Column(name = "primer_estilo")
    private String primerEstilo;
    
	@Column(name= "segundo_estilo")
	private String segundoEstilo;
	//ESTILO VISUAL
    @Column(name = "resultado_v")
    private Integer resultadoV;
    
	//ESTILO AUDITIVO
    @Column(name = "resultado_a")
    private Integer resultadoA;
    
  //ESTILO LECTOR
    @Column(name = "resultado_r")
    private Integer resultadoR;
    
  //ESTILO KINESTESICO
    @Column(name = "resultado_k")
    private Integer resultadoK;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrimerEstilo() {
		return primerEstilo;
	}

	public void setPrimerEstilo(String primerEstilo) {
		this.primerEstilo = primerEstilo;
	}

	public String getSegundoEstilo() {
		return segundoEstilo;
	}

	public void setSegundoEstilo(String segundoEstilo) {
		this.segundoEstilo = segundoEstilo;
	}

	public Integer getResultadoV() {
		return resultadoV;
	}

	public void setResultadoV(Integer resultadoV) {
		this.resultadoV = resultadoV;
	}

	public Integer getResultadoA() {
		return resultadoA;
	}

	public void setResultadoA(Integer resultadoA) {
		this.resultadoA = resultadoA;
	}

	public Integer getResultadoR() {
		return resultadoR;
	}

	public void setResultadoR(Integer resultadoR) {
		this.resultadoR = resultadoR;
	}

	public Integer getResultadoK() {
		return resultadoK;
	}

	public void setResultadoK(Integer resultadoK) {
		this.resultadoK = resultadoK;
	}

	
	
	
}
