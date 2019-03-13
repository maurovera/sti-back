package seguridad;

public class RegistroOnLine {

	private String nombre;
	private String cedula;
	private String password;
	private String password2;
	private String email;
	private Boolean recibirNotificacion;
	
	public RegistroOnLine() {
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Boolean getRecibirNotificacion() {
		return recibirNotificacion;
	}

	public void setRecibirNotificacion(Boolean recibirNotificacion) {
		this.recibirNotificacion = recibirNotificacion;
	}
	
}
