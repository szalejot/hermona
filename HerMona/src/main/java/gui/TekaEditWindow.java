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
import javax.swing.JFrame;
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

import util.DBUtil;
import model.Teka;

public class TekaEditWindow extends JFrame {
	private static final long serialVersionUID = 4484009714046170060L;
	public static final String[] columnNames = {"Numer teki", "Tytuł teki", "Rok"};
	private InteractiveTableModel tableModel;

	private JPanel p = new JPanel();
	private JButton b = new JButton("Zapisz zmiany");
	private JTable table;
	private DBUtil dbUtil = new DBUtil();

	public TekaEditWindow() {
		super("Edytuj tekę");

		setSize(700, 300);
		setResizable(true);
		setLocationRelativeTo(null);
		
		Vector<Teka> tekaVec= new Vector<Teka>(dbUtil.getTekas());
		tableModel = new InteractiveTableModel(columnNames, tekaVec);
        tableModel.addTableModelListener(new TekaEditWindow.InteractiveTableModelListener());
        table = new JTable();
        table.setModel(tableModel);
        table.setSurrendersFocusOnKeystroke(true);
        table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("tableCellEditor")) {
					if (table.isEditing()) {
						b.setEnabled(false);
					} else {
						b.setEnabled(true);
					}
				}
			}
		});
        
        JTableHeader header = table.getTableHeader();
        tableModel.setRendererColumnSizes(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        b.addActionListener(new ButtonListener());
        
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.add(header);
        p.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        p.add(Box.createRigidArea(new Dimension(0,5)));
        p.add(b);
		add(p);
		setVisible(true);
	}

	private class InteractiveTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 4466896298699455046L;
		private static final int NUMER_INDEX = 0;
		private static final int TYTUL_INDEX = 1;
		private static final int ROK_INDEX = 2;
		private static final int HIDDEN_INDEX = 3;
		private static final int COL_MIN_WIDTH = 200;

		private String[] columnNames;
		private Vector<Teka> dataVector;
		
		public InteractiveTableModel(String[] columnNames, Vector<Teka> dataVector) {
			List<String> list = new LinkedList<String>(Arrays.asList(columnNames));
			list.add("");
			this.columnNames = list.toArray(new String[list.size()]);
			this.dataVector = dataVector;
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
				return String.class;
			case NUMER_INDEX:
			case ROK_INDEX:
				return Integer.class;
			default:
				return Object.class;
			}
		}

		public Object getValueAt(int row, int column) {
			Teka record = dataVector.get(row);
			switch (column) {
			case TYTUL_INDEX:
				return record.getTytul();
			case NUMER_INDEX:
				return record.getNumer();
			case ROK_INDEX:
				return record.getRok();
			default:
				return new Object();
			}
		}
		
		public Teka getObjectAt(int row) {
			return dataVector.get(row);
		}

		public void setValueAt(Object value, int row, int column) {
			Teka record = dataVector.get(row);
			switch (column) {
			case TYTUL_INDEX:
				record.setTytul((String)value);
				break;
			case NUMER_INDEX:
				record.setNumer((Integer)value);
				break;
			case ROK_INDEX:
				record.setRok((Integer)value);
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
	
    private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < table.getRowCount(); i++) {
				Teka teka = tableModel.getObjectAt(i);
				dbUtil.saveTeka(teka);
			}
			JOptionPane.showMessageDialog(null, "Zmiany zostały zapisane", "", JOptionPane.PLAIN_MESSAGE);
		}
		
	}

}
