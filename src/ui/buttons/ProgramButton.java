package ui.buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import ui.UIParameters;

public class ProgramButton extends JButton {

	private static final long serialVersionUID = 1L;

	public ProgramButton(String label, ActionListener actionListener, Optional<Color> color, Optional<Integer> width) {
		super(label);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setBorderPainted(false);
		this.setWidth(width.orElse(UIParameters.Dimensions.BUTTON_WIDTH_DEFAULT));
		this.setOpaque(true);
		this.setFont(UIParameters.Fonts.BUTTON_FONT);
		this.setForeground(UIParameters.Colors.TEXT);
		this.setBackground(color.orElse(UIParameters.Colors.BUTTON_BACKGROUND_DEFAULT));
		this.addActionListener(actionListener);
	}

	public ProgramButton(String label, ActionListener actionListener) {
		this(label, actionListener, Optional.empty(), Optional.empty());
	}

	public ProgramButton(String label, ActionListener actionListener, Color color) {
		this(label, actionListener, Optional.of(color), Optional.empty());
	}

	public ProgramButton(String label, ActionListener actionListener, int width) {
		this(label, actionListener, Optional.empty(), Optional.of(width));
	}

	public ProgramButton setWidth(int width) {
		Dimension size = new Dimension(width, UIParameters.Dimensions.BUTTON_HEIGHT);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		return this;
	}

}