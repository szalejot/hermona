package util;


import java.util.List;

import model.Kategoria;
import model.Technika;

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
	
	public void shutdown() {
		HibernateUtil.shutdown();
	}

}
