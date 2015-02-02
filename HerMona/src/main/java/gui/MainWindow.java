package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -2278436951424873713L;
	
	private JPanel p = new JPanel();
	private JButton b = new JButton("Hello");
	
	public MainWindow() {
		super("HerMona");
		
		setSize(800, 600);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		p.add(b);
		add(p);
		
		setVisible(true);
	}
	
	public static void main(String[] args){
		new MainWindow();
	}
	
	
	
}
