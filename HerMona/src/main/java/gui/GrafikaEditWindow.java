package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import model.Grafika;
import model.Technika;
import model.Teka;
import util.DBUtil;

public class GrafikaEditWindow extends JFrame {
	private static final long serialVersionUID = 1113700118119705921L;
	private static final Dimension taMin = new Dimension(400, 40);
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
	private JComboBox<Technika> technikaCB;
	private JTextField wymiaryTF;
	private JTextField miejsceWydaniaTF;
	private JTextField rokOdTF;
	private JTextField rokDoTF;
	private JTextArea sygnaturyTA;
	private JTextArea opisTA;
	private JTextArea uwagiTA;
	private JTextArea inskrypcjeTA;
	private JTextArea bibliografiaTA;

	public GrafikaEditWindow(Grafika g) {
		super("Edytuj grafikê");

		grafika = g;
		setSize(900, 700);
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

		JPanel leftContainer = new JPanel();
		BoxLayout bLayout = new BoxLayout(leftContainer, BoxLayout.PAGE_AXIS);
		leftContainer.setLayout(bLayout);
		leftContainer.setMaximumSize(new Dimension(500, 480));
		leftContainer.setSize(new Dimension(500, 480));
		leftContainer.add(getTekaContainer());
		leftContainer.add(getNumerInewntarzaContainer());
		leftContainer.add(getTematContainer());
		leftContainer.add(getSygnaturyContainer());
		leftContainer.add(getSeriaContainer());
		leftContainer.add(getProjektantContainer());
		leftContainer.add(getRytownikContainer());
		leftContainer.add(getWydawcaContainer());
		leftContainer.add(getTechnikaContainer());
		leftContainer.add(getWymiaryContainer());
		leftContainer.add(getMiejsceWydaniaContainer());
		leftContainer.add(getRokOdContainer());
		leftContainer.add(getRokDoContainer());
		
		p.setLayout(new BorderLayout());
		p.add(leftContainer, BorderLayout.LINE_START);
		p.add(getImageContainer(), BorderLayout.LINE_END);

		Container bottomContainer = new Container();
		bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.PAGE_AXIS));
		bottomContainer.add(getOpisContainer());
		bottomContainer.add(getUwagiContainer());
		bottomContainer.add(getInskrypcjeContainer());
		bottomContainer.add(getBibliografiaContainer());
		bottomContainer.add(bSave);
		p.add(bottomContainer, BorderLayout.PAGE_END);

		JScrollPane thePane = new JScrollPane(p);
		add(thePane);
		setVisible(true);
	}
	
	private Container getTekaContainer() {
		List<Teka> list = dbUtil.getTekas();
		tekaCB = new JComboBox<Teka>(list.toArray(new Teka[list.size()]));
		tekaCB.setSelectedItem(grafika.getTeka());
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("teka"));
		container.add(tekaCB);
		return container;
	}
	
	private Container getNumerInewntarzaContainer() {
		numerInwentarzaTF = new JTextField(grafika.getNumerInwentarza(), 12);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("numer inwentarza"));
		container.add(numerInwentarzaTF);
		return container;
	}
	
	private Container getTematContainer() {
		tematTF = new JTextField(grafika.getTemat(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("temat"));
		container.add(tematTF);
		return container;
	}
	
	private Container getSeriaContainer() {
		seriaTF = new JTextField(grafika.getSeria(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("seria"));
		container.add(seriaTF);
		return container;
	}
	
	private Container getSygnaturyContainer() {
		sygnaturyTA = new JTextArea(grafika.getSygnatury(), 4, 30);
		Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(new JLabel("sygnatury"));
		container.add(new JScrollPane (sygnaturyTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		return container;
	}
	
	private Container getProjektantContainer() {
		projektantTF = new JTextField(grafika.getProjektant(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("projektant"));
		container.add(projektantTF);
		return container;
	}
	
	private Container getRytownikContainer() {
		rytownikTF = new JTextField(grafika.getRytownik(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("projektant"));
		container.add(rytownikTF);
		return container;
	}
	
	private Container getWydawcaContainer() {
		wydawcaTF = new JTextField(grafika.getWydawca(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("wydawca"));
		container.add(wydawcaTF);
		return container;
	}
	
	private Container getTechnikaContainer() {
		List<Technika> list = dbUtil.getTechniques();
		technikaCB = new JComboBox<Technika>(list.toArray(new Technika[list.size()]));
		technikaCB.setSelectedItem(grafika.getTechnika());
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("technika"));
		container.add(technikaCB);
		return container;
	}
	
	private Container getWymiaryContainer() {
		wymiaryTF = new JTextField(grafika.getWymiary(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("wymiary"));
		container.add(wymiaryTF);
		return container;
	}
	
	private Container getMiejsceWydaniaContainer() {
		miejsceWydaniaTF = new JTextField(grafika.getMiejsceWydania(), 20);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("miejsce wydania"));
		container.add(miejsceWydaniaTF);
		return container;
	}
	
	private Container getRokOdContainer() {
		rokOdTF = new JTextField((grafika.getRokOd() == null ? "" : grafika.getRokOd().toString()), 6);
		PlainDocument doc = (PlainDocument) rokOdTF.getDocument();
	    doc.setDocumentFilter(new MyIntFilter());
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("rok od"));
		container.add(rokOdTF);
		return container;
	}
	
	private Container getRokDoContainer() {
		rokDoTF = new JTextField((grafika.getRokDo() == null ? "" : grafika.getRokDo().toString()), 6);
		PlainDocument doc = (PlainDocument) rokDoTF.getDocument();
	    doc.setDocumentFilter(new MyIntFilter());
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(new JLabel("rok do"));
		container.add(rokDoTF);
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
		return container;
	}
	
	private Container getOpisContainer() {
		opisTA = new JTextArea(grafika.getOpis());
		final Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(new JLabel("opis"));
		final JScrollPane scrollPane = new JScrollPane (opisTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(taMin);
		ComponentResizer cr = new ComponentResizer();
		cr.setSnapSize(new Dimension(10, 10));
		cr.registerComponent(scrollPane);
		container.add(scrollPane);
		return container;
	}
	
	private Container getInskrypcjeContainer() {
		inskrypcjeTA = new JTextArea(grafika.getInskrypcje());
		final Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(new JLabel("inskrypcje"));
		final JScrollPane scrollPane = new JScrollPane (inskrypcjeTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(taMin);
		ComponentResizer cr = new ComponentResizer();
		cr.setSnapSize(new Dimension(10, 10));
		cr.registerComponent(scrollPane);
		container.add(scrollPane);
		return container;
	}
	
	private Container getUwagiContainer() {
		uwagiTA = new JTextArea(grafika.getUwagi());
		final Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(new JLabel("uwagi"));
		final JScrollPane scrollPane = new JScrollPane (uwagiTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(taMin);
		ComponentResizer cr = new ComponentResizer();
		cr.setSnapSize(new Dimension(10, 10));
		cr.registerComponent(scrollPane);
		container.add(scrollPane);
		return container;
	}
	
	private Container getBibliografiaContainer() {
		bibliografiaTA = new JTextArea(grafika.getBibliografia());
		final Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(new JLabel("bibliografia"));
		final JScrollPane scrollPane = new JScrollPane (bibliografiaTA, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(taMin);
		ComponentResizer cr = new ComponentResizer();
		cr.setSnapSize(new Dimension(10, 10));
		cr.registerComponent(scrollPane);
		container.add(scrollPane);
		return container;
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
			g.setTechnika((Technika) technikaCB.getSelectedItem());
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

	private class ImageImplement extends JPanel {
		private static final long serialVersionUID = 4115161325532051904L;
		private Image img;

		public ImageImplement(Image img) {
			this.img = img;
			Dimension size = new Dimension(img.getWidth(null),
					img.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}

		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, null);
		}
	}

}
