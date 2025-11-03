package ui.panels;

import java.awt.GridLayout;

import javax.swing.JPanel;

import ui.UIParameters;

public class MapGrid extends JPanel {

	private static final long serialVersionUID = 1L;

	private final MapField mapGrid[][];

	private boolean isInner(int row, int column) {
		return UIParameters.INNER_AREA.contains(row, column);
	}

	public MapGrid() {
		setBackground(UIParameters.Colors.PRIMARY_BACKGROUND);
		setBorder(UIParameters.Borders.BORDER_AROUND_GRID);
		final int ROWS = UIParameters.MAP_SIZE.height;
		final int COLUMNS = UIParameters.MAP_SIZE.width;
		int gridGap = UIParameters.Dimensions.GAP_BETWEEN_BUTTONS;
		setLayout(new GridLayout(ROWS, COLUMNS, gridGap, gridGap));
		mapGrid = new MapField[ROWS][COLUMNS];
		for (int row = 0; row < ROWS; row++)
			for (int column = 0; column < COLUMNS; column++)
				this.add(mapGrid[row][column] = new MapField(isInner(row, column)));
	}

	public MapField[][] getMapGrid() {
		return mapGrid;
	}

}
