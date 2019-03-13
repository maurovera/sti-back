package shiro;

public class Credenciales {
	
	private String username;
	private String password;
	
	public Credenciales() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[ username=" + username + " password: "+password+" ]";
	}
	
	

}
