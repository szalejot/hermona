package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Kategoria;
import util.DBUtil;
import util.ToStringListCellRenderer;

public class CategoryDeleteWindow extends JFrame {
	private static final long serialVersionUID = -4241568839154938869L;

	private JPanel p = new JPanel();
	private JButton b = new JButton("Usuń kategorię");
	private JComboBox<Kategoria> comboBox;
	
	@SuppressWarnings("unchecked")
	public CategoryDeleteWindow() {
		super("Usuń kategorie");
		DBUtil dbUtil = new DBUtil();
		
		setSize(300, 130);
		setResizable(false);
		setLocationRelativeTo(null);
		JLabel l = new JLabel("Wybierz kategorię do usunięcia:");
		List<Kategoria> kList = dbUtil.getCategories();
		comboBox = new JComboBox<Kategoria>(kList.toArray(new Kategoria[kList.size()]));
		if (comboBox.getItemCount() == 0) {
			b.setEnabled(false);
		}
		
		comboBox.setRenderer(new ToStringListCellRenderer(
				comboBox.getRenderer(), Kategoria.getStringRenderer()));
		
		b.addActionListener(new ButtonListener());
		p.add(l);
		p.add(comboBox);
		p.add(b);
		add(p);
		setVisible(true);
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DBUtil dbUtil = new DBUtil();
			Kategoria kat = (Kategoria) comboBox.getSelectedItem();
			dbUtil.deleteCategory(kat);
			comboBox.removeItemAt(comboBox.getSelectedIndex());
			if (comboBox.getItemCount() == 0) {
				b.setEnabled(false);
			}
			JOptionPane.showMessageDialog(null, "Kategoria '" + kat.getNazwa() + "' została usunięta", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	


	public static void main(String[] args) {
		new CategoryDeleteWindow();
	}
	
}
