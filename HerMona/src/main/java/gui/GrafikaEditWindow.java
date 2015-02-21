package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import model.Grafika;
import model.Kategoria;
import model.Technika;
import model.Teka;
import util.DBUtil;
import util.ImageImplement;

public class GrafikaEditWindow extends JFrame {
	private static final long serialVersionUID = 1113700118119705921L;
	private static final Insets WEST_INSETS = new Insets(1, 0, 1, 1);
	private static final Insets EAST_INSETS = new Insets(1, 1, 1, 0);
	private static final Dimension taMin = new Dimension(400, 40);
	private static final Dimension taMax = new Dimension(400, 200);
	private JPanel p = new JPanel();
	private JButton bSave = new JButton("Zapisz");
	private DBUtil dbUtil = new DBUtil();
	private Grafika grafika;
	
	private JComboBox<Teka> tekaCB;
	private JTextField numerInwentarzaTF;
	private JTextField tematTF;
	private JTextField seriaTF;
	private JTextField projektantTF;
	private JTextField rytownikTF;
	private JTextField wydawcaTF;
	private JList<Technika> technikiJL;
	private JButton technikiB;
	private Set<Technika> techS;
	private JTextField wymiaryTF;
	private JTextField miejsceWydaniaTF;
	private JTextField rokOdTF;
	private JTextField rokDoTF;
	private JTextArea sygnaturyTA;
	private JTextArea opisTA;
	private JTextArea uwagiTA;
	private JTextArea inskrypcjeTA;
	private JTextArea bibliografiaTA;
	private JList<Kategoria> kategorieJL;
	private JButton kategorieB;
	private Set<Kategoria> katS;

