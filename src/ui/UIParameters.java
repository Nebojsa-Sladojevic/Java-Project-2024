package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public abstract class UIParameters {

	public static final Dimension MAP_SIZE = new Dimension(20, 20);
	public static final Rectangle INNER_AREA = new Rectangle(5, 5, 10, 10);

	abstract public class Colors {

		public static final Color PRIMARY_BACKGROUND = new Color(0x171717);
		public static final Color SECONDARY_BACKGROUND = new Color(0x202020);
		public static final Color TERTIARY_BACKGROUND = new Color(0x181818);

		public static final Color BUTTON_BACKGROUND_DEFAULT = new Color(0x505050);
		public static final Color BUTTON_BACKGROUND_TABLE = new Color(0x404060);
		public static final Color BUTTON_BACKGROUND_VEHICLES = new Color(0x404060);
		public static final Color BUTTON_BACKGROUND_USERS_RENTS = new Color(0x604040);
		public static final Color BUTTON_BACKGROUND_FINANCIAL = new Color(0x406040);

		private static final Color BORDER_TABLE = Color.GRAY;

		public static final Color FIELD_BACKGROUND_INNER_ZONE = new Color(0x202030);
		public static final Color FIELD_BACKGROUND_OUTER_ZONE = new Color(0x302020);
		public static final Color FIELD_BACKGROUND_1_VEHICLE = new Color(0xBBBBFF);
		public static final Color FIELD_BACKGROUND_2_VEHICLES = new Color(0xDDDDFF);
		public static final Color FIELD_BACKGROUND_3_VEHICLES = new Color(0xFFFFFF);

		public static final Color TEXT = Color.WHITE;
		public static final Color TEXT_TABLE = Color.WHITE;
		public static final Color TEXT_FIELD = Color.BLACK;

	}

	public abstract class Fonts {

		private static final int HEIGHT_LABEL = 13;
		private static final int HEIGHT_BUTTON = 13;
		private static final int HEIGHT_TABLE = 12;

		public static final Font LABEL_FONT = new Font(null, Font.PLAIN, HEIGHT_LABEL);
		public static final Font BUTTON_FONT = new Font(null, Font.PLAIN, HEIGHT_BUTTON);
		public static final Font TABLE_FONT = new Font(null, Font.PLAIN, HEIGHT_TABLE);

	}

	public abstract class Dimensions {

		public static final int BUTTON_HEIGHT = 24;
		public static final int BUTTON_WIDTH_DEFAULT = 100;
		public static final int BUTTON_WIDTH_FINANCIAL = 115;
		public static final int BUTTON_WIDTH_START_SIMULATION = 130;
		public static final int TABLE_WINDOW_MIN_HEIGHT = 400;
		public static final int TABLE_COLUMN_MARGIN = 25;

		public static final int GAP_BETWEEN_BUTTONS = 5;
		public static final int GAP_BETWEEN_PANELS = 10;

		public static final Dimension buttonCustomWidth(int width) {
			return new Dimension(width, BUTTON_HEIGHT);
		}

		public static final Dimension MAP_FIELD_DIMENSION = new Dimension(80, 25);
		public static final Dimension VEHICLE_STATUS_PANEL_DIMENSION = new Dimension(60, 25);
		public static final Dimension TABLE_HEADER_DIMENSION = new Dimension(25, 25);

	}

	public abstract class Borders {

		public static final EmptyBorder BORDER_AROUND_TABLE = new EmptyBorder(10, 12, 10, 12);
		public static final EmptyBorder BORDER_AROUND_GRID = new EmptyBorder(10, 12, 10, 12);
		public static final EmptyBorder BORDER_AROUND_BUTTONS = new EmptyBorder(5, 7, 5, 7);
		public static final EmptyBorder BORDER_AROUND_BUTTONS_TOP = new EmptyBorder(5, 7, 0, 7);
		public static final EmptyBorder BORDER_AROUND_BUTTONS_BOTTOM = new EmptyBorder(0, 7, 5, 7);
		public static final EmptyBorder BORDER_AROUND_BUTTONS_DIALOG = new EmptyBorder(2, 10, 2, 10);
		public static final EmptyBorder BORDER_AROUND_TEXT = new EmptyBorder(10, 15, 10, 20);
		public static final EmptyBorder BORDER_AROUND_TEXT_TABLE = new EmptyBorder(0, 7, 0, 7);
		public static final Border BORDER_LINE_AROUND_TABLE = BorderFactory.createLineBorder(Colors.BORDER_TABLE, 1);

	}

}
