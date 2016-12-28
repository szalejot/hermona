package util;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import model.Grafika;
import model.Kategoria;
import model.Technika;
import model.Teka;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;

public class DBUtil {
	
	public static final int IMG_SIZE = 400;
	
	private static Session session = null;
	
	public DBUtil() {
		if (session == null) {
			session = HibernateUtil.getSessionFactory().openSession();
			session.setCacheMode(CacheMode.IGNORE);
		}
	}
	
	public void resetSession() {
		session.close();
		session = HibernateUtil.getSessionFactory().openSession();
	}
	
	/**
	 * Pobiera Kategorię. Jak nie ma w bazie o takiej nazwie to tworzy nową.
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
	
	public Kategoria saveCategory(Kategoria kat) {
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
		String hql = "From Kategoria K order by nazwa";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	/**
	 * Pobiera Technikę. Jak nie ma w bazie o takiej nazwie to tworzy nową.
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
	
	public Technika saveTechnique(Technika tech) {
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
		String hql = "From Technika T order by nazwa";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	/**
	 * Pobiera Tekę. Jak nie ma w bazie o takiej nazwie to tworzy nową.
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
	
	public Teka saveTeka(Teka teka) {
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
		String hql = "From Teka T order by numer";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	/**
	 * Pobiera Grafikę.
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
		if (g.getIlustracjaPath() == null || g.getIlustracjaPath().trim().equals("")) {
			g.setIlustracjaPath(System.getProperty("user.dir") + "\\" + g.getTeka().getNumer() + "\\" + g.getNumerInwentarza() + ".JPG");
			makeMiniature(g);
		}
		//session.saveOrUpdate(g);
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
		String hql = "select distinct grafika from Grafika grafika"
				+ " join grafika.teka teka";
		if (predicate != null && predicate.length() > 0) {
			hql += " left join grafika.techniki technika"
					+ " left join grafika.kategorie kategoria"
					+ " where " + predicate;
		}
		hql+= " order by teka.numer, grafika.numerInwentarza";
		Query query = session.createQuery(hql);
		List<Grafika> retList = query.list();
		for (Grafika g : retList) {
			session.refresh(g);
		}
		return retList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Grafika> getGrafikasByString(String columnName, String searchText) {
		String hql = "select grafika from Grafika grafika where upper(" + columnName + ") like upper('%" + searchText
				+ "%')";
		Query query = session.createQuery(hql);
		List<Grafika> retList = query.list();
		for (Grafika g : retList) {
			session.refresh(g);
		}
		return retList;
	}
	
	public String createPredicateByText(String text) {
		text = text.toLowerCase();
		return "    lower(grafika.numerInwentarza) like '%" + text + "%'"
				+ " or lower(grafika.temat) like '%" + text + "%'"
				+ " or lower(grafika.seria) like '%" + text + "%'"
				+ " or lower(grafika.wymiary) like '%" + text + "%'"
				+ " or lower(grafika.projektant) like '%" + text + "%'"
				+ " or lower(grafika.rytownik) like '%" + text + "%'"
				+ " or lower(grafika.sygnatury) like '%" + text + "%'"
				+ " or lower(grafika.miejsceWydania) like '%" + text + "%'"
				+ " or lower(grafika.opis) like '%" + text + "%'"
				+ " or lower(grafika.inskrypcje) like '%" + text + "%'"
				+ " or lower(grafika.bibliografia) like '%" + text + "%'"
				+ " or lower(grafika.uwagi) like '%" + text + "%'"
				+ " or lower(kategoria.nazwa) like '%" + text + "%'"
				+ " or lower(technika.nazwa) like '%" + text + "%'"
				+ " or lower(teka.tytul) like '%" + text + "%'";
	}
	
	public void shutdown() {
		session.flush();
		HibernateUtil.shutdown();
	}
	
	public void makeMiniature(Grafika g) {
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(new File(g.getIlustracjaPath()));
		} catch (IOException e) {
			JOptionPane
			.showMessageDialog(
					null,
					"Nie udało sią otworzyć pliku:\n"
							+ g.getIlustracjaPath()
							+ "\nNie wygenerowano dla niego miniatury.",
					"BŁĄD", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizeImagePng = resizeImage(originalImage, type, IMG_SIZE);
		try {
			ImageIO.write(resizeImagePng, "png", new File(".\\Hermona_miniatury\\" + g.getTeka().getNumer() + "_" + g.getNumerInwentarza() + ".png"));
		} catch (Exception e) {
			JOptionPane
			.showMessageDialog(
					null,
					"Nie udało się zapisać pliku:\n"
							+ g.getIlustracjaPath()
							+ "\nNie wygenerowano miniatury.",
					"BŁĄD", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int type, int imgSize) {
		int width = 0;
		int height = 0;
		if (originalImage.getHeight() > originalImage.getWidth()) {
			height = imgSize;
			width = (originalImage.getWidth() * imgSize) / originalImage.getHeight();
		} else {
			width = imgSize;
			height = (originalImage.getHeight() * imgSize) / originalImage.getWidth();
		}
		
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
	 
		return resizedImage;
	    }

}
