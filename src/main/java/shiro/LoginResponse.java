package shiro;

import java.util.List;

import utils.Respuesta;



public class LoginResponse extends Respuesta {

    private Long userId;
	private String usuario;
	private List<String> permisos;
	
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
	
}
