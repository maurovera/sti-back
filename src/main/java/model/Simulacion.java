package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import base.BaseEntity;


@Entity
@Table(name = "simulacion")
@DynamicInsert
public class Simulacion extends BaseEntity implements Serializable {
	
	
	private static final long serialVersionUID = -5415272862093809864L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_simulacion")
	private Long id;

	
	@Column(name="nombre")
	private String Nombre;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return Nombre;
	}


	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	
	
}
