package ui.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ui.UIParameters;

interface IcollectionToRows {
	public String[][] toRows(Collection<? extends ToTable> collection);
}

interface IobjectToRow {
	public String[] toRow(Object object);
}

public class TableWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private static void packColumn(JTable table, int vColIndex) {
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width + UIParameters.Dimensions.TABLE_COLUMN_MARGIN;
		for (int r = 0; r < table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false, r,
					vColIndex);
			width = Math.max(width, comp.getMinimumSize().width + UIParameters.Dimensions.TABLE_COLUMN_MARGIN);
		}
		col.setPreferredWidth(width);
	}

	private static class MyTableCellRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText(value.toString());
			setFont(UIParameters.Fonts.TABLE_FONT);
			setForeground(UIParameters.Colors.TEXT_TABLE);
			setBorder(UIParameters.Borders.BORDER_AROUND_TEXT_TABLE);
			return this;
		}
	}

	private static class MyTableHeaderRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText("  " + value.toString() + "  ");
			setFont(UIParameters.Fonts.TABLE_FONT);
			setForeground(UIParameters.Colors.TEXT_TABLE);
			setPreferredSize(UIParameters.Dimensions.TABLE_HEADER_DIMENSION);
			setBorder(UIParameters.Borders.BORDER_LINE_AROUND_TABLE);
			return this;
		}
	}

	private static MyTableCellRenderer tableCellRenderer = new MyTableCellRenderer();
	private static MyTableHeaderRenderer tableHeaderRenderer = new MyTableHeaderRenderer();

	public TableWindow(String title, String[] tableHeader, Collection<? extends ToTable> collection,
			Optional<Function<Object, String[]>> customRowFunction) {

		JTable table;
		synchronized (collection) {
			if (customRowFunction.isEmpty())
				table = new JTable(ToTable.toRows(collection), tableHeader);
			else
				table = new JTable(ToTable.toRows(collection, customRowFunction.get()), tableHeader);
		}
		JTableHeader header = table.getTableHeader();
		header.setBackground(UIParameters.Colors.TERTIARY_BACKGROUND);
		header.setForeground(UIParameters.Colors.TEXT_TABLE);
		header.setDefaultRenderer(tableHeaderRenderer);
		header.setOpaque(false);

		table.setBackground(UIParameters.Colors.SECONDARY_BACKGROUND);
		table.setForeground(UIParameters.Colors.TEXT_TABLE);
		table.setDefaultRenderer(Object.class, tableCellRenderer);
		table.setBorder(UIParameters.Borders.BORDER_LINE_AROUND_TABLE);
		for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++)
			packColumn(table, columnIndex);

		JPanel tableBackground = new JPanel(new BorderLayout());
		tableBackground.setBackground(UIParameters.Colors.TERTIARY_BACKGROUND);
		tableBackground.add(header, BorderLayout.NORTH);
		tableBackground.add(table, BorderLayout.CENTER);
		tableBackground.setBorder(UIParameters.Borders.BORDER_LINE_AROUND_TABLE);

		JScrollPane scrollPane = new JScrollPane(tableBackground);
		scrollPane.setBackground(UIParameters.Colors.PRIMARY_BACKGROUND);
		scrollPane.setBorder(UIParameters.Borders.BORDER_AROUND_TABLE);

		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().add(scrollPane);
		this.pack();

		Dimension windowSize = this.getSize();
		windowSize.height = Math.min(windowSize.height, UIParameters.Dimensions.TABLE_WINDOW_MIN_HEIGHT);
		this.setMinimumSize(windowSize);
		this.setVisible(true);
	}

}
