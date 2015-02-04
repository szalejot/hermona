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

import model.Teka;
import util.DBUtil;
import util.ToStringListCellRenderer;

public class TekaDeleteWindow extends JFrame {
	private static final long serialVersionUID = -4241568839154938869L;

	private JPanel p = new JPanel();
	private JButton b = new JButton("Usuñ tekê");
	private JComboBox<Teka> comboBox;
	
	@SuppressWarnings("unchecked")
	public TekaDeleteWindow() {
		super("Usuñ tekê");
		DBUtil dbUtil = new DBUtil();
		
		setSize(300, 130);
		setResizable(false);
		setLocationRelativeTo(null);
		JLabel l = new JLabel("Wybierz tekê do usuniêcia:");
		List<Teka> tList = dbUtil.getTekas();
		comboBox = new JComboBox<Teka>(tList.toArray(new Teka[tList.size()]));
		if (comboBox.getItemCount() == 0) {
			b.setEnabled(false);
		}
		
		comboBox.setRenderer(new ToStringListCellRenderer(
				comboBox.getRenderer(), Teka.getStringRenderer()));
		
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
			Teka teka= (Teka) comboBox.getSelectedItem();
			dbUtil.deleteTeka(teka);
			comboBox.removeItemAt(comboBox.getSelectedIndex());
			if (comboBox.getItemCount() == 0) {
				b.setEnabled(false);
			}
			JOptionPane.showMessageDialog(null, "Teka o numerze " + teka.getNumer() + " zosta³a usuniêta", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
	public static void main(String[] args) {
		new TekaDeleteWindow();
	}
	
}
