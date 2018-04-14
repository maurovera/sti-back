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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import utils.ConceptoView;
import utils.EjercicioView;
import base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


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
   
    //nivel de dificultad
    @Column(name = "nivelDif")
    private Integer nivelDif;
    
	//definido por el profesor
    @Column(name = "descuido")
    private Integer descuido;

    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH} )
	@JoinTable( 
			name = "ejercicio_concepto", 
			joinColumns = { @JoinColumn(name = "id_ejercicio", referencedColumnName = "id_ejercicio") }, 
			inverseJoinColumns = { @JoinColumn(name = "id_concepto", referencedColumnName = "id_concepto") })
    private List<Concepto> listaConceptos = new ArrayList<Concepto>();

    
    @Transient
    private List<String> conceptosAsociados = new ArrayList<String>();
    
    
    @Transient
    private List<ConceptoView> conceptosView = new ArrayList<ConceptoView>();
    
	@Transient
	private EjercicioView ejercicioView = new EjercicioView();

   
	public List<ConceptoView> getConceptosView() {
		
		for ( Concepto c: this.listaConceptos) {
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
		for ( Concepto c: this.listaConceptos) {
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

	public Integer getNivelDif() {
		return nivelDif;
	}

	public void setNivelDif(Integer nivelDif) {
		this.nivelDif = nivelDif;
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
	
	@JsonProperty
	public void setListaConceptos(List<Concepto> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}
	
	public void addConceptos(Concepto c){
		this.listaConceptos.add(c);
	}
    
   
}
