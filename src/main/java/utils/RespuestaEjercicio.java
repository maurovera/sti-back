package utils;


public class RespuestaEjercicio {
	
	private Long idEjercicio;
	private String respuesta;
	private Long idAlumno;
	private Long idAsignatura;
	private Long idTarea;
	private Long idConcepto;
	private Long idMaterial;
	
	public Long getIdEjercicio() {
		return idEjercicio;
	}
	public void setIdEjercicio(Long idEjercicio) {
		this.idEjercicio = idEjercicio;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public Long getIdAlumno() {
		return idAlumno;
	}
	public void setIdAlumno(Long idAlumno) {
		this.idAlumno = idAlumno;
	}
	public Long getIdAsignatura() {
		return idAsignatura;
	}
	public void setIdAsignatura(Long idAsignatura) {
		this.idAsignatura = idAsignatura;
	}
	public Long getIdTarea() {
		return idTarea;
	}
	public void setIdTarea(Long idTarea) {
		this.idTarea = idTarea;
	}
	
	public Long getIdConcepto() {
		return idConcepto;
	}
	
	public void setIdConcepto(Long idConcepto) {
		this.idConcepto = idConcepto;
	}
	
	public Long getIdMaterial() {
		return idMaterial;
	}
	
	public void setIdMaterial(Long idMaterial) {
		this.idMaterial = idMaterial;
	}
	

}
