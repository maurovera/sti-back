package shiro;

import java.util.List;

import javax.persistence.Column;

import utils.Respuesta;



public class LoginResponse extends Respuesta {

    private Long userId;
	private String usuario;
	private List<String> permisos;
	private Integer rol;
    private Long idAlumno;
    private Long idProfesor;
    private Boolean mostrar;
    
    
	public LoginResponse() {
	}
	
	public LoginResponse(Long userId, String usuario) {
		super(true, null);
		this.userId = userId;
		this.usuario = usuario;
	}
	
	public LoginResponse(Long userId, String usuario, List<String> permisos) {
		super(true, null);
		this.userId = userId;
		this.usuario = usuario;
		this.permisos = permisos;
	}
	
	public LoginResponse(Long userId, String usuario, List<String> permisos, Integer rol, Long idAlumno, Long idProfesor, Boolean mostrar) {
		super(true, null);
		this.userId = userId;
		this.usuario = usuario;
		this.permisos = permisos;
		this.rol = rol;
		this.idAlumno = idAlumno;
		this.idProfesor = idProfesor;
		this.mostrar = mostrar;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<String> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<String> permisos) {
		this.permisos = permisos;
	}

	public Integer getRol() {
		return rol;
	}

	public void setRol(Integer rol) {
		this.rol = rol;
	}

	public Long getIdAlumno() {
		return idAlumno;
	}

	public void setIdAlumno(Long idAlumno) {
		this.idAlumno = idAlumno;
	}

	public Long getIdProfesor() {
		return idProfesor;
	}

	public void setIdProfesor(Long idProfesor) {
		this.idProfesor = idProfesor;
	}

	public Boolean getMostrar() {
		return mostrar;
	}

	public void setMostrar(Boolean mostrar) {
		this.mostrar = mostrar;
	}
	
	
	
}
