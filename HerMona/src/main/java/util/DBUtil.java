package util;


import java.sql.Date;
import java.util.List;

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
		return kat;
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
		return tech;
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
	
	public Teka saveTeka(Integer numer, String tytul, Date data) {
		Teka teka = new Teka(numer, tytul, data);
		session.save(teka);
		return teka;
	}
	
	@SuppressWarnings("unchecked")
	public List<Technika> getTekas() {
		String hql = "From Teka T";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	public void shutdown() {
		HibernateUtil.shutdown();
	}

}
