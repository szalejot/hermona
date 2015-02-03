package test;

import util.DBUtil;
import junit.framework.TestCase;

public class TestTechnika extends TestCase {
	
	public void testKategoria() {
		System.out.println("--- Test Technika ---");
		DBUtil dbUtil = new DBUtil();
		dbUtil.saveTechnique("testowa1");
		dbUtil.getTechnique("testowa2");
		System.out.println("! -->" + dbUtil.getTechnique("testowa3"));
		System.out.println("! -->" + dbUtil.getTechniques());
		System.out.println("----------------------");
		dbUtil.shutdown();
	}

}
