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

public class CategoryAddWindow extends JFrame {
	private static final long serialVersionUID = -4241568839154938869L;

	private JPanel p = new JPanel();
	private JButton b = new JButton("Dodaj kategorię");
	private JTextField text = new JTextField(20);
	
	public CategoryAddWindow() {
		super("Dodaj kategorię");
		
		setSize(300, 130);
		setResizable(false);
		setLocationRelativeTo(null);
		JLabel l = new JLabel("Podaj nazwę nowej kategorii:");
		b.addActionListener(new ButtonListener());
		p.add(l);
		p.add(text);
		p.add(b);
		add(p);
		setVisible(true);
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (text.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Nie podano nazwy kategorii!", "BŁĄD", JOptionPane.WARNING_MESSAGE);
				return;
			}
			DBUtil dbUtil = new DBUtil();
			dbUtil.getCategory(text.getText());
			JOptionPane.showMessageDialog(null, "Kategoria '" + text.getText() + "' została dodana", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
}
