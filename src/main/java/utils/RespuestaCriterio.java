package utils;

public class RespuestaCriterio {

    private boolean exitoso;
    private Long concepto;
    private Boolean esEjercicio;
    private String mensaje;

    public RespuestaCriterio() {
    }

    public RespuestaCriterio(boolean exitoso, String mensaje) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

	public Long getConcepto() {
		return concepto;
	}

	public void setConcepto(Long concepto) {
		this.concepto = concepto;
	}

	public Boolean getEsEjercicio() {
		return esEjercicio;
	}

	public void setEsEjercicio(Boolean esEjercicio) {
		this.esEjercicio = esEjercicio;
	}
    
    

}
