package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;

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


    @JoinTable(name = "tarea_concepto", joinColumns = {
            @JoinColumn(name = "id_tarea", referencedColumnName = "id_tarea")}, inverseJoinColumns = {
            @JoinColumn(name = "id_concepto", referencedColumnName = "id_concepto")})
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH })
    private List<Concepto> listaConceptosTarea;
	
    
    //Conceptos asociados a la tarea
    @Transient
	private List<String> conceptosAsociados = new ArrayList<String>();
	

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
	public List<Concepto> getListaConceptosTarea() {
		return listaConceptosTarea;
	}

	public void setListaConceptosTarea(List<Concepto> listaConceptosTarea) {
		this.listaConceptosTarea = listaConceptosTarea;
	}


	public List<String> getConceptosAsociados() {

		String cadena = null;
		for (Concepto c : this.listaConceptosTarea) {
			cadena = String.valueOf(c.getId());
			this.conceptosAsociados.add(cadena);
		}

		return conceptosAsociados;
	}

	public void setConceptosAsociados(List<String> conceptosAsociados) {
		this.conceptosAsociados = conceptosAsociados;
	}
	
	public void addConceptos(Concepto c) {
		this.listaConceptosTarea.add(c);
	}
	

}