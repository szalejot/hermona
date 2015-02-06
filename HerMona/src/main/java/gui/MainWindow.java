package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -2278436951424873713L;
	
	public MainWindow() {
		super("HerMona");
		
		setSize(800, 600);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setJMenuBar(initializeMenuBar());
		
		GrafikaPanel gPanel = new GrafikaPanel();
		setContentPane(gPanel);
		
		setVisible(true);
	}
	
	private JMenuBar initializeMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu catMenu = new JMenu("Kategorie");
		menubar.add(catMenu);
		JMenuItem catAdd = new JMenuItem("Dodaj kategorie");
		catMenu.add(catAdd);
		catAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CategoryAddWindow();	
			}
		});
		JMenuItem catDel = new JMenuItem("Usuñ kategorie");
		catMenu.add(catDel);
		catDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CategoryDeleteWindow();
			}
		});
		JMenuItem catEdit = new JMenuItem("Edytuj kategorie");
		catMenu.add(catEdit);
		catEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CategoryEditWindow();
			}
		});
		
		JMenu techMenu = new JMenu("Techniki");
		menubar.add(techMenu);
		JMenuItem techAdd = new JMenuItem("Dodaj techniki");
		techMenu.add(techAdd);
		techAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TechniqueAddWindow();	
			}
		});
		JMenuItem techDel = new JMenuItem("Usuñ techniki");
		techMenu.add(techDel);
		techDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TechniqueDeleteWindow();
			}
		});
		JMenuItem techEdit = new JMenuItem("Edytuj techniki");
		techMenu.add(techEdit);
		techEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TechniqueEditWindow();
			}
		});
		
		JMenu tekaMenu = new JMenu("Teki");
		menubar.add(tekaMenu);
		JMenuItem tekaAdd = new JMenuItem("Dodaj teki");
		tekaMenu.add(tekaAdd);
		tekaAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TekaAddWindow();	
			}
		});
		JMenuItem tekaDel = new JMenuItem("Usuñ teki");
		tekaMenu.add(tekaDel);
		tekaDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TekaDeleteWindow();
			}
		});
		JMenuItem tekaEdit = new JMenuItem("Edytuj teki");
		tekaMenu.add(tekaEdit);
		tekaEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TekaEditWindow();
			}
		});
		
		return menubar;
	}
	
	public static void main(String[] args){
		new MainWindow();
	}
	
	
	
}
