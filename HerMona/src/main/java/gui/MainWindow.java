package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Grafika;
import util.DBUtil;
import util.XLSImporter;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -2278436951424873713L;
	public static final String mainTitle = "Kolekcja grafiki Jana Ponętowskiego";
	
	private GrafikaPanel grafikaPanel;
	
	public MainWindow() {
		super(mainTitle);
		
		setSize(1000, 700);
		setResizable(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				String ObjButtons[] = { "Tak", "Anuluj" };
				int PromptResult = JOptionPane.showOptionDialog(null,
						"Czy na pewno chcesz wyłączyć aplikację?\n"
						+ "Wszelkie niezapisane zmiany zostaną utracone.",
						"",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[1]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		setJMenuBar(initializeMenuBar());
		
		grafikaPanel = new GrafikaPanel(this);
		setContentPane(grafikaPanel);
		
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
		JMenuItem catDel = new JMenuItem("Usuń kategorie");
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
		JMenuItem techDel = new JMenuItem("Usuń techniki");
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
		JMenuItem tekaDel = new JMenuItem("Usuń teki");
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
		
		JMenu importMenu = new JMenu("Importuj");
		menubar.add(importMenu);
		JMenuItem importXLS = new JMenuItem("Importuj z pliku XLSX");
		importMenu.add(importXLS);
		importXLS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel", "xlsx");
				fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            XLSImporter importer = new XLSImporter();
		            importer.importXLS(file.getAbsolutePath());
		            JOptionPane.showMessageDialog(null, "Import został wykonany", "", JOptionPane.PLAIN_MESSAGE);
		        }
			}
		});
		JMenuItem refreshImg = new JMenuItem("Odśwież miniatury");
		importMenu.add(refreshImg);
		refreshImg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DBUtil dbUtil = new DBUtil();
				List<Grafika> gList = dbUtil.getGrafikas("");
				for (Grafika g : gList) {
					dbUtil.makeMiniature(g);
				}
			}
		});
		
		JMenu extrasMenu = new JMenu("Dodatki");
		menubar.add(extrasMenu);
		JMenuItem findByText = new JMenuItem("Wyszukaj tekst");
		extrasMenu.add(findByText);
		findByText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grafikaPanel.findBytext();
			}
		});
		
		JMenuItem ReplaceText = new JMenuItem("Zamień tekst");
		extrasMenu.add(ReplaceText);
		ReplaceText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ReplaceWindow();
			}
		});
		
		return menubar;
	}
	
	
	public static void main(String[] args){
		new MainWindow();
	}
	
	
	
}
