package test;

import util.DBUtil;
import junit.framework.TestCase;

public class TestKategoria extends TestCase {
	
	public void testKategoria() {
		System.out.println("--- Test Kategoria ---");
		DBUtil dbUtil = new DBUtil();
		dbUtil.saveCategory("testowa1");
		dbUtil.getCategory("testowa2");
		System.out.println("! -->" + dbUtil.getCategory("testowa3"));
		System.out.println("! -->" + dbUtil.getCategories());
		System.out.println("----------------------");
		dbUtil.shutdown();
	}

}
