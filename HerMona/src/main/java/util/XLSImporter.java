package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import model.Grafika;
import model.Kategoria;
import model.Technika;
import model.Teka;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class XLSImporter {
	
	private static final String NR_INWENTARZA_NAME = "nr inwentarza";
	private static final String TEMAT_NAME = "temat";
	private static final String PROJEKTANT_NAME = "projektant";
	private static final String RYTOWNIK_NAME = "rytownik";
	private static final String WYDAWCA_NAME = "wydawca";
	private static final String SYGNATURY_NAME = "sygnatury";
	private static final String DATA_OD_DO_NAME = "data od do";
	private static final String MIEJSCE_WYDANIA_NAME = "miejsce wydania";
	private static final String OPIS_NAME = "opis";
	private static final String INSKRYPCJE_NAME = "inskrypcje";
	private static final String WYMIARY_NAME = "wymiary";
	private static final String BIBLIOGRAFIA_NAME = "bibliografia";
	private static final String SERIA_NAME = "seria";
	private static final String UWAGI_NAME = "uwagi";
	private static final String KATEGORIA_NAME = "kategoria";
	private static final String TEKA_NAME = "teka";
	private static final String TECHNIKA_NAME = "technika";
	private static final String KATALOGI_NAME = "katalogi";
	private static final String INNY_AUTOR_NAME = "inny autor";
	private static final String NUMER_KATALOGOWY_NAME = "numer katalogowy";
	private static final String DATOWANIE_NAME = "datowanie";

	private DBUtil dbUtil = new DBUtil();
	
	public void importXLS(String fileName) {
		try
        {
            FileInputStream file = new FileInputStream(new File(fileName));
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();
            Map<String, Integer> headerMap = parseHeader(row);
            
            while (rowIterator.hasNext())
            {
                row = rowIterator.next();
                Grafika g = new Grafika();
                g.setNumerInwentarza(getValue(row, headerMap.get(NR_INWENTARZA_NAME)));
                g.setTemat(getValue(row, headerMap.get(TEMAT_NAME)));
                g.setProjektant(getValue(row, headerMap.get(PROJEKTANT_NAME)));
                g.setRytownik(getValue(row, headerMap.get(RYTOWNIK_NAME)));
                g.setWydawca(getValue(row, headerMap.get(WYDAWCA_NAME)));
                g.setSygnatury(getValue(row, headerMap.get(SYGNATURY_NAME)));
                g.setMiejsceWydania(getValue(row, headerMap.get(MIEJSCE_WYDANIA_NAME)));
                g.setOpis(getValue(row, headerMap.get(OPIS_NAME)));
                g.setInskrypcje(getValue(row, headerMap.get(INSKRYPCJE_NAME)));
                g.setWymiary(getValue(row, headerMap.get(WYMIARY_NAME)));
                g.setBibliografia(getValue(row, headerMap.get(BIBLIOGRAFIA_NAME)));
                g.setSeria(getValue(row, headerMap.get(SERIA_NAME)));
                g.setUwagi(getValue(row, headerMap.get(UWAGI_NAME)));
                g.setTeka(getTeka(row, headerMap.get(TEKA_NAME)));
                g.setTechniki(getTechniki(row, headerMap.get(TECHNIKA_NAME)));
                g.setTeka(getTeka(row, headerMap.get(TEKA_NAME)));
                g.setKategorie(getKategorie(row, headerMap.get(KATEGORIA_NAME)));
                g.setKatalogi(getValue(row, headerMap.get(KATALOGI_NAME)));
                g.setInnyAutor(getValue(row, headerMap.get(INNY_AUTOR_NAME)));
                g.setNumerKatalogowy(getValue(row, headerMap.get(NUMER_KATALOGOWY_NAME)));
                g.setDatowanie(getValue(row, headerMap.get(DATOWANIE_NAME)));
                setDataOdDo(g, row, headerMap.get(DATA_OD_DO_NAME));
                
                if (g.getTeka() != null && g.getTeka().getNumer() != null && g.getNumerInwentarza() != null) {
                	dbUtil.saveGrafika(g);
                }
            }
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	private Teka getTeka(Row row, Integer pos) {
		String sNum = getValue(row, pos);
		if (sNum == null) {
			return null;
		} else {
			return dbUtil.getTeka(Integer.parseInt(sNum.trim()));
		}
	}
	
	private Set<Kategoria> getKategorie(Row row, Integer pos) {
		Set<Kategoria> set = new HashSet<Kategoria>();
		String sVal = getValue(row, pos);
		if (sVal != null) {
			String[] sNames = sVal.split(";");
			for (String s : sNames) {
				set.add(dbUtil.getCategory(s.trim()));
			}
		}
		return set;
	}
	
	private Set<Technika> getTechniki(Row row, Integer pos) {
		Set<Technika> set = new HashSet<Technika>();
		String sVal = getValue(row, pos);
		if (sVal != null) {
			String[] sNames = sVal.split(",");
			for (String s : sNames) {
				set.add(dbUtil.getTechnique(s.trim()));
			}
		}
		return set;
	}
	
	private Grafika setDataOdDo(Grafika g, Row row, Integer pos) {
		String sVal = getValue(row, pos);
		try {
		if (sVal != null && sVal.trim().length() > 0) {
			sVal = sVal.trim();
			if (sVal.length() == 4) { //np "1501"
				g.setRokOd(Integer.parseInt(sVal));
				g.setRokDo(Integer.parseInt(sVal));
			} else if (sVal.lastIndexOf("-") > -1) { // np "1501 - 1502"
				String[] sArr = sVal.split("-");
				g.setRokOd(Integer.parseInt(sArr[0].trim()));
				g.setRokDo(Integer.parseInt(sArr[1].trim()));
			} else if (sVal.startsWith("ok")) { // np "ok. 1580"
				String sTmp = sVal.substring(sVal.length() - 4, sVal.length() - 1);
				g.setRokOd(Integer.parseInt(sTmp) - 9);
				g.setRokDo(Integer.parseInt(sTmp) + 9);
			} else { //WTF?
				JOptionPane.showMessageDialog(null, "Błędny format datowania: '" + sVal + "'\n"
						+ "Dla grafiki o numerze inwentarza: " + g.getNumerInwentarza()
						+ "\nz teki: " + g.getTeka(), "BŁĄD", JOptionPane.WARNING_MESSAGE);
			}
		}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Błędny format liczbowy datowania: '" + sVal + "'\n"
					+ e.getMessage() + "\n"
					+ "Dla grafiki o numerze inwentarza: " + g.getNumerInwentarza()
					+ "\nz teki: " + g.getTeka(), "BŁĄD", JOptionPane.WARNING_MESSAGE);
		}
		return g;
	}
	
	private String getValue(Row row, Integer pos) {
		if (pos < 0) {
			return null;
		}
		Cell cell = row.getCell(pos);
		if (cell == null) {
			return null;
		} else {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return new Integer(new Double(cell.getNumericCellValue()).intValue()).toString().trim();
			} else { // Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue().trim();
			}
		}
	}
	
	private Map<String, Integer> parseHeader (Row row) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		//initial values
		map.put(NR_INWENTARZA_NAME, -1);
		map.put(TEMAT_NAME, -1);
		map.put(PROJEKTANT_NAME, -1);
		map.put(RYTOWNIK_NAME, -1);
		map.put(WYDAWCA_NAME, -1);
		map.put(SYGNATURY_NAME, -1);
		map.put(DATA_OD_DO_NAME, -1);
		map.put(MIEJSCE_WYDANIA_NAME, -1);
		map.put(OPIS_NAME, -1);
		map.put(INSKRYPCJE_NAME, -1);
		map.put(WYMIARY_NAME, -1);
		map.put(BIBLIOGRAFIA_NAME, -1);
		map.put(SERIA_NAME, -1);
		map.put(UWAGI_NAME, -1);
		map.put(KATEGORIA_NAME, -1);
		map.put(TEKA_NAME, -1);
		map.put(TECHNIKA_NAME, -1);
		map.put(KATALOGI_NAME, -1);
		map.put(INNY_AUTOR_NAME, -1);
		map.put(NUMER_KATALOGOWY_NAME, -1);
		map.put(DATOWANIE_NAME, -1);

		short minColIx = row.getFirstCellNum();
		short maxColIx = row.getLastCellNum();
		for (short colIx = minColIx; colIx < maxColIx; colIx++) {
			Cell cell = row.getCell(colIx);
			if (cell == null) {
				continue;
			}
			map.put(cell.getStringCellValue().toLowerCase(), new Integer(colIx));
		}

		return map;
	}
	
	public static void main(String[] args) {
		XLSImporter imp = new XLSImporter();
		imp.importXLS("C:\\temp\\153.xlsx");
	}
	
}
