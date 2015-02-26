package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.DBUtil;

public class TekaAddWindow extends JFrame {
	private static final long serialVersionUID = -4241568839154938869L;

	private JPanel p = new JPanel();
	private JButton b = new JButton("Dodaj tekę");
	private JTextField textNumer = new JTextField(6);
	private JTextField textTytul = new JTextField(20);
	private JTextField textRok = new JTextField(6);
	
	public TekaAddWindow() {
		super("Dodaj tekę");
		
		setSize(300, 165);
		setResizable(false);
		setLocationRelativeTo(null);
		JLabel l1 = new JLabel("Podaj numer nowej teki:");
		JLabel l2 = new JLabel("Podaj tytuł nowej teki:");
		JLabel l3 = new JLabel("Podaj rok nowej teki:");
		b.addActionListener(new ButtonListener());
		p.add(l1);
		p.add(textNumer);
		p.add(l2);
		p.add(textTytul);
		p.add(l3);
		p.add(textRok);
		p.add(b);
		add(p);
		setVisible(true);
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (textNumer.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Nie podano numeru teki!", "BŁĄD", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Integer rok;
			Integer numer;
			try {

				numer = Integer.parseInt(textNumer.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Podana wartość dla numeru nie jest liczbą całkowitą!", "BŁĄD", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				if(textRok.getText().isEmpty()) {
					rok = null;
				} else {
					rok = Integer.parseInt(textRok.getText().trim());
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Podana wartość dla roku nie jest liczbą całkowitą!", "BŁĄD", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			DBUtil dbUtil = new DBUtil();
			if (dbUtil.existsTekaInDb(numer)) {
				JOptionPane.showMessageDialog(null, "Teka o numerze " + numer + " jest już w bazie!", "BŁĄD", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			dbUtil.saveTeka(numer, textTytul.getText(), rok);
			JOptionPane.showMessageDialog(null, "Teka o numerze " + textNumer.getText() + " została dodana", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
	public static void main(String[] args) {
		new TekaAddWindow();
	}
	
}
