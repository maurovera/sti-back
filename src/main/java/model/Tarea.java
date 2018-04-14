package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import base.BaseEntity;

@Entity
@Table(name = "tarea")
@DynamicInsert
public class Tarea extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tarea")
	private Long id;

	@JoinColumn(name = "curso", referencedColumnName = "id_curso")
	@ManyToOne
	private Curso curso;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	/** Lista de temas separados por coma */
	@Column(name = "temas_selec")
	private String temasSeleccionados;

	/** Lista de conceptos separados por coma */
	@Column(name = "conceptos_selec")
	private String conceptosSeleccionados;

	@Column(name = "fecha_inicio")
	private Date fechaInicio;

	@Column(name = "fecha_fin")
	private Date fechaFin;

	@Column(name = "estado_tarea")
	private boolean estadoTarea;

	@Column(name = "tiempo")
	private Date tiempo;

	@OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL)
	private List<TareaDetalle> listaTareas = new ArrayList<TareaDetalle>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
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

	public String getTemasSeleccionados() {
		return temasSeleccionados;
	}

	public void setTemasSeleccionados(String temasSeleccionados) {
		this.temasSeleccionados = temasSeleccionados;
	}

	public String getConceptosSeleccionados() {
		return conceptosSeleccionados;
	}

	public void setConceptosSeleccionados(String conceptosSeleccionados) {
		this.conceptosSeleccionados = conceptosSeleccionados;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isEstadoTarea() {
		return estadoTarea;
	}

	public void setEstadoTarea(boolean estadoTarea) {
		this.estadoTarea = estadoTarea;
	}

	public Date getTiempo() {
		return tiempo;
	}

	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}

	@JsonIgnore
	public List<TareaDetalle> getListaTareas() {
		return listaTareas;
	}

	public void setListaTareas(List<TareaDetalle> listaTareas) {
		this.listaTareas = listaTareas;
	}

}