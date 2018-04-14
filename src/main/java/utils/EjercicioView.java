package utils;

import java.io.Serializable;
import java.util.List;

public class EjercicioView implements Serializable{
	
	private static final long serialVersionUID = 5727920413382856477L;
	private Long id;
	private String nombre;
	private String enunciado;
	private List<ConceptoView> listaConceptos;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEnunciado() {
		return enunciado;
	}
	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}
	public List<ConceptoView> getListaConceptos() {
		return listaConceptos;
	}
	public void setListaConceptos(List<ConceptoView> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}
	
	
	
	
	
	
	

}
