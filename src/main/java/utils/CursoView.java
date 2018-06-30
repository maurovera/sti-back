package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Alumno;

public class CursoView implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nombre;

	private String descripcion;

	private List<Long> listaAlumno = new ArrayList<Long>();
	
	
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Long> getListaAlumno() {
		return listaAlumno;
	}

	public void setListaAlumno(List<Long> listaAlumno) {
		this.listaAlumno = listaAlumno;
	}
	
	public void addAlumnoId(Long idAlumno){
		this.listaAlumno.add(idAlumno);
	}



}
