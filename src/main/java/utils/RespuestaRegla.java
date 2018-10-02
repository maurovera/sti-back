package utils;

public class RespuestaRegla {

	
	private String idMaterial;
	private Character prioridad;
	
	
	public RespuestaRegla(String idMaterial, Character prioridad) {
		super();
		this.idMaterial = idMaterial;
		this.prioridad = prioridad;
	}


	public String getIdMaterial() {
		return idMaterial;
	}
	public void setIdMaterial(String idMaterial) {
		this.idMaterial = idMaterial;
	}


	public Character getPrioridad() {
		return prioridad;
	}


	public void setPrioridad(Character prioridad) {
		this.prioridad = prioridad;
	}


	@Override
	public String toString() {
		return "RespuestaRegla [idMaterial=" + idMaterial + ", prioridad="
				+ prioridad + "]";
	}
	
	
	
	
}
