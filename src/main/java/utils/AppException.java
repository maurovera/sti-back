package utils;

public class AppException extends Exception {

    int status;
	String mensaje;
    String codigo;
    String estado;
    String mensajeLog;
    
    public AppException(int status, String mensaje) {
    	this.status = status;
        this.mensaje = mensaje;
    }

    public AppException(String codigo, String mensaje) {
        this.mensaje = mensaje;
        this.mensajeLog = mensaje;
        this.codigo = codigo;
        this.estado = null;
    }

    public AppException(String codigo, String mensaje, String mensajeLog) {
        this.mensaje = mensaje;
        this.mensajeLog = mensajeLog;
        this.codigo = codigo;
        this.estado = null;
    }
    
    public AppException(String estado, String codigo, String mensaje, String mensajeLog) {
        this.mensaje = mensaje;
        this.mensajeLog = mensajeLog;
        this.codigo = codigo;
        this.estado = estado;
    }

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
    public String getMessage() {
        return this.mensaje;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getEstado() {
        return this.estado;
    }

    public String getMensajeLog() {
        return mensajeLog;
    }

}
