package utils;

import java.io.Serializable;

public class AsignaturaView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5727920413382856477L;
	private Long id;
	private String nombre;
	
	
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
	
	
}
