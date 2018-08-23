package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import base.BaseEntity;

@Entity
@Table(name = "drl")
@DynamicInsert
public class Drl extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_drl")
	private Long id;

	// text
	@Column(columnDefinition = "text", name = "archivo_drl")
	private String archivoDrl;

	private String nuevaColumna;
	// oid
	//@Lob
	//private byte[] campo02;

	// bytea
	//private byte[] campo03;

	// bytea
	private String campo04;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArchivoDrl() {
		return archivoDrl;
	}

	public void setArchivoDrl(String archivoDrl) {
		this.archivoDrl = archivoDrl;
	}

	public String getNuevaColumna() {
		return nuevaColumna;
	}

	public void setNuevaColumna(String nuevaColumna) {
		this.nuevaColumna = nuevaColumna;
	}

	
	
	

}
