package dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import model.Camino;
import model.Drl;
import model.Resuelto;
import model.Simulacion;
import base.BaseDAO;

@Stateless
public class SimulacionDAO extends BaseDAO<Simulacion> {

	@Override
	public Class getEntity() {
		// TODO Auto-generated method stub
		return Simulacion.class;
	}

	/** comparar */
	public List<Double> comparar(Long idAlumno, Integer test) {

		/** Lista de retorno **/
		List<Resuelto> listaRetorno = new ArrayList<Resuelto>();
		List<Double> listaFinalInicial = new ArrayList<Double>();
		String drl = "test final";
		Query q = null;

		if (test == 1) {
			// primer test
			q = em.createQuery("SELECT  r" + " FROM Resuelto r where "
					+ "r.idAlumno  =:alumno " + "and r.esPrimerTest = true "
					+ "order by r.fechaCreacion");

			q.setParameter("alumno", idAlumno);

		} else if (test == 2) {

			// segundo test
			q = em.createQuery("SELECT  r" + " FROM Resuelto r where "
					+ "r.idAlumno  =:alumno " + "and r.esPrimerTest = false "
					+ "order by r.fechaCreacion");

			q.setParameter("alumno", idAlumno);

		} else if (test == 3) {

			// tercer test
			q = em.createQuery("SELECT  r" + " FROM Resuelto r where "
					+ "r.idAlumno  =:alumno " + "and r.testFinal = true "
					+ "order by r.fechaCreacion");

			q.setParameter("alumno", idAlumno);

		}

		listaRetorno = q.getResultList();
		int tama = listaRetorno.size() - 1;
		if (!listaRetorno.isEmpty()) {
			Resuelto Resuelto01 = listaRetorno.get(0);
			Double valor1 = Resuelto01.getNivelInicial();
			Resuelto Resuelto02 = listaRetorno.get(tama);
			Double valor2 = Resuelto02.getNivelFinal();
			listaFinalInicial.add(valor1);
			listaFinalInicial.add(valor2);
		}

		return listaFinalInicial;

	}

	/** Tamano de camino de los materiales */
	public List<Camino> consultaTamanoCamino(Long inicialCamino, Long finalCamino) {

		/** Lista de retorno **/
		List<Camino> listaRetorno = new ArrayList<Camino>();
		Query q = null;
		

		q = em.createQuery("SELECT  c " + " FROM Camino c WHERE "
				+ "c.usuarioCreacion  >= :inicial "
				+ "and c.usuarioCreacion <= :final "
				+ "order by c.usuarioCreacion");

		q.setParameter("inicial", inicialCamino);
		q.setParameter("final", finalCamino);

		System.out.println("Caminos");
		listaRetorno = q.getResultList();
		for (Camino cam : listaRetorno) {
			System.out.println(cam.getId() + " :idUsuario : "
					+ cam.getUsuarioCreacion());
		}

		return listaRetorno;

	}

	/** devuelve la lista de resueltos del test 2 */
	public List<Resuelto> comparar2TestTutor(Long idAlumno, Integer test) {

		/** Lista de retorno **/
		List<Resuelto> listaRetorno = new ArrayList<Resuelto>();
		List<Double> listaFinalInicial = new ArrayList<Double>();
		String drl = "test final";
		Query q = null;

		if (test == 1) {
			// primer test
			q = em.createQuery("SELECT  r" + " FROM Resuelto r where "
					+ "r.idAlumno  =:alumno " + "and r.esPrimerTest = true "
					+ "order by r.fechaCreacion");

			q.setParameter("alumno", idAlumno);

		} else if (test == 2) {

			// segundo test
			q = em.createQuery("SELECT  r" + " FROM Resuelto r where "
					+ "r.idAlumno  =:alumno " + "and r.esPrimerTest = false "
					+ "order by r.fechaCreacion");

			q.setParameter("alumno", idAlumno);

		} else if (test == 3) {

			// tercer test
			q = em.createQuery("SELECT  r" + " FROM Resuelto r where "
					+ "r.idAlumno  =:alumno " + "and r.testFinal = true "
					+ "order by r.fechaCreacion");

			q.setParameter("alumno", idAlumno);

		}

		listaRetorno = q.getResultList();
		int tama = listaRetorno.size() - 1;
		if (!listaRetorno.isEmpty()) {
			Resuelto Resuelto01 = listaRetorno.get(0);
			Double valor1 = Resuelto01.getNivelInicial();
			Resuelto Resuelto02 = listaRetorno.get(tama);
			Double valor2 = Resuelto02.getNivelFinal();
			listaFinalInicial.add(valor1);
			listaFinalInicial.add(valor2);
		}

		return listaRetorno;

	}
}
