package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import utils.EjercicioView;
import base.BaseEntity;

@Entity
@Table(name = "tarea_detalle")
@DynamicInsert
public class TareaDetalle extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tarea_det")
	private Long id;

	@JoinColumn(name = "tarea", referencedColumnName = "id_tarea")
	@ManyToOne
	private Tarea tarea;

	@OneToOne
	private Ejercicio ejercicio;

	@Column(name = "resultado")
	private boolean resultado;

	@Column(name = "nombre")
	private String nombre;


	@Transient
	private EjercicioView ejercicioView = new EjercicioView();
	
	

	public EjercicioView getEjercicioView() {
		
		if(this.ejercicio != null){
			Long idEje = this.ejercicio.getId();
			String enunciadoEje = this.ejercicio.getEnunciado();
			this.ejercicioView.setId(idEje);
			this.ejercicioView.setEnunciado(enunciadoEje);
		}
		return ejercicioView;
	}

	public void setEjercicioView(EjercicioView ejercicioView) {
		this.ejercicioView = ejercicioView;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}

	public boolean isResultado() {
		return resultado;
	}

	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
