package test;

import util.DBUtil;
import junit.framework.TestCase;

public class TestTeka extends TestCase {
	
	public void testKategoria() {
		System.out.println("--- Test Teka ---");
		DBUtil dbUtil = new DBUtil();
		dbUtil.saveTeka(1, "testowa1", 1500);
		dbUtil.getTeka(2);
		System.out.println("! -->" + dbUtil.getTeka(3));
		System.out.println("! -->" + dbUtil.getTekas());
		System.out.println("----------------------");
		dbUtil.shutdown();
	}

}
