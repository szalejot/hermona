package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.hibernate.HibernateException;

import util.DBUtil;
import model.Grafika;
import model.Kategoria;
import model.Technika;
import model.Teka;

public class GrafikaPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 4484009714046170060L;
	private static final int STATIC_COLUMNS_NUMBER = 2;
	public static Integer editLock = 0;
	public static final String[] columnNames = {"teka", "numer inwentarza", "temat", "seria",
		"technika", "wymiary", "projekatant", "rytownik", "wydawca", "sygnatury", "rok od",
		"rok do", "miejsce wydania", "opis", "inskrypcje", "bibliografia", "uwagi", "kategorie", "œcie¿ka ilustracji"};

	private Set<Grafika> gSet = new HashSet<Grafika>();
	private Set<Grafika> gSetIlu = new HashSet<Grafika>();
	
	private String previousPredicate = "1=0";
	private InteractiveTableModel tableModel;
	private JButton bSave = new JButton("Zapisz zmiany");
	private JButton bFilter = new JButton("Filtruj");
	private JButton bRefresh = new JButton("Odœwie¿");
	private JButton bReport = new JButton("Generuj raport");
	private JTable table;
	private DBUtil dbUtil = new DBUtil();
	private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem menuItemEdit = new JMenuItem("Edytuj grafikê");

	public GrafikaPanel() {
		
		Vector<Grafika> grafVec= new Vector<Grafika>(dbUtil.getGrafikas(previousPredicate));
		tableModel = new InteractiveTableModel(columnNames, grafVec);
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
        bContainer.setMaximumSize(new Dimension(400, 50));
        FlowLayout bLayout = new FlowLayout();
        bContainer.setLayout(bLayout);
        bContainer.add(bSave);
        bContainer.add(bFilter);
        bContainer.add(bRefresh);
        bContainer.add(bReport);
        add(bContainer);
	}
	

	@Override
	public void actionPerformed(ActionEvent event) {
		JMenuItem menu = (JMenuItem) event.getSource();
		if (menu == menuItemEdit) {
			int selectedRow = table.getSelectedRow();
			editGraphic(tableModel.getItemAt(selectedRow));
		}
	}
	
	private void editGraphic(Grafika g) {
		if (g.getTeka() == null || g.getNumerInwentarza() == null) {
			JOptionPane.showMessageDialog(null, "Aby edytowaæ grafikê musi mieæ ona nadan¹ tekê i numer inwentarza", "B£¥D", JOptionPane.WARNING_MESSAGE);
		} else {
			new GrafikaEditWindow(g);
		}
	}

	private class InteractiveTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 4466896298699455046L;
		private static final int TEKA_INDEX = 0;
		private static final int NUMER_INWENTARZA_INDEX = 1;
		private static final int TEMAT_INDEX = 2;
		private static final int SERIA_INDEX = 3;
		private static final int TECHNIKA_INDEX = 4;
		private static final int WYMIARY_INDEX = 5;
		private static final int PROJEKTANT_INDEX = 6;
		private static final int RYTOWNIK_INDEX = 7;
		private static final int WYDAWCA_INDEX = 8;
		private static final int SYGNATURY_INDEX = 9;
		private static final int ROK_OD_INDEX = 10;
		private static final int ROK_DO_INDEX = 11;
		private static final int MIEJSCE_WYDANIA_INDEX = 12;
		private static final int OPIS_INDEX = 13;
		private static final int INSKRYPCJE_INDEX = 14;
		private static final int BIBLIOGRAFIA_INDEX = 15;
		private static final int UWAGI_INDEX = 16;
		private static final int KATEGORIE_INDEX = 17;
		private static final int ILUSTRACJA_PATH_INDEX = 18;
		private static final int HIDDEN_INDEX = 19;
		private static final int COL_MIN_WIDTH = 200;
		private static final int COL_MIN_WIDTH_IDX = 100;

		private String[] columnNames;
		private Vector<Grafika> dataVector;
		
		public InteractiveTableModel(String[] columnNames, Vector<Grafika> dataVector) {
			List<String> list = new LinkedList<String>(Arrays.asList(columnNames));
			list.add("");
			this.columnNames = list.toArray(new String[list.size()]);
			this.dataVector = dataVector;
		}
		
		public void changeData(Vector<Grafika> dataVector) {
			int before = getRowCount() - 1;
			this.dataVector.clear();
			gSet.clear();
			gSetIlu.clear();
			fireTableRowsDeleted(0, before);
			this.dataVector.addAll(dataVector);
			addEmptyRow();
			fireTableRowsInserted(0, this.dataVector.size() - 1);
		}
		
		public void setUpColumns(JTable table) {
			setUpTekaColumn(table);
			setUpTechnikaColumn(table);
			setUpKategorieColumn(table);
			setUpSygnaturyColumn(table);
			setUpInskrypcjeColumn(table);
			setUpIlustracjaPathColumn(table);
			setUpBibliografiaColumn(table);
			setUpUwagiColumn(table);
			
	        setRendererColumnSizes(table);
		}
		
		private void setUpTekaColumn(JTable table) {
			List<Teka> list = dbUtil.getTekas();
			JComboBox<Teka> comboBox = new JComboBox<Teka>(list.toArray(new Teka[list.size()]));
			table.getColumnModel().getColumn(TEKA_INDEX).setCellEditor(new DefaultCellEditor(comboBox));
		}
		
		private void setUpTechnikaColumn(JTable table) {
			List<Technika> list = dbUtil.getTechniques();
			JComboBox<Technika> comboBox = new JComboBox<Technika>(list.toArray(new Technika[list.size()]));
			table.getColumnModel().getColumn(TECHNIKA_INDEX).setCellEditor(new DefaultCellEditor(comboBox));
		}
		
		private void setUpKategorieColumn(JTable table) {
			table.getColumnModel().getColumn(KATEGORIE_INDEX).setCellEditor(new KategorieCellEditor());
		}
		
		private void setUpSygnaturyColumn(JTable table) {
			table.getColumnModel().getColumn(SYGNATURY_INDEX).setCellEditor(new TextAreaCellEditor("Sygnatury"));
		}
		
		private void setUpInskrypcjeColumn(JTable table) {
			table.getColumnModel().getColumn(INSKRYPCJE_INDEX).setCellEditor(new TextAreaCellEditor("Inskrypcje"));
		}
		
		private void setUpIlustracjaPathColumn(JTable table) {
			table.getColumnModel().getColumn(ILUSTRACJA_PATH_INDEX).setCellEditor(new IlustracjaPathCellEditor());
		}
		
		private void setUpBibliografiaColumn(JTable table) {
			table.getColumnModel().getColumn(BIBLIOGRAFIA_INDEX).setCellEditor(new TextAreaCellEditor("Bibliografia"));
		}
		
		private void setUpUwagiColumn(JTable table) {
			table.getColumnModel().getColumn(UWAGI_INDEX).setCellEditor(new TextAreaCellEditor("Uwagi"));
		}

		public String getColumnName(int column) {
			return columnNames[column];
		}
		
		private void setRendererColumnSizes(JTable table) {
			TableColumn col;
			for (int i = 0; i < InteractiveTableModel.HIDDEN_INDEX; i++) {
				col = table.getColumnModel().getColumn(i);
				col.setMinWidth(COL_MIN_WIDTH);
			}
			col = table.getColumnModel().getColumn(TEKA_INDEX);
			col.setMinWidth(COL_MIN_WIDTH_IDX);
			col.setPreferredWidth(COL_MIN_WIDTH_IDX);
	        //col.setMaxWidth(COL_MIN_WIDTH_IDX);
			col = table.getColumnModel().getColumn(NUMER_INWENTARZA_INDEX);
			col.setMinWidth(COL_MIN_WIDTH_IDX);
			col.setPreferredWidth(COL_MIN_WIDTH_IDX);
	        //col.setMaxWidth(COL_MIN_WIDTH_IDX/2);
			TableColumn hidden = table.getColumnModel().getColumn(InteractiveTableModel.HIDDEN_INDEX);
	        hidden.setMinWidth(0);
	        hidden.setPreferredWidth(0);
	        hidden.setMaxWidth(0);
	        hidden.setCellRenderer(new InteractiveRenderer(InteractiveTableModel.HIDDEN_INDEX));
		}

		public boolean isCellEditable(int row, int column) {
			if (column == HIDDEN_INDEX)
				return false;
			else
				return true;
		}
		
		public Grafika getItemAt(int pos) {
			return dataVector.elementAt(pos);
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Class getColumnClass(int column) {
			switch (column) {
			case TEKA_INDEX:
				return Teka.class;
			case TECHNIKA_INDEX:
				return Technika.class;
			case TEMAT_INDEX:
			case NUMER_INWENTARZA_INDEX:
			case SERIA_INDEX:
			case WYMIARY_INDEX:
			case PROJEKTANT_INDEX:
			case RYTOWNIK_INDEX:
			case WYDAWCA_INDEX:
			case SYGNATURY_INDEX:
			case MIEJSCE_WYDANIA_INDEX:
			case OPIS_INDEX:
			case INSKRYPCJE_INDEX:
			case BIBLIOGRAFIA_INDEX:
			case UWAGI_INDEX:
			case KATEGORIE_INDEX:
			case ILUSTRACJA_PATH_INDEX:
				return String.class;
			case ROK_OD_INDEX:
			case ROK_DO_INDEX:
				return Integer.class;
			default:
				return Object.class;
			}
		}

		public Object getValueAt(int row, int column) {
			Grafika record = dataVector.get(row);
			switch (column) {
			case TEMAT_INDEX:
				return record.getTemat();
			case TEKA_INDEX:
				return record.getTeka();
			case NUMER_INWENTARZA_INDEX:
				return record.getNumerInwentarza();
			case SERIA_INDEX:
				return record.getSeria();
			case TECHNIKA_INDEX:
				if (record.getTechnika() != null) {
					return record.getTechnika();
				} else return null;
			case WYMIARY_INDEX:
				return record.getWymiary();
			case PROJEKTANT_INDEX:
				return record.getProjektant();
			case RYTOWNIK_INDEX:
				return record.getRytownik();
			case WYDAWCA_INDEX:
				return record.getWydawca();
			case SYGNATURY_INDEX:
				return record.getSygnatury();
			case ROK_OD_INDEX:
				return record.getRokOd();
			case ROK_DO_INDEX:
				return record.getRokDo();
			case MIEJSCE_WYDANIA_INDEX:
				return record.getMiejsceWydania();
			case OPIS_INDEX:
				return record.getOpis();
			case INSKRYPCJE_INDEX:
				return record.getInskrypcje();
			case BIBLIOGRAFIA_INDEX:
				return record.getBibliografia();
			case UWAGI_INDEX:
				return record.getUwagi();
			case KATEGORIE_INDEX:
				return record.getKategorie();
			case ILUSTRACJA_PATH_INDEX:
				return record.getIlustracjaPath();
			default:
				return new Object();
			}
		}

		@SuppressWarnings("unchecked")
		public void setValueAt(Object value, int row, int column) {
			Grafika record = dataVector.get(row);
			gSet.add(record);
			switch (column) {
			case TEMAT_INDEX:
				record.setTemat((String)value);
				break;
			case TEKA_INDEX:
				record.setTeka((Teka)value);
				break;
			case NUMER_INWENTARZA_INDEX:
				record.setNumerInwentarza((String)value);
				break;
			case SERIA_INDEX:
				record.setSeria((String)value);
				break;
			case TECHNIKA_INDEX:
				record.setTechnika((Technika)value);
				break;
			case WYMIARY_INDEX:
				record.setWymiary((String)value);
				break;
			case PROJEKTANT_INDEX:
				record.setProjektant((String)value);
				break;
			case RYTOWNIK_INDEX:
				record.setRytownik((String)value);
				break;
			case WYDAWCA_INDEX:
				record.setWydawca((String)value);
				break;
			case SYGNATURY_INDEX:
				record.setSygnatury((String)value);
				break;
			case ROK_OD_INDEX:
				record.setRokOd((Integer)value);
				break;
			case ROK_DO_INDEX:
				record.setRokDo((Integer)value);
				break;
			case MIEJSCE_WYDANIA_INDEX:
				record.setMiejsceWydania((String)value);
				break;
			case OPIS_INDEX:
				record.setOpis((String)value);
				break;
			case INSKRYPCJE_INDEX:
				record.setInskrypcje((String)value);
				break;
			case BIBLIOGRAFIA_INDEX:
				record.setBibliografia((String)value);
				break;
			case UWAGI_INDEX:
				record.setUwagi((String)value);
				break;
			case KATEGORIE_INDEX:
				record.setKategorie((Set<Kategoria>)value);
				break;
			case ILUSTRACJA_PATH_INDEX:
				record.setIlustracjaPath((String)value);
				gSetIlu.add(record);
				break;
			default:
				System.out.println("invalid index");
			}
			int col = column - STATIC_COLUMNS_NUMBER;
			if (col < 0) col = 0;
			fireTableCellUpdated(row, col);
			if(!hasEmptyRow()) {
				addEmptyRow();
			}
		}

		public int getRowCount() {
			return dataVector.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}
		
		public boolean hasEmptyRow() {
	         if (dataVector.size() == 0) return false;
	         Grafika grafika = (Grafika)dataVector.get(dataVector.size() - 1);
	         if (grafika.getTeka() == null ||
	        		 grafika.getNumerInwentarza() == null ||
	        		 grafika.getNumerInwentarza().trim().equals("")) {
	            return true;
	         } else {
	        	 return false;
	         }
	     }

	     public void addEmptyRow() {
	         dataVector.add(new Grafika());
	         fireTableRowsInserted(
	            dataVector.size() - 1,
	            dataVector.size() - 1);
	     }
	}
	
	/*public void highlightLastRow(int row) {
        int lastrow = tableModel.getRowCount();
        if (row == lastrow - 1) {
            table.setRowSelectionInterval(lastrow - 1, lastrow - 1);
        } else {
            table.setRowSelectionInterval(row + 1, row + 1);
        }
        table.setColumnSelectionInterval(0, 0);
    }*/
	
	class InteractiveRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -6959006446611341448L;
		protected int interactiveColumn;

		public InteractiveRenderer(int interactiveColumn) {
			this.interactiveColumn = interactiveColumn;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			return c;
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
					"Czy na pewno chcesz zapisaæ zmiany?",
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
					"Czy na pewno zmieniæ warunki filtrowania?\n"
					+ "Wszelkie niezapisane zmiany zostan¹ utracone.",
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
						JOptionPane.showMessageDialog(null, "Wyst¹pi³ b³ad sk³adni zapytania:\n" + e.getMessage(), "B£¥D", JOptionPane.WARNING_MESSAGE);
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
					"Czy na pewno chcesz odœwie¿yæ widok?\n"
							+ "Wszelkie niezapisane zmiany zostan¹ utracone.",
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
							"Wyst¹pi³ b³ad sk³adni zapytania:\n"
									+ e.getMessage(), "B£¥D",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				tableModel.changeData(grafVec);
			}
		}
	}
    	
    
    private class KategorieCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = -1463545753241064548L;
		private static final int CLICK_COUNT_TO_START = 2;
		private JLabel label;
		private Set<Kategoria> kategorie;
		JList<Kategoria> jList;
		
		public KategorieCellEditor() {
			super(new JTextField());
			setClickCountToStart(CLICK_COUNT_TO_START);
			
			// Using a JButton as the editor component
	        label = new JLabel();
	        label.setBackground(Color.white);
	        label.setFont(label.getFont().deriveFont(Font.PLAIN));
	        label.setBorder(null);
	        
	        List<Kategoria> list = dbUtil.getCategories();
			jList = new JList<Kategoria>(list.toArray(new Kategoria[list.size()]));
			jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		
		@Override
	    public Object getCellEditorValue() {
	        return kategorie;
	    }

	    @SuppressWarnings("unchecked")
		@Override
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    	jList.clearSelection();
	    	kategorie = (Set<Kategoria>) value;
	    	List<Integer> indices = new ArrayList<Integer>();
	    	for (int i = 0; i < jList.getModel().getSize(); i++) {
	    		Kategoria kat = jList.getModel().getElementAt(i);
	    		if (kategorie.contains(kat)) {
	    			indices.add(i);
	    		}
	    	}
	    	int[] intArr = new int[indices.size()];
	    	for (int i = 0; i < indices.size(); i++) {
	    		intArr[i] = indices.get(i);
	    	}
	    	jList.setSelectedIndices(intArr);
	    	
	    	JOptionPane.showMessageDialog(null, jList);
	    	
	    	kategorie.clear();
	    	intArr = jList.getSelectedIndices();
	    	for (int i = 0; i < intArr.length; i++) {
	    		Kategoria kat = jList.getModel().getElementAt(intArr[i]);
	    		kategorie.add(kat);
	    	}
	    	
	    	label.setText(value.toString());
	        return label;
	    }
    }
    
    private class IlustracjaPathCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = -1463545753241064548L;
		private static final int CLICK_COUNT_TO_START = 2;
		private JLabel label;
		
		public IlustracjaPathCellEditor() {
			super(new JTextField());
			setClickCountToStart(CLICK_COUNT_TO_START);
			
			// Using a JButton as the editor component
	        label = new JLabel();
	        label.setBackground(Color.white);
	        label.setFont(label.getFont().deriveFont(Font.PLAIN));
	        label.setBorder(null);
	        
		}
		
		@Override
	    public Object getCellEditorValue() {
	        return label.getText();
	    }
		@Override
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    	JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG", "png");
			FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG", "JPG");
			fc.setFileFilter(jpgFilter);
			fc.addChoosableFileFilter(pngFilter);
			String sRet;
			if (value == null) {
				sRet = "";
			} else {
				sRet = value.toString();
				if (sRet.lastIndexOf('\\') >= 0) {
					File dir = new File(sRet.substring(0, sRet.lastIndexOf('\\')));
					fc.setCurrentDirectory(dir);
				}
			}
			int returnVal = fc.showOpenDialog(null);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            sRet = file.getAbsolutePath();
	        }
	    	
	    	label.setText(sRet);
	        return label;
	    }
    }
    
    private class TextAreaCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = -1463545753241064548L;
		private static final int CLICK_COUNT_TO_START = 2;
		private JTextArea jArea = new JTextArea(25, 60);
		private String title;
		
		public TextAreaCellEditor(String title) {
			super(new JTextField());
			this.title = title;
			setClickCountToStart(CLICK_COUNT_TO_START);
		}
		
		@Override
	    public Object getCellEditorValue() {
	        return jArea.getText();
	    }

		@Override
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    	jArea.setText((String)value);
	    	
	    	JOptionPane.showMessageDialog(null, jArea, title, JOptionPane.PLAIN_MESSAGE);
	        return jArea;
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
