package util;


import java.util.List;

import model.Kategoria;

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
	
	public void shutdown() {
		HibernateUtil.shutdown();
	}

}
