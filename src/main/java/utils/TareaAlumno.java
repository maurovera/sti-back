package utils;

import java.util.Date;

import model.Tarea;

public class TareaAlumno {

	
	private Long id;
	private Long curso;
	private Long Alumno;
	private String nombre;
	private String descripcion;
	private String temasSeleccionados;
	private String conceptosSeleccionados;
	private Date fechaInicio;
	private Date fechaFin;
	private boolean estadoTarea;
	private Date tiempo;
    private Integer cantidadEjercicioParada;
    private Integer totalIntentos;
    private Double margenConocimiento;
    private Boolean parar;
    private Integer ejerciciosResueltos;
    private boolean testFinal;
    
    
    
    

	public TareaAlumno(Tarea tarea, Long idAlumno, Integer ejercicios) {
		this.setId(tarea.getId());
		this.setAlumno(idAlumno);
		this.setCantidadEjercicioParada(tarea.getCantidadEjercicioParada());
		this.setCurso(tarea.getCurso().getId());
		this.setDescripcion(tarea.getDescripcion());
		this.setEstadoTarea(false);
		this.setFechaInicio(tarea.getFechaInicio());
		this.setMargenConocimiento(tarea.getMargenConocimiento());
		this.setNombre(tarea.getNombre());
		this.setTotalIntentos(tarea.getTotalIntentos());
		this.setEjerciciosResueltos(ejercicios);
		if(ejercicios < tarea.getCantidadEjercicioParada())
			this.setParar(false);
		else
			this.setParar(true);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCurso() {
		return curso;
	}
	public void setCurso(Long curso) {
		this.curso = curso;
	}
	public Long getAlumno() {
		return Alumno;
	}
	public void setAlumno(Long alumno) {
		Alumno = alumno;
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
	public Integer getCantidadEjercicioParada() {
		return cantidadEjercicioParada;
	}
	public void setCantidadEjercicioParada(Integer cantidadEjercicioParada) {
		this.cantidadEjercicioParada = cantidadEjercicioParada;
	}
	public Integer getTotalIntentos() {
		return totalIntentos;
	}
	public void setTotalIntentos(Integer totalIntentos) {
		this.totalIntentos = totalIntentos;
	}
	public Double getMargenConocimiento() {
		return margenConocimiento;
	}
	public void setMargenConocimiento(Double margenConocimiento) {
		this.margenConocimiento = margenConocimiento;
	}
	public Boolean getParar() {
		return parar;
	}
	public void setParar(Boolean parar) {
		this.parar = parar;
	}
	
	public Integer getEjerciciosResueltos() {
		return ejerciciosResueltos;
	}
	
	public void setEjerciciosResueltos(Integer ejerciciosResueltos) {
		this.ejerciciosResueltos = ejerciciosResueltos;
	}

	public boolean isTestFinal() {
		return testFinal;
	}

	public void setTestFinal(boolean testFinal) {
		this.testFinal = testFinal;
	}
	
	
	
   
}