	public GrafikaEditWindow(Grafika g) {
		super("Edytuj grafikê");

		grafika = g;
		setSize(1000, 700);
		setResizable(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				String ObjButtons[] = { "Tak", "Anuluj" };
				int PromptResult = JOptionPane
						.showOptionDialog(
								null,
								"Czy na pewno chcesz zamkn¹æ okno?\n"
										+ "Wszelkie niezapisane zmiany zostan¹ utracone.",
								"", JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE, null, ObjButtons,
								ObjButtons[1]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					GrafikaEditWindow.this.setVisible(false);
					GrafikaEditWindow.this.dispose();
				}
			}
		});
		bSave.addActionListener(new ButtonSaveListener());
		bSave.setMaximumSize(new Dimension(100, 35));
		bSave.setMinimumSize(new Dimension(100, 35));
		bSave.setPreferredSize(new Dimension(100, 35));
		bSave.setSize(new Dimension(100, 35));

		JPanel leftContainer = new JPanel(new GridBagLayout());
		leftContainer.setMaximumSize(new Dimension(500, 480));
		leftContainer.setSize(new Dimension(500, 480));
		leftContainer.add(new JLabel("teka"), createGbc(0, 0));
		leftContainer.add(getTekaContainer(), createGbc(1, 0));
		leftContainer.add(new JLabel("numer inwentarza"), createGbc(0, 1));
		leftContainer.add(getNumerInewntarzaContainer(), createGbc(1, 1));
		leftContainer.add(new JLabel("temat"), createGbc(0, 2));
		leftContainer.add(getTematContainer(), createGbc(1, 2));
		leftContainer.add(new JLabel("sygmatury"), createGbc(0, 3));
		leftContainer.add(getSygnaturyContainer(), createGbc(1, 3));
		leftContainer.add(new JLabel("seria"), createGbc(0, 4));
		leftContainer.add(getSeriaContainer(), createGbc(1, 4));
		leftContainer.add(new JLabel("projektant"), createGbc(0, 5));
		leftContainer.add(getProjektantContainer(), createGbc(1, 5));
		leftContainer.add(new JLabel("rytownik"), createGbc(0, 6));
		leftContainer.add(getRytownikContainer(), createGbc(1, 6));
		leftContainer.add(new JLabel("wydawca"), createGbc(0, 7));
		leftContainer.add(getWydawcaContainer(), createGbc(1, 7));
		leftContainer.add(new JLabel("technika"), createGbc(0, 8));
		leftContainer.add(getTechnikaContainer(), createGbc(1, 8));
		leftContainer.add(new JLabel("wymiary"), createGbc(0, 9));
		leftContainer.add(getWymiaryContainer(), createGbc(1, 9));
		leftContainer.add(new JLabel("miejsce wydania"), createGbc(0, 10));
		leftContainer.add(getMiejsceWydaniaContainer(), createGbc(1, 10));
		leftContainer.add(new JLabel("rok od"), createGbc(0, 11));
		leftContainer.add(getRokOdContainer(), createGbc(1, 11));
		leftContainer.add(new JLabel("rok do"), createGbc(0, 12));
		leftContainer.add(getRokDoContainer(), createGbc(1, 12));
		leftContainer.add(new JLabel("kategorie"), createGbc(0, 13));
		leftContainer.add(getKategorieContainer(), createGbc(1, 13));
		
		p.setLayout(new BorderLayout());
		p.add(leftContainer, BorderLayout.LINE_START);
		p.add(getImageContainer(), BorderLayout.LINE_END);

		Container bottomContainer = new Container();
		bottomContainer.setLayout(new GridBagLayout());
		bottomContainer.add(new JLabel("opis"), createGbc(0, 0));
		bottomContainer.add(getOpisContainer(), createGbc(0, 1));
		bottomContainer.add(new JLabel("uwagi"), createGbc(0, 2));
		bottomContainer.add(getUwagiContainer(), createGbc(0, 3));
		bottomContainer.add(new JLabel("inskrypcje"), createGbc(0, 4));
		bottomContainer.add(getInskrypcjeContainer(), createGbc(0, 5));
		bottomContainer.add(new JLabel("bibliografia"), createGbc(0, 6));
		bottomContainer.add(getBibliografiaContainer(), createGbc(0, 7));
		GridBagConstraints buttonGBC = new GridBagConstraints();
		buttonGBC.gridx = 0;
		buttonGBC.gridy = 8;
		bottomContainer.add(bSave, buttonGBC);
		p.add(bottomContainer, BorderLayout.PAGE_END);

		JScrollPane thePane = new JScrollPane(p);
		add(thePane);
		setVisible(true);
	}
	
	private GridBagConstraints createGbc(int x, int y) {
	      GridBagConstraints gbc = new GridBagConstraints();
	      gbc.gridx = x;
	      gbc.gridy = y;
	      gbc.gridwidth = 1;
	      gbc.gridheight = 1;

	      gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
	      gbc.fill = (x == 0) ? GridBagConstraints.BOTH
	            : GridBagConstraints.HORIZONTAL;

	      gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
	      gbc.weightx = (x == 0) ? 0.1 : 1.0;
	      gbc.weighty = 1.0;
	      return gbc;
	   }
	
	private Container getTekaContainer() {
		List<Teka> list = dbUtil.getTekas();
		tekaCB = new JComboBox<Teka>(list.toArray(new Teka[list.size()]));
		tekaCB.setSelectedItem(grafika.getTeka());
		Container container = new JPanel();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(tekaCB);
		return container;
	}
	
	private Container getNumerInewntarzaContainer() {
		numerInwentarzaTF = new JTextField(grafika.getNumerInwentarza(), 12);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(numerInwentarzaTF);
		return container;
	}
	
	private Container getTematContainer() {
		tematTF = new JTextField(grafika.getTemat(), 40);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(tematTF);
		return container;
	}
	
	private Container getSeriaContainer() {
		seriaTF = new JTextField(grafika.getSeria(), 40);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(seriaTF);
		return container;
	}
	
	private Container getSygnaturyContainer() {
		sygnaturyTA = new JTextArea(grafika.getSygnatury(), 4, 20);
		sygnaturyTA.setLineWrap(true);
		sygnaturyTA.setWrapStyleWord(true);
		Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(new JScrollPane (sygnaturyTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		return container;
	}
	
	private Container getProjektantContainer() {
		projektantTF = new JTextField(grafika.getProjektant(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(projektantTF);
		return container;
	}
	
	private Container getRytownikContainer() {
		rytownikTF = new JTextField(grafika.getRytownik(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(rytownikTF);
		return container;
	}
	
	private Container getWydawcaContainer() {
		wydawcaTF = new JTextField(grafika.getWydawca(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(wydawcaTF);
		return container;
	}
	
	private Container getTechnikaContainer() {
		List<Technika> list = dbUtil.getTechniques();
		technikiJL = new JList<Technika>(list.toArray(new Technika[list.size()]));
		technikiJL.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		List<Integer> indices = new ArrayList<Integer>();
		techS = new HashSet<Technika>();
    	for (int i = 0; i < technikiJL.getModel().getSize(); i++) {
    		Technika tech = technikiJL.getModel().getElementAt(i);
    		if (grafika.getTechniki().contains(tech)) {
    			indices.add(i);
    			techS.add(tech);
    		}
    	}
    	int[] intArr = new int[indices.size()];
    	for (int i = 0; i < indices.size(); i++) {
    		intArr[i] = indices.get(i);
    	}
    	technikiJL.setSelectedIndices(intArr);
    	
    	technikiB = new JButton(grafika.getTechniki().toString());
    	technikiB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, technikiJL, "Wybierz techniki", JOptionPane.PLAIN_MESSAGE);
		    	
				techS.clear();
				int[] intArr = technikiJL.getSelectedIndices();
		    	for (int i = 0; i < intArr.length; i++) {
		    		Technika tech = technikiJL.getModel().getElementAt(intArr[i]);
		    		techS.add(tech);
		    	}
		    	technikiB.setText(techS.toString());
			}
		});
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(technikiB);
		return container;
	}
	
	private Container getWymiaryContainer() {
		wymiaryTF = new JTextField(grafika.getWymiary(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(wymiaryTF);
		return container;
	}
	
	private Container getMiejsceWydaniaContainer() {
		miejsceWydaniaTF = new JTextField(grafika.getMiejsceWydania(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(miejsceWydaniaTF);
		return container;
	}
	
	private Container getRokOdContainer() {
		rokOdTF = new JTextField((grafika.getRokOd() == null ? "" : grafika.getRokOd().toString()), 6);
		PlainDocument doc = (PlainDocument) rokOdTF.getDocument();
	    doc.setDocumentFilter(new MyIntFilter());
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(rokOdTF);
		return container;
	}
	
	private Container getRokDoContainer() {
		rokDoTF = new JTextField((grafika.getRokDo() == null ? "" : grafika.getRokDo().toString()), 6);
		PlainDocument doc = (PlainDocument) rokDoTF.getDocument();
	    doc.setDocumentFilter(new MyIntFilter());
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(rokDoTF);
		return container;
	}
	
	private Container getKategorieContainer() {
		List<Kategoria> list = dbUtil.getCategories();
		kategorieJL = new JList<Kategoria>(list.toArray(new Kategoria[list.size()]));
		kategorieJL.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		katS = new HashSet<Kategoria>();
		List<Integer> indices = new ArrayList<Integer>();
    	for (int i = 0; i < kategorieJL.getModel().getSize(); i++) {
    		Kategoria kat = kategorieJL.getModel().getElementAt(i);
    		if (grafika.getKategorie().contains(kat)) {
    			indices.add(i);
    			katS.add(kat);
    		}
    	}
    	int[] intArr = new int[indices.size()];
    	for (int i = 0; i < indices.size(); i++) {
    		intArr[i] = indices.get(i);
    	}
    	kategorieJL.setSelectedIndices(intArr);
    	
    	kategorieB = new JButton(grafika.getKategorie().toString());
    	kategorieB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, kategorieJL, "Wybierz kategorie", JOptionPane.PLAIN_MESSAGE);
		    	
				katS.clear();
				int[] intArr = kategorieJL.getSelectedIndices();
		    	for (int i = 0; i < intArr.length; i++) {
		    		Kategoria kat = kategorieJL.getModel().getElementAt(intArr[i]);
		    		katS.add(kat);
		    	}
		    	kategorieB.setText(katS.toString());
			}
		});
		Container container = new Container();
		container.setLayout(new FlowLayout(FlowLayout.LEFT));
		container.add(kategorieB);
		return container;
	}
	
	private Container getImageContainer() {
		Container container = new Container();
		ImageImplement imgPanel = new ImageImplement(new ImageIcon(".\\Hermona_miniatury\\" + grafika.getTeka().getNumer() + "_" + grafika.getNumerInwentarza() + ".png").getImage());
		container.add(imgPanel);
		Dimension size = new Dimension(DBUtil.IMG_SIZE, DBUtil.IMG_SIZE);
		container.setPreferredSize(size);
		container.setMinimumSize(size);
		container.setMaximumSize(size);
		container.setSize(size);
		container.addMouseListener(new MouseListener() {		
			@Override
			public void mouseReleased(MouseEvent arg0) { }
			@Override
			public void mousePressed(MouseEvent arg0) { }
			@Override
			public void mouseExited(MouseEvent arg0) { }
			@Override
			public void mouseEntered(MouseEvent arg0) { }
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new GrafikaShowWindow(grafika.getIlustracjaPath());
			}
		});
		return container;
	}
	
	private Container getOpisContainer() {
		opisTA = new JTextArea(grafika.getOpis());
		opisTA.setLineWrap(true);
		opisTA.setWrapStyleWord(true);
		final JScrollPane scrollPane = new JScrollPane (opisTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(taMin);
		scrollPane.setMaximumSize(taMin);
		scrollPane.setPreferredSize(taMin);
		scrollPane.setSize(taMin);
		opisTA.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				scrollPane.setMinimumSize(taMin);
				scrollPane.setMaximumSize(taMin);
				scrollPane.setPreferredSize(taMin);
				scrollPane.setSize(taMin);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				scrollPane.setMinimumSize(taMax);
				scrollPane.setMaximumSize(taMax);
				scrollPane.setPreferredSize(taMax);
				scrollPane.setSize(taMax);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
		});
		return scrollPane;
	}
	
	private Container getInskrypcjeContainer() {
		inskrypcjeTA = new JTextArea(grafika.getInskrypcje());
		inskrypcjeTA.setLineWrap(true);
		inskrypcjeTA.setWrapStyleWord(true);
		final JScrollPane scrollPane = new JScrollPane (inskrypcjeTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(taMin);
		scrollPane.setMaximumSize(taMin);
		scrollPane.setPreferredSize(taMin);
		scrollPane.setSize(taMin);
		inskrypcjeTA.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				scrollPane.setMinimumSize(taMin);
				scrollPane.setMaximumSize(taMin);
				scrollPane.setPreferredSize(taMin);
				scrollPane.setSize(taMin);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				scrollPane.setMinimumSize(taMax);
				scrollPane.setMaximumSize(taMax);
				scrollPane.setPreferredSize(taMax);
				scrollPane.setSize(taMax);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
		});
		return scrollPane;
	}
	
	private Container getUwagiContainer() {
		uwagiTA = new JTextArea(grafika.getUwagi());
		uwagiTA.setLineWrap(true);
		uwagiTA.setWrapStyleWord(true);
		final JScrollPane scrollPane = new JScrollPane (uwagiTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(taMin);
		scrollPane.setMaximumSize(taMin);
		scrollPane.setPreferredSize(taMin);
		scrollPane.setSize(taMin);
		uwagiTA.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				scrollPane.setMinimumSize(taMin);
				scrollPane.setMaximumSize(taMin);
				scrollPane.setPreferredSize(taMin);
				scrollPane.setSize(taMin);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				scrollPane.setMinimumSize(taMax);
				scrollPane.setMaximumSize(taMax);
				scrollPane.setPreferredSize(taMax);
				scrollPane.setSize(taMax);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
		});
		return scrollPane;
	}
	
	private Container getBibliografiaContainer() {
		bibliografiaTA = new JTextArea(grafika.getBibliografia());
		bibliografiaTA.setLineWrap(true);
		bibliografiaTA.setWrapStyleWord(true);
		final JScrollPane scrollPane = new JScrollPane (bibliografiaTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(taMin);
		scrollPane.setMaximumSize(taMin);
		scrollPane.setPreferredSize(taMin);
		scrollPane.setSize(taMin);
		bibliografiaTA.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				scrollPane.setMinimumSize(taMin);
				scrollPane.setMaximumSize(taMin);
				scrollPane.setPreferredSize(taMin);
				scrollPane.setSize(taMin);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				scrollPane.setMinimumSize(taMax);
				scrollPane.setMaximumSize(taMax);
				scrollPane.setPreferredSize(taMax);
				scrollPane.setSize(taMax);
				GrafikaEditWindow.this.revalidate();
				GrafikaEditWindow.this.repaint();
			}
		});
		return scrollPane;
	}

	private class ButtonSaveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String ObjButtons[] = { "Tak", "Anuluj" };
			int PromptResult = JOptionPane.showOptionDialog(null,
					"Czy na pewno chcesz zapisaæ zmiany?", "",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, ObjButtons, ObjButtons[1]);
			Grafika gTmp = GrafikaEditWindow.this.grafika;
			setGrafikaAttributes(gTmp);
			if (PromptResult == JOptionPane.YES_OPTION) {
				try {
					dbUtil.saveGrafika(grafika);
					JOptionPane.showMessageDialog(null,
							"Zmiany zosta³y zapisane", "",
							JOptionPane.PLAIN_MESSAGE);
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Wyst¹pi³ b³ad podczas zapisu:\n"
											+ ex.getMessage()
											+ "\n(Prawdopodobnie duplikacja wartoœci (teka, numerInwentarza))"
											+ "\nNie wszystkie zmiany zosta³y zapisane.",
									"B£¥D", JOptionPane.WARNING_MESSAGE);
					ex.printStackTrace();
					System.out.println(gTmp);
					dbUtil.resetSession();
				}
			}
		}

		private void setGrafikaAttributes(Grafika g) {
			g.setTeka((Teka) tekaCB.getSelectedItem());
			g.setNumerInwentarza(numerInwentarzaTF.getText());
			g.setTemat(tematTF.getText());
			g.setSygnatury(sygnaturyTA.getText());
			g.setSeria(seriaTF.getText());
			g.setProjektant(projektantTF.getText());
			g.setRytownik(rytownikTF.getText());
			g.setWydawca(wydawcaTF.getText());
			g.setTechniki(techS);
			g.setWymiary(wymiaryTF.getText());
			g.setMiejsceWydania(miejsceWydaniaTF.getText());
			if (!rokOdTF.getText().isEmpty()) {
				g.setRokOd(Integer.parseInt(rokOdTF.getText()));
			}
			if (!rokDoTF.getText().isEmpty()) {
				g.setRokDo(Integer.parseInt(rokDoTF.getText()));
			}
			g.setOpis(opisTA.getText());
			g.setUwagi(uwagiTA.getText());
			g.setInskrypcje(inskrypcjeTA.getText());
			g.setBibliografia(bibliografiaTA.getText());
			g.setKategorie(katS);
		}

	}
	
	private class MyIntFilter extends DocumentFilter {
		   @Override
		   public void insertString(FilterBypass fb, int offset, String string,
		         AttributeSet attr) throws BadLocationException {

		      Document doc = fb.getDocument();
		      StringBuilder sb = new StringBuilder();
		      sb.append(doc.getText(0, doc.getLength()));
		      sb.insert(offset, string);

		      if (test(sb.toString())) {
		         super.insertString(fb, offset, string, attr);
		      } else {
		         // warn the user and don't allow the insert
		      }
		   }

		   private boolean test(String text) {
		      try {
		         Integer.parseInt(text);
		         return true;
		      } catch (NumberFormatException e) {
		         return false;
		      }
		   }

		   @Override
		   public void replace(FilterBypass fb, int offset, int length, String text,
		         AttributeSet attrs) throws BadLocationException {

		      Document doc = fb.getDocument();
		      StringBuilder sb = new StringBuilder();
		      sb.append(doc.getText(0, doc.getLength()));
		      sb.replace(offset, offset + length, text);

		      if (test(sb.toString())) {
		         super.replace(fb, offset, length, text, attrs);
		      } else {
		         // warn the user and don't allow the insert
		      }

		   }

		   @Override
		   public void remove(FilterBypass fb, int offset, int length)
		         throws BadLocationException {
		      Document doc = fb.getDocument();
		      StringBuilder sb = new StringBuilder();
		      sb.append(doc.getText(0, doc.getLength()));
		      sb.delete(offset, offset + length);

		      if (test(sb.toString())) {
		         super.remove(fb, offset, length);
		      } else {
		         // warn the user and don't allow the insert
		      }

		   }
		}

}
