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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicInsert;

import utils.ConceptoView;
import utils.EjercicioView;
import base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "ejercicio")
@DynamicInsert
public class Ejercicio extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ejercicio")
	private Long id;

	@Column(name = "respCorrecta")
	private Integer respCorrecta;

	@Column(name = "enunciado")
	private String enunciado;

	// nivel de dificultad
	@Column(name = "nivelDif")
	private Double nivelDificultad;

	// definido por el profesor
	@Column(name = "descuido")
	private Integer descuido;

	@Column(name = "adivinanza")
	private Double adivinanza;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH })
	@JoinTable(name = "ejercicio_concepto", joinColumns = { @JoinColumn(name = "id_ejercicio", referencedColumnName = "id_ejercicio") }, inverseJoinColumns = { @JoinColumn(name = "id_concepto", referencedColumnName = "id_concepto") })
	private List<Concepto> listaConceptos = new ArrayList<Concepto>();

	@JoinTable(name = "ejercicio_respuesta", joinColumns = { @JoinColumn(name = "id_ejercicio", referencedColumnName = "id_ejercicio") }, inverseJoinColumns = { @JoinColumn(name = "id_respuesta", referencedColumnName = "id_respuesta") })
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH })
	private List<Respuesta> listaRespuesta = new ArrayList<Respuesta>();

	@JoinColumn(name = "respuesta", referencedColumnName = "id_respuesta")
	@ManyToOne()
	private Respuesta respuesta;

	// Dato que trae desde el front
	
	private String respuestaCorrecta;

	@Transient
	private Long idAsignatura;
	
	
	
	@Transient
	private String dificultad;
	
	@Transient 
	private String nivelAdivinanza;
	
	
	
	
	
	
	public Long getIdAsignatura() {
		return idAsignatura;
	}

	public void setIdAsignatura(Long idAsignatura) {
		this.idAsignatura = idAsignatura;
	}

	public String getDificultad() {
		return String.valueOf(nivelDificultad);
	}
	
	public String getNivelAdivinanza(){
		return String.valueOf(adivinanza);
	}
	
	
	public String getRespuestaCorrecta() {
		return respuestaCorrecta;
	}

	public void setRespuestaCorrecta(String respuestaCorrecta) {
		this.respuestaCorrecta = respuestaCorrecta;
	}

	@Transient
	private List<String> conceptosAsociados = new ArrayList<String>();

	@Transient
	private List<ConceptoView> conceptosView = new ArrayList<ConceptoView>();

	@Transient
	private EjercicioView ejercicioView = new EjercicioView();

	public List<ConceptoView> getConceptosView() {

		for (Concepto c : this.listaConceptos) {
			ConceptoView dato = new ConceptoView();
			dato.setId(String.valueOf(c.getId()));
			dato.setNombre(c.getNombre());
			this.conceptosView.add(dato);
		}

		return conceptosView;
	}

	public void setConceptosView(List<ConceptoView> conceptosView) {
		this.conceptosView = conceptosView;
	}

	public List<String> getConceptosAsociados() {

		String cadena = null;
		for (Concepto c : this.listaConceptos) {
			cadena = String.valueOf(c.getId());
			this.conceptosAsociados.add(cadena);
		}

		return conceptosAsociados;
	}

	public void setConceptosAsociados(List<String> conceptosAsociados) {
		this.conceptosAsociados = conceptosAsociados;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRespCorrecta() {
		return respCorrecta;
	}

	public void setRespCorrecta(Integer respCorrecta) {
		this.respCorrecta = respCorrecta;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public Integer getDescuido() {
		return descuido;
	}

	public void setDescuido(Integer descuido) {
		this.descuido = descuido;
	}

	@JsonIgnore
	public List<Concepto> getListaConceptos() {
		return this.listaConceptos;
	}

	public void setListaConceptos(List<Concepto> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}

	public void addConceptos(Concepto c) {
		this.listaConceptos.add(c);
	}
	
	
	public void addRespuesta(Respuesta r) {
		this.listaRespuesta.add(r);
	}

	public Double getNivelDificultad() {
		return nivelDificultad;
	}

	public void setNivelDificultad(Double nivelDificultad) {
		this.nivelDificultad = nivelDificultad;
	}

	public Double getAdivinanza() {
		return adivinanza;
	}

	public void setAdivinanza(Double adivinanza) {
		this.adivinanza = adivinanza;
	}

	public Respuesta getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}

	@JsonIgnore
	public List<Respuesta> getListaRespuesta() {
		return listaRespuesta;
	}

	
	public void setListaRespuesta(List<Respuesta> listaRespuesta) {
		this.listaRespuesta = listaRespuesta;
	}
	
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ejercicio)) {
            return false;
        }
        Ejercicio other = (Ejercicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
	
	
	
	@Override
	public String toString() {
		return "model.Ejercicio[ Enunciado=" + enunciado + " id: "+id+" ]";
	}
	
	

}
