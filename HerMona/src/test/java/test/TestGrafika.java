package test;

import java.util.List;

import model.Grafika;
import model.Kategoria;
import model.Technika;
import model.Teka;
import util.DBUtil;
import junit.framework.TestCase;

public class TestGrafika extends TestCase {
	
	public void testGrafika() {
		System.out.println("--- Test Grafika ---");
		DBUtil dbUtil = new DBUtil();
		Teka teka = dbUtil.getTeka(101);
		Technika tech = dbUtil.getTechnique("technika1-g");
		Kategoria kat1 = dbUtil.getCategory("kat1-g");
		Kategoria kat2 = dbUtil.getCategory("kat2-g");
		Kategoria kat3 = dbUtil.getCategory("kat3-g");
		
		Grafika g1 = new Grafika();
		Grafika g2 = new Grafika();
		
		g1.setTeka(teka);
		g2.setTeka(teka);
		g1.setTechnika(tech);
		g2.setTechnika(tech);
		g1.setNumerInwentarza("1");
		g2.setNumerInwentarza("2");
		g1.getKategorie().add(kat1);
		g2.getKategorie().add(kat2);
		g2.getKategorie().add(kat3);
		
		dbUtil.saveGrafika(g1);
		dbUtil.saveGrafika(g2);
		System.out.println("! -->" + dbUtil.getGrafika(101, "1"));
		System.out.println("! -->" + dbUtil.getGrafikas(null));
		System.out.println("! -->" + dbUtil.getGrafikas("K.nazwa = 'kat2-g'"));
		List<Grafika> gList = dbUtil.getGrafikas("K.nazwa = 'kat2-g'");
		dbUtil.deleteGrafika(gList.get(0));
		System.out.println("! -->" + dbUtil.getGrafikas(null));
		System.out.println("----------------------");
		dbUtil.shutdown();
	}

}
