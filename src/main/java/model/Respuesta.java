package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonBackReference;

import base.BaseEntity;

@Entity
@Table(name = "respuesta")
@DynamicInsert
public class Respuesta extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_respuesta")
	private Long id;

	//@Column(name = "descripcion")
	@Column(columnDefinition = "text", name = "descripcion")
	private String descripcion;

	@JsonIgnore
	@ManyToMany(mappedBy = "listaRespuesta", fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private List<Ejercicio> listaEjercicio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@JsonBackReference(value = "respuesta-ejercicio")
	public List<Ejercicio> getListaEjercicio() {
		return listaEjercicio;
	}

	@JsonProperty
	public void setListaEjercicio(List<Ejercicio> listaEjercicio) {
		this.listaEjercicio = listaEjercicio;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Respuesta)) {
			return false;
		}
		Respuesta other = (Respuesta) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id
						.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.Respuesta[ idRespuesta=" + id + " ]";
	}

}
