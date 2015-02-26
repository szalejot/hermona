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

public class TechniqueAddWindow extends JFrame {
	private static final long serialVersionUID = -4241568839154938869L;

	private JPanel p = new JPanel();
	private JButton b = new JButton("Dodaj technikę");
	private JTextField text = new JTextField(20);
	
	public TechniqueAddWindow() {
		super("Dodaj technikę");
		
		setSize(300, 130);
		setResizable(false);
		setLocationRelativeTo(null);
		JLabel l = new JLabel("Podaj nazwę nowej techniki:");
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
				JOptionPane.showMessageDialog(null, "Nie podano nazwy techniki!", "BŁĄD", JOptionPane.WARNING_MESSAGE);
				return;
			}
			DBUtil dbUtil = new DBUtil();
			dbUtil.getTechnique(text.getText());
			JOptionPane.showMessageDialog(null, "Technika '" + text.getText() + "' została dodana", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
	public static void main(String[] args) {
		new TechniqueAddWindow();
	}

}
