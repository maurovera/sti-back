package model;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicInsert;

import utils.AsignaturaView;
import base.BaseEntity;

@Entity
@Table(name = "curso")
@DynamicInsert
public class Curso extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_curso")
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "introduccion")
	private String introduccion;
	
	@Column(name = "acerca_de")
	private String acerca_de;
	
	@Column(name = "para_quienes")
	private String para_quienes;
	
	@Column(name = "calif_alumn")
	private String calif_alumn;
	
	@OneToOne
	private Asignatura asignatura;

	@Column(name = "asignatura_arbol")
	private byte[] asignaturaArbol;

	@JoinColumn(name = "profesor", referencedColumnName = "id_profesor")
	@ManyToOne
	private Profesor profesor;
	
	@Transient
	private AsignaturaView asignaturaView = new AsignaturaView();

	@Transient 
	private Long alumno;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(name = "curso_alumno", 
			joinColumns = { @JoinColumn(name = "id_curso", 
			referencedColumnName = "id_curso") },
			inverseJoinColumns = { @JoinColumn(
					name = "id_alumno", referencedColumnName = "id_alumno") })
	private List<Alumno> listaAlumno = new ArrayList<Alumno>();

	
	
	public AsignaturaView getAsignaturaView() {
		
		if(this.asignatura != null){
			Long idAsig = this.asignatura.getId();
			String nombreAsig = this.asignatura.getNombre();
			this.asignaturaView.setId(idAsig);
			this.asignaturaView.setNombre(nombreAsig);
		}
		return asignaturaView;
	}

	public void setAsignaturaView(AsignaturaView asignaturaView) {
		this.asignaturaView = asignaturaView;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	
	
	
	public Long getAlumno() {
		return alumno;
	}

	public void setAlumno(Long alumno) {
		this.alumno = alumno;
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

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	public byte[] getAsignaturaArbol() {
		return asignaturaArbol;
	}

	public void setAsignaturaArbol(byte[] asignaturaArbol) {
		this.asignaturaArbol = asignaturaArbol;
	}

	@JsonIgnore
	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	@JsonIgnore
	public List<Alumno> getListaAlumno() {
		return listaAlumno;
	}

	@JsonProperty
	public void setListaAlumno(List<Alumno> listaAlumno) {
		this.listaAlumno = listaAlumno;
	}
	
	public void agregarAlumno(Alumno al){
		this.listaAlumno.add(al);
	}

	public String getIntroduccion() {
		return introduccion;
	}

	public void setIntroduccion(String introduccion) {
		this.introduccion = introduccion;
	}

	public String getAcerca_de() {
		return acerca_de;
	}

	public void setAcerca_de(String acerca_de) {
		this.acerca_de = acerca_de;
	}

	public String getPara_quienes() {
		return para_quienes;
	}

	public void setPara_quienes(String para_quienes) {
		this.para_quienes = para_quienes;
	}

	public String getCalif_alumn() {
		return calif_alumn;
	}

	public void setCalif_alumn(String calif_alumn) {
		this.calif_alumn = calif_alumn;
	}
	
	
	
	
}