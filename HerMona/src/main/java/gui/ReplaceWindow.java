package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Grafika;
import util.DBUtil;

public class ReplaceWindow extends JFrame {
	private static final long serialVersionUID = -5443633378890437916L;
	private static final Insets WEST_INSETS = new Insets(1, 0, 1, 1);
	private static final Insets EAST_INSETS = new Insets(1, 1, 1, 0);
	
	private JPanel p = new JPanel();
	private JLabel labelBefore = new JLabel("Tekst przed zmianą:");
	private JLabel labelAfter = new JLabel("Tekst po zmianie:");
	private JButton bOk = new JButton("Zastosuj zmianę");
	private JButton bNext = new JButton("Następna ->");
	private JButton bEnd = new JButton("Zakończ");
	private JTextArea beforeTF = new JTextArea("");
	private JTextArea afterTF = new JTextArea("");
	private String[] possibleFields = {"temat", "seria",
			"wymiary", "projektant", "rytownik", "wydawca", "innyAutor", "sygnatury", 
			"miejsceWydania", "opis", "inskrypcje", "katalogi", "bibliografia", "uwagi"};
	
	private String selectedField;
	private String searchString;
	private String replaceString;
	
	private Grafika grafika;
	private Iterator<Grafika> grafikaIterator;
	
	
	private DBUtil dbUtil = new DBUtil();
	
	public ReplaceWindow() {
		super("Zamiana tekstu");
		
		selectedField = (String)JOptionPane.showInputDialog(null,
				"Wybierz pole, w którym będzie wyszukiwanie", "Wybór pola",
				JOptionPane.INFORMATION_MESSAGE, null,
				possibleFields, possibleFields[0]);
		
		searchString = (String)JOptionPane.showInputDialog(
                null,
                "Wpisz tekst do wyszukania:",
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
		
		replaceString = (String)JOptionPane.showInputDialog(
                null,
                "Wpisz tekst po zmianie:",
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
		
		
		setSize(600, 530);
		setResizable(true);
		setLocationRelativeTo(null);
		
		bOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//generateReport();
			}
		});
		
		bNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToNextGrafika();
			}
		});
		
		beforeTF.setEditable(false);
		afterTF.setEditable(false);
		initGrafikaSearch();
		
		p.setLayout(new GridBagLayout());
		labelBefore.setSize(200, 20);
		p.add(labelBefore, createGbc(0, 0, 0.0));
		JScrollPane sp1 = new JScrollPane(beforeTF);
		p.add(sp1, createGbc(0, 1));
		labelAfter.setSize(200, 20);
		p.add(labelAfter, createGbc(0, 2, 0.0));
		JScrollPane sp2 = new JScrollPane(afterTF);
		p.add(sp2, createGbc(0, 3));
		
		JPanel pButton = new JPanel();
		pButton.setLayout(new GridBagLayout());
		pButton.add(bOk, createGbc(0, 0));
		pButton.add(bNext, createGbc(1, 0));
		pButton.add(bEnd, createGbc(2, 0));
		
		p.add(pButton, createGbc(0, 4, 0.0));
		
		add(p);
		setVisible(true);
	}
	

	private void initGrafikaSearch() {
		List<Grafika> list = dbUtil.getGrafikasByString(selectedField, searchString);
		grafikaIterator = list.iterator();
		//TODO zrobic zabezpieczenie jak nie bedzie grafik spelniajacych warunek
		grafika = grafikaIterator.next();
		beforeTF.setText((String)grafika.getFieldByName(selectedField));
	}
	
	private void switchToNextGrafika() {
		if (grafikaIterator.hasNext()) {
			grafika = grafikaIterator.next();
			beforeTF.setText((String)grafika.getFieldByName(selectedField));
		}
	}
	
	
	private GridBagConstraints createGbc(int x, int y) {
		return createGbc(x, y, 1.0);
	}
	
	private GridBagConstraints createGbc(int x, int y, double weighty) {
	      GridBagConstraints gbc = new GridBagConstraints();
	      gbc.gridx = x;
	      gbc.gridy = y;
	      gbc.gridwidth = 1;
	      gbc.gridheight = 1;

	      gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
	      gbc.fill = (x == 0) ? GridBagConstraints.BOTH
	            : GridBagConstraints.HORIZONTAL;

	      gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
	      gbc.weightx = (x == 0) ? 0.1 : 1.0;
	      gbc.weighty = weighty;
	      return gbc;
	   }
	
	

}
