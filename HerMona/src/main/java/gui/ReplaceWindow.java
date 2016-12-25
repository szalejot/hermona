package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.DBUtil;

public class ReplaceWindow extends JFrame {
	private static final long serialVersionUID = -5443633378890437916L;
	
	private JPanel p = new JPanel();
	private JButton b = new JButton("OK");
	private JTextField tytulTextField = new JTextField("");
	private String[] possibleFields = {"temat", "seria",
			"wymiary", "projektant", "rytownik", "wydawca", "innyAutor", "sygnatury", 
			"miejsceWydania", "opis", "inskrypcje", "katalogi", "bibliografia", "uwagi"};
	private DBUtil dbUtil = new DBUtil();
	
	public ReplaceWindow() {
		super("Zamiana tekstu");
		
		
		
		
		String selectedField = (String)JOptionPane.showInputDialog(null,
				"Wybierz pole, w którym będzie wyszukiwanie", "Wybór pola",
				JOptionPane.INFORMATION_MESSAGE, null,
				possibleFields, possibleFields[0]);
		
		String searchString = (String)JOptionPane.showInputDialog(
                null,
                "Wpisz tekst do wyszukania:",
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
		
		String replaceString = (String)JOptionPane.showInputDialog(
                null,
                "Wpisz tekst po zmianie:",
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
		
		
		setSize(300, 530);
		setResizable(false);
		setLocationRelativeTo(null);
		
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//generateReport();
			}
		});
		tytulTextField.setSize(50, 15);
		
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(new JLabel("Wybierz pole, w którym będzie wyszukiwanie:"));
		p.add(new JLabel("Podaj tekst do wyszukania:"));
		p.add(tytulTextField);
		p.add(b);
		
		add(p);
		setVisible(true);
	}
	
	
	
	

}
