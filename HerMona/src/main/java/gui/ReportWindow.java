package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Grafika;
import util.DBUtil;

public class ReportWindow extends JFrame {
	private static final long serialVersionUID = -5443633378890437915L;
	
	private JPanel p = new JPanel();
	private JButton b = new JButton("Generuj raport");
	private JTextField tytulTextField = new JTextField("raport");
	private JComboBox<String> typeComboBox = new JComboBox<String>(new String[]{"HTML", "CSV"});
	private DBUtil dbUtil = new DBUtil();
	private String predicate;
	
	private JList<String> jList;
	
	public ReportWindow(String predicate) {
		super("Generuj raport");
		this.predicate = predicate;
		initializeList();
		setSize(300, 480);
		setResizable(false);
		setLocationRelativeTo(null);
		
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateReport();
			}
		});
		tytulTextField.setSize(50, 15);
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(new JLabel("Wybierz pola dla raportu:"));
		p.add(jList);
		p.add(new JLabel("Podaj nazwê pliku raportu:"));
		p.add(tytulTextField);
		p.add(typeComboBox);
		p.add(b);
		
		add(p);
		setVisible(true);
	}
	
	private void generateReport() {
		List<String> valList = jList.getSelectedValuesList();
		boolean isHtml = typeComboBox.getSelectedItem().toString().toLowerCase().equals("html");
		if (valList.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Nie wybrano kolumn do raportu", "B£¥D", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			File file = new File(tytulTextField.getText() + "."
					+ typeComboBox.getSelectedItem().toString().toLowerCase());
			if (!file.exists()) {
				file.createNewFile();
			}

			OutputStreamWriter char_output = new OutputStreamWriter(
				     new FileOutputStream(file.getAbsoluteFile()),
				     Charset.forName("UTF-8").newEncoder()
				 );
			
			if (isHtml) {
				char_output.write(getHTMLBeginning());
			}
			
			char_output.write(getLineBegining(isHtml));
			for (String s : valList) {
				char_output.write(s);
				char_output.write(getSeparator(isHtml));
			}
			char_output.write(getLineEnding(isHtml));
			
			List<Grafika> gList =  dbUtil.getGrafikas(predicate);
			
			for (Grafika g : gList) {
				char_output.write(getLineBegining(isHtml));
				for (String s : valList) {
					String toFile = getField(g, s, isHtml);
					if (!isHtml && toFile.lastIndexOf(';') > -1) {
						toFile = "\"" + toFile + "\"";
					}
					char_output.write(toFile);
					char_output.write(getSeparator(isHtml));
				}
				char_output.write(getLineEnding(isHtml));
			}

			if (isHtml) {
				char_output.write(getHTMLEnding());
			}
			char_output.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "B³¹d podczas generowania raportu:\n" + e.getMessage(), "B£¥D", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
			return;
		}
		JOptionPane.showMessageDialog(null, "Zakoñczono generowanie raportu", "", JOptionPane.PLAIN_MESSAGE);
	}
	
	private String getHTMLBeginning(){
		return "<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='Type=text/html; charset=utf-8'></head><body>\n"
				+ "<table border=\"1\">";
	}
	
	private String getHTMLEnding(){
		return "</table></body></html>\n";
	}
	
	private String getLineBegining(boolean html) {
		if (html) {
			return "<tr><td>";
		} else {
			return "";
		}
	}
	
	private String getLineEnding(boolean html) {
		if (html) {
			return "</td></tr>\n";
		} else {
			return "\n";
		}
	}
	
	private String getSeparator(boolean html) {
		if (html) {
			return "</td><td>";
		} else {
			return ";";
		}
	}
	
	private String getField(Grafika g, String fieldName, boolean isHtml) {
		switch(fieldName) {
			case "teka":
				return getNullableString(g.getTeka(), isHtml);
			case "numer inwentarza":
				return getNullableString(g.getNumerInwentarza(), isHtml);
			case "temat":
				return getNullableString(g.getTemat(), isHtml);
			case "seria":
				return getNullableString(g.getSeria(), isHtml);
			case "technika":
				return getNullableString(g.getTechniki().toString(), isHtml);
			case "wymiary":
				return getNullableString(g.getWymiary(), isHtml);
			case "projekatant":
				return getNullableString(g.getProjektant(), isHtml);
			case "rytownik":
				return getNullableString(g.getRytownik(), isHtml);
			case "wydawca":
				return getNullableString(g.getWydawca(), isHtml);
			case "sygnatury":
				return getNullableString(g.getSygnatury(), isHtml);
			case "rok od":
				return getNullableString(g.getRokOd(), isHtml);
			case "rok do":
				return getNullableString(g.getRokDo(), isHtml);
			case "miejsce wydania":
				return getNullableString(g.getMiejsceWydania(), isHtml);
			case "opis":
				return getNullableString(g.getOpis(), isHtml);
			case "inskrypcje":
				return getNullableString(g.getInskrypcje(), isHtml);
			case "bibliografia":
				return getNullableString(g.getBibliografia(), isHtml);
			case "uwagi":
				return getNullableString(g.getUwagi(), isHtml);
			case "kategorie":
				return getNullableString(g.getKategorie().toString(), isHtml);
			case "ilustracja":
				return "<a href=\"file:///" + getNullableString(g.getIlustracjaPath(), isHtml) + "\">" +
					"<img border=\"0\" src=\"file:///" + System.getProperty("user.dir") + "\\Hermona_miniatury\\" + getNullableString(g.getTeka().getNumer(), isHtml) + "_" + getNullableString(g.getNumerInwentarza(), isHtml) +  ".png\"></a>";
			default:
				return "";
		}
	}
	
	private void initializeList() {
		jList = new JList<String>(new String[]{"teka", "numer inwentarza", "temat", "seria",
				"technika", "wymiary", "projekatant", "rytownik", "wydawca", "sygnatury", "rok od",
				"rok do", "miejsce wydania", "opis", "inskrypcje", "bibliografia", "uwagi", "kategorie", "ilustracja"});
	}
	
	private String getNullableString(Object obj, boolean isHtml) {
		if (obj != null) {
			String ret = obj.toString();
			ret = ret.replace('\n', ' ');
			return ret;
		} else {
			return "";
		}
	}

}
