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


/***
 * 
 * Clase dedicada a recolectar el camino del alumno
 * en cuanto al total del recorrido en la segunda prueba
 * no importa si acerto todo o fallo todo o genero una regla
 ***/

@Entity
@Table(name = "log")
@DynamicInsert
public class Log extends BaseEntity implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_log")
	private Long id;
	
	@Column(name = "asignatura")
	private Long asignatura;

	@Column(name = "curso")
	private Long curso;

	@Column(name = "alumno")
	private Long alumno;
	
	@Column(name = "tarea")
	private Long tarea;

	@Column(name = "concepto")
	private String concepto;

	
	@Column(columnDefinition = "text", name = "secuencia")
	private String secuencia;


	public Log() {
		super();
	}

	public Log(Log log) {
		super();
		this.setAsignatura(log.getAsignatura());
		this.setCurso(log.getCurso());
		this.setAlumno(log.getAlumno());
		this.setTarea(log.getTarea());
		this.setSecuencia(log.getSecuencia());
		this.setConcepto(log.getConcepto());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Long asignatura) {
		this.asignatura = asignatura;
	}

	public Long getCurso() {
		return curso;
	}

	public void setCurso(Long curso) {
		this.curso = curso;
	}

	public Long getAlumno() {
		return alumno;
	}

	public void setAlumno(Long alumno) {
		this.alumno = alumno;
	}

	public Long getTarea() {
		return tarea;
	}

	public void setTarea(Long tarea) {
		this.tarea = tarea;
	}

	public String getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	public void addSecuencia(String valor){
		this.secuencia += valor + ",";
	}

}
