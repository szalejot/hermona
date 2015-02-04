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

import model.Technika;
import util.DBUtil;
import util.ToStringListCellRenderer;

public class TechniqueDeleteWindow extends JFrame {
	private static final long serialVersionUID = -4241568839154938869L;

	private JPanel p = new JPanel();
	private JButton b = new JButton("Usuñ technikê");
	private JComboBox<Technika> comboBox;
	
	@SuppressWarnings("unchecked")
	public TechniqueDeleteWindow() {
		super("Usuñ technikê");
		DBUtil dbUtil = new DBUtil();
		
		setSize(300, 130);
		setResizable(false);
		setLocationRelativeTo(null);
		JLabel l = new JLabel("Wybierz technikê do usuniêcia:");
		List<Technika> tList = dbUtil.getTechniques();
		comboBox = new JComboBox<Technika>(tList.toArray(new Technika[tList.size()]));
		if (comboBox.getItemCount() == 0) {
			b.setEnabled(false);
		}
		
		comboBox.setRenderer(new ToStringListCellRenderer(
				comboBox.getRenderer(), Technika.getStringRenderer()));
		
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
			Technika tech= (Technika) comboBox.getSelectedItem();
			dbUtil.deleteTechnique(tech);
			comboBox.removeItemAt(comboBox.getSelectedIndex());
			if (comboBox.getItemCount() == 0) {
				b.setEnabled(false);
			}
			JOptionPane.showMessageDialog(null, "Technika '" + tech.getNazwa() + "' zosta³a usuniêta", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
	public static void main(String[] args) {
		new TechniqueDeleteWindow();
	}
	
}
