package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import model.Grafika;

import org.hibernate.HibernateException;

import util.DBUtil;
import util.InteractiveTableModel;

public class GrafikaPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 4484009714046170060L;
	public static final int STATIC_COLUMNS_NUMBER = 3;
	public static Integer editLock = 0;
	public static final String[] columnNames = {"teka", "numer inwentarza", "temat", "seria",
		"technika", "wymiary", "projektant", "rytownik", "wydawca", "inny autor", "sygnatury", "rok od",
		"rok do", "miejsce wydania", "opis", "inskrypcje", "katalogi", "bibliografia", "uwagi", "kategorie", 
		"numer katalogowy", "datowanie", "ścieżka ilustracji"};
	

	private Set<Grafika> gSet = new HashSet<Grafika>();
	private Set<Grafika> gSetIlu = new HashSet<Grafika>();
	
	private String previousPredicate = "1=0";
	private InteractiveTableModel tableModel;
	private JButton bSave = new JButton("Zapisz zmiany");
	private JButton bFilter = new JButton("Filtruj");
	private JButton bRefresh = new JButton("Odśwież");
	private JButton bReport = new JButton("Generuj raport");
	private JTable table;
	private DBUtil dbUtil = new DBUtil();
	private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem menuItemEdit = new JMenuItem("Edytuj grafikę");

	public GrafikaPanel(MainWindow mainWindow) {
		Vector<Grafika> grafVec= new Vector<Grafika>(dbUtil.getGrafikas(previousPredicate));
		tableModel = new InteractiveTableModel(columnNames, grafVec, mainWindow, gSet, gSetIlu);
        tableModel.addTableModelListener(new GrafikaPanel.InteractiveTableModelListener());
        table = new JTable();
        table.setModel(tableModel);
        table.setSurrendersFocusOnKeystroke(true);
        table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("tableCellEditor")) {
					synchronized (editLock) {
						if (table.isEditing()) {
							bSave.setEnabled(false);
							editLock++;
						} else {
							editLock--;
							if (editLock == 0) {
								bSave.setEnabled(true);
							}
						}
					}
				}
			}
		});

        tableModel.setUpColumns(table);
        if (!tableModel.hasEmptyRow()) {
            tableModel.addEmptyRow();
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        menuItemEdit.addActionListener(this);
        popupMenu.add(menuItemEdit);
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new TableMouseListener(table));

        bSave.addActionListener(new ButtonSaveListener());
        bFilter.addActionListener(new ButtonFilterListener());
        bRefresh.addActionListener(new ButtonRefreshListener());
        bReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ReportWindow(previousPredicate);
			}
		});
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        new FixedColumnTable(STATIC_COLUMNS_NUMBER, scrollPane, bSave);
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0,5)));
        Container bContainer = new Container();
        bContainer.setMaximumSize(new Dimension(800, 50));
        FlowLayout bLayout = new FlowLayout();
        bContainer.setLayout(bLayout);
        bContainer.add(bSave);
        bContainer.add(bFilter);
        bContainer.add(bRefresh);
        bContainer.add(bReport);
        add(bContainer);
	}
	
	public void findBytext() {
		String ObjButtons[] = { "Tak", "Anuluj" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Czy na pewno zmienić warunki filtrowania?\n"
				+ "Wszelkie niezapisane zmiany zostaną utracone.",
				"",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE, null, ObjButtons,
				ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			String s = (String)JOptionPane.showInputDialog(
                    table,
                    "Wpisz tekst do wyszukania:",
                    "",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
			if ((s != null)) {
				Vector<Grafika> grafVec = null;
				try {
					grafVec = new Vector<Grafika>(dbUtil.getGrafikas(dbUtil.createPredicateByText(s)));
				} catch (HibernateException e) {
					JOptionPane.showMessageDialog(null, "Wystąpił błąd składni zapytania:\n" + e.getMessage(), "BŁĄD", JOptionPane.WARNING_MESSAGE);
					return;
				}
				previousPredicate = dbUtil.createPredicateByText(s);
				tableModel.changeData(grafVec);
            }
		}
		
	}
	

	@Override
	public void actionPerformed(ActionEvent event) {
		JMenuItem menu = (JMenuItem) event.getSource();
		if (menu == menuItemEdit) {
			int selectedRow = table.getSelectedRow();
			editGraphic(tableModel.getItemAt(selectedRow), selectedRow);
		}
	}
	
	private void editGraphic(Grafika g, int selectedRow) {
		if (g.getTeka() == null || g.getNumerInwentarza() == null) {
			JOptionPane.showMessageDialog(null, "Aby edytować grafikę musi mieć ona nadaną tekę i numer inwentarza", "BŁĄD", JOptionPane.WARNING_MESSAGE);
		} else {
			new GrafikaEditWindow(g, new InteractiveTableModel(tableModel), selectedRow);
		}
	}

    public class InteractiveTableModelListener implements TableModelListener {
        public void tableChanged(TableModelEvent evt) {
            if (evt.getType() == TableModelEvent.UPDATE) {
                int column = evt.getColumn();
                int row = evt.getFirstRow();
                table.setColumnSelectionInterval(column + 1, column + 1);
                table.setRowSelectionInterval(row, row);
            }
        }
    }
	
    private class ButtonSaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String ObjButtons[] = { "Tak", "Anuluj" };
			int PromptResult = JOptionPane.showOptionDialog(null,
					"Czy na pewno chcesz zapisać zmiany?",
					"",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, ObjButtons,
					ObjButtons[1]);
			Grafika gTmp = null;
			if (PromptResult == JOptionPane.YES_OPTION) {
				try {
					for (Grafika grafika : gSet) {
						gTmp = grafika;
						if (grafika.getTeka() == null
								|| grafika.getNumerInwentarza() == null
								|| grafika.getNumerInwentarza().trim()
										.equals("")) {
							continue;
						}
						dbUtil.saveGrafika(grafika);
					}
					JOptionPane.showMessageDialog(null,
							"Zmiany zostały zapisane", "",
							JOptionPane.PLAIN_MESSAGE);
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(
									null,
									"Wystąpił błąd podczas zapisu:\n"
											+ ex.getMessage()
											+ "\n(Prawdopodobnie duplikacja wartości (teka, numerInwentarza))"
											+ "\nNie wszystkie zmiany zostały zapisane.",
									"BŁĄD", JOptionPane.WARNING_MESSAGE);
					ex.printStackTrace();
					System.out.println(gTmp);
					dbUtil.resetSession();
				}
			}
			for (Grafika g : gSetIlu) {
				dbUtil.makeMiniature(g);
			}
			gSet.clear();
			gSetIlu.clear();
		}
		
	}
    
    private class ButtonFilterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String ObjButtons[] = { "Tak", "Anuluj" };
			int PromptResult = JOptionPane.showOptionDialog(null,
					"Czy na pewno zmienić warunki filtrowania?\n"
					+ "Wszelkie niezapisane zmiany zostaną utracone.",
					"",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, ObjButtons,
					ObjButtons[1]);
			if (PromptResult == JOptionPane.YES_OPTION) {
				String s = (String)JOptionPane.showInputDialog(
	                    table,
	                    "Wpisz warunek filtrowania:",
	                    "Wybierz filtr",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    "");
				if ((s != null)) {
					Vector<Grafika> grafVec = null;
					try {
						grafVec = new Vector<Grafika>(dbUtil.getGrafikas(s));
					} catch (HibernateException e) {
						JOptionPane.showMessageDialog(null, "Wystąpił błąd składni zapytania:\n" + e.getMessage(), "BŁĄD", JOptionPane.WARNING_MESSAGE);
						return;
					}
					previousPredicate = s;
					tableModel.changeData(grafVec);
	            }
			}
			
		}
    	
    }
    
	private class ButtonRefreshListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String ObjButtons[] = { "Tak", "Anuluj" };
			int PromptResult = JOptionPane.showOptionDialog(null,
					"Czy na pewno chcesz odświeżyć widok?\n"
							+ "Wszelkie niezapisane zmiany zostaną utracone.",
					"", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, ObjButtons,
					ObjButtons[1]);
			if (PromptResult == JOptionPane.YES_OPTION) {
				Vector<Grafika> grafVec = null;
				try {
					grafVec = new Vector<Grafika>(
							dbUtil.getGrafikas(previousPredicate));
				} catch (HibernateException e) {
					JOptionPane.showMessageDialog(
							null,
							"Wystąpił bład składni zapytania:\n"
									+ e.getMessage(), "BŁĄD",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				tableModel.changeData(grafVec);
			}
		}
	}
    	
	private class TableMouseListener extends MouseAdapter {
	    
	    private JTable table;
	     
	    public TableMouseListener(JTable table) {
	        this.table = table;
	    }
	     
	    @Override
	    public void mousePressed(MouseEvent event) {
	        // selects the row at which point the mouse is clicked
	        Point point = event.getPoint();
	        int currentRow = table.rowAtPoint(point);
	        table.setRowSelectionInterval(currentRow, currentRow);
	    }
	}
    

}
