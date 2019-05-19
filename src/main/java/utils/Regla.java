package utils;

import java.util.ArrayList;
import java.util.List;

public class Regla {

	private String materialAMostrar;
	private String concepto;
	private String nivel;
	private String estilo;
	private String secuenciaVideos;
	private String secuenciaEjercicios;
	private String ejercioValido;
	private Character prioridad;
	private String resultado;
	private Long idAsignatura;
	private List<RespuestaRegla> listaRespuestaRegla = new ArrayList<RespuestaRegla>();

	public Regla(String materialAMostrar, String concepto, String nivel,
			String estilo, String secuenciaVideos, String secuenciaEjercicios,
			String ejercioValido, String prioridad) {
		super();
		this.materialAMostrar = materialAMostrar;
		this.concepto = concepto;
		this.nivel = nivel;
		this.estilo = estilo;
		this.secuenciaVideos = secuenciaVideos;
		this.secuenciaEjercicios = secuenciaEjercicios;
		this.ejercioValido = ejercioValido;
		this.prioridad = prioridad.charAt(0);

	}

	public Regla() {
		super();

	}

	public String getMaterialAMostrar() {
		return materialAMostrar;
	}

	public void setMaterialAMostrar(String materialAMostrar) {
		this.materialAMostrar = materialAMostrar;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getEstilo() {
		return estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}

	public String getSecuenciaVideos() {
		return secuenciaVideos;
	}

	public void setSecuenciaVideos(String secuenciaVideos) {
		this.secuenciaVideos = secuenciaVideos;
	}

	public String getSecuenciaEjercicios() {
		return secuenciaEjercicios;
	}

	public void setSecuenciaEjercicios(String secuenciaEjercicios) {
		this.secuenciaEjercicios = secuenciaEjercicios;
	}

	public String getEjercioValido() {
		return ejercioValido;
	}

	public void setEjercioValido(String ejercioValido) {
		this.ejercioValido = ejercioValido;
	}

	public Character getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Character prioridad) {
		this.prioridad = prioridad;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;

	}

	public List<RespuestaRegla> getListaRespuestaRegla() {
		return listaRespuestaRegla;
	}

	public void setListaRespuestaRegla(List<RespuestaRegla> listaRespuestaRegla) {
		this.listaRespuestaRegla = listaRespuestaRegla;
	}

	public void addRespuestaRegla(String idMaterial, Character prioridad) {

		RespuestaRegla r = new RespuestaRegla(idMaterial, prioridad);
		listaRespuestaRegla.add(r);

	}

	public Long getIdAsignatura() {
		return idAsignatura;
	}

	public void setIdAsignatura(Long idAsignatura) {
		this.idAsignatura = idAsignatura;
	}
	

}
