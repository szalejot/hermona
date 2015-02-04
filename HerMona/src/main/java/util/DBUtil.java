package util;


import java.util.List;

import model.Grafika;
import model.Kategoria;
import model.Technika;
import model.Teka;

import org.hibernate.Query;
import org.hibernate.Session;

public class DBUtil {
	
	private static Session session = null;
	
	public DBUtil() {
		if (session == null) {
			session = HibernateUtil.getSessionFactory().openSession();
		}
	}
	
	/**
	 * Pobiera Kategoriê. Jak nie ma w bazie o takiej nazwie to tworzy now¹.
	 * 
	 * @param nazwa
	 * @return
	 */
	public Kategoria getCategory(String nazwa) {
		String hql = "From Kategoria K where K.nazwa = '" + nazwa + "'";
		Query query = session.createQuery(hql);
		if (query.list().isEmpty()) {
			return saveCategory(nazwa);
		} else {
			return (Kategoria)query.list().get(0);
		}
	}
	
	public Kategoria saveCategory(String nazwa) {
		Kategoria kat = new Kategoria(nazwa);
		session.save(kat);
		session.flush();
		return kat;
	}
	
	public void deleteCategory(Kategoria k) {
		session.delete(k);;
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<Kategoria> getCategories() {
		String hql = "From Kategoria K";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	/**
	 * Pobiera Technikê. Jak nie ma w bazie o takiej nazwie to tworzy now¹.
	 * 
	 * @param nazwa
	 * @return
	 */
	public Technika getTechnique(String nazwa) {
		String hql = "From Technika T where T.nazwa = '" + nazwa + "'";
		Query query = session.createQuery(hql);
		if (query.list().isEmpty()) {
			return saveTechnique(nazwa);
		} else {
			return (Technika)query.list().get(0);
		}
	}
	
	public Technika saveTechnique(String nazwa) {
		Technika tech = new Technika(nazwa);
		session.save(tech);
		session.flush();
		return tech;
	}
	
	public void deleteTechnique(Technika t) {
		session.delete(t);
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<Technika> getTechniques() {
		String hql = "From Technika T";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	/**
	 * Pobiera Tekê. Jak nie ma w bazie o takiej nazwie to tworzy now¹.
	 * 
	 * @param nazwa
	 * @return
	 */
	public Teka getTeka(Integer numer) {
		String hql = "From Teka T where T.numer = " + numer;
		Query query = session.createQuery(hql);
		if (query.list().isEmpty()) {
			return saveTeka(numer, null, null);
		} else {
			return (Teka)query.list().get(0);
		}
	}
	
	public boolean existsTekaInDb(Integer numer) {
		String hql = "From Teka T where T.numer = " + numer;
		Query query = session.createQuery(hql);
		if (query.list().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public Teka saveTeka(Integer numer, String tytul, Integer rok) {
		Teka teka = new Teka(numer, tytul, rok);
		session.save(teka);
		session.flush();
		return teka;
	}
	
	public void deleteTeka(Teka t) {
		session.delete(t);
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<Teka> getTekas() {
		String hql = "From Teka T";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	/**
	 * Pobiera Grafikê.
	 * 
	 * @param nazwa
	 * @return
	 */
	public Grafika getGrafika(Integer numerTeki, String numerInwentarza) {
		String hql = "select G"
				+ " from Grafika G "
				+ " join G.teka T"
				+ " where G.numerInwentarza = '" + numerInwentarza + "'"
				+ " and T.numer = " + numerTeki;
		Query query = session.createQuery(hql);
		if (query.list().isEmpty()) {
			return null;
		} else {
			return (Grafika)query.list().get(0);
		}
	}
	
	public Grafika saveGrafika(Grafika g) {
		session.save(g);
		session.flush();
		return g;
	}
	
	public void deleteGrafika(Grafika g) {
		session.delete(g);
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<Grafika> getGrafikas(String predicate) {
		String hql = "select G from Grafika G";
		if (predicate != null && predicate.length() > 0) {
			hql += " join G.teka T"
					+ " join G.kategorie K"
					+ " where " + predicate;
		}
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	public void shutdown() {
		session.flush();
		HibernateUtil.shutdown();
	}

}
