package seguridad;


public class UsuarioDto {

	private String nombre;
	private String cedula;
	private String email;
	private Boolean notificaciones;
	
	public UsuarioDto() {
	}
	
	public UsuarioDto(Usuario usuario) {
		this.nombre = usuario.getNombre();
		this.cedula = usuario.getCedula();
		this.email = usuario.getEmail();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(Boolean notificaciones) {
		this.notificaciones = notificaciones;
	}
	
}
