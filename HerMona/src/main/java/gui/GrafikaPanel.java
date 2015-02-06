package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.hibernate.HibernateException;

import util.DBUtil;
import model.Grafika;

public class GrafikaPanel extends JPanel {
	private static final long serialVersionUID = 4484009714046170060L;
	public static final String[] columnNames = {"Teka", "Numer inwentarza", "Tytul", "Seria",
		"Technika", "Wymiary", "Projekatant", "Rytownik", "Wydawca", "Sygnatury", "Rok od",
		"Rok do", "Miejsce wydania", "Opis", "Inskrypcje", "Bibliografia", "Uwagi", "Kategorie", "Œcie¿ka ilustracji"};
	
	private InteractiveTableModel tableModel;
	private JButton bSave = new JButton("Zapisz zmiany");
	private JButton bFilter = new JButton("Filtruj");
	private JTable table;
	DBUtil dbUtil = new DBUtil();

	public GrafikaPanel() {
		
		Vector<Grafika> grafVec= new Vector<Grafika>(dbUtil.getGrafikas(""));
		tableModel = new InteractiveTableModel(columnNames, grafVec);
        tableModel.addTableModelListener(new GrafikaPanel.InteractiveTableModelListener());
        table = new JTable();
        table.setModel(tableModel);
        table.setSurrendersFocusOnKeystroke(true);
        table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("tableCellEditor")) {
					if (table.isEditing()) {
						bSave.setEnabled(false);
					} else {
						bSave.setEnabled(true);
					}
				}
			}
		});
        
        JTableHeader header = table.getTableHeader();
        tableModel.setRendererColumnSizes(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        bSave.addActionListener(new ButtonSaveListener());
        bFilter.addActionListener(new ButtonFilterListener());
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(header);
        add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        add(Box.createRigidArea(new Dimension(0,5)));
        add(bSave);
        add(bFilter);
	}

	private class InteractiveTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 4466896298699455046L;
		private static final int TEKA_INDEX = 0;
		private static final int NUMER_INWENTARZA_INDEX = 1;
		private static final int TYTUL_INDEX = 2;
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
			fireTableRowsDeleted(0, before);
			this.dataVector.addAll(dataVector);
			fireTableRowsInserted(0, this.dataVector.size() - 1);
		}

		public String getColumnName(int column) {
			return columnNames[column];
		}
		
		public void setRendererColumnSizes(JTable table) {
			for (int i = 0; i < InteractiveTableModel.HIDDEN_INDEX; i++) {
				TableColumn col = table.getColumnModel().getColumn(i);
				col.setMinWidth(COL_MIN_WIDTH);
			}
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

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Class getColumnClass(int column) {
			switch (column) {
			case TYTUL_INDEX:
			case TEKA_INDEX:
			case NUMER_INWENTARZA_INDEX:
			case SERIA_INDEX:
			case TECHNIKA_INDEX:
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
			case TYTUL_INDEX:
				return record.getTytul();
			case TEKA_INDEX:
				return record.getTeka().getNumer().toString();
			case NUMER_INWENTARZA_INDEX:
				return record.getNumerInwentarza();
			case SERIA_INDEX:
				return record.getSeria();
			case TECHNIKA_INDEX:
				if (record.getTechnika() != null) {
					return record.getTechnika().getNazwa();
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
				return "To be implemented";
			case ILUSTRACJA_PATH_INDEX:
				return record.getIlustracjaPath();
			default:
				return new Object();
			}
		}
		
		public Grafika getObjectAt(int row) {
			return dataVector.get(row);
		}

		public void setValueAt(Object value, int row, int column) {
			Grafika record = dataVector.get(row);
			switch (column) {
			case TYTUL_INDEX:
				record.setTytul((String)value);
				break;
			case TEKA_INDEX:
				//TODO
				break;
			case NUMER_INWENTARZA_INDEX:
				record.setNumerInwentarza((String)value);
				break;
			case SERIA_INDEX:
				record.setSeria((String)value);
				break;
			case TECHNIKA_INDEX:
				//TODO
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
				//TODO
				break;
			case ILUSTRACJA_PATH_INDEX:
				record.setIlustracjaPath((String)value);
				break;
			default:
				System.out.println("invalid index");
			}
			fireTableCellUpdated(row, column);
		}

		public int getRowCount() {
			return dataVector.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}
	}
	
	class InteractiveRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -6959006446611341448L;
		protected int interactiveColumn;

        public InteractiveRenderer(int interactiveColumn) {
            this.interactiveColumn = interactiveColumn;
        }

        public Component getTableCellRendererComponent(JTable table,
           Object value, boolean isSelected, boolean hasFocus, int row,
           int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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
			for (int i = 0; i < table.getRowCount(); i++) {
				Grafika grafika = tableModel.getObjectAt(i);
				dbUtil.saveGrafika(grafika);
			}
			JOptionPane.showMessageDialog(null, "Zmiany zosta³y zapisane", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
    
    private class ButtonFilterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
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
				tableModel.changeData(grafVec);
            }
		}
    	
    }

}
