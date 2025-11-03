package ui.windows;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.UIParameters;
import ui.buttons.ProgramButton;

public class ProgramDialog {

	private Object lock = new Object();
	private JDialog dialogWindow = null;

	public void launchProgramDialog(String title, String[] dialogText) {

		synchronized (lock) {
			if (dialogWindow != null)
				dialogWindow.dispose();
			dialogWindow = new JDialog();
			dialogWindow.setTitle(title);
			dialogWindow.setLayout(new BorderLayout());
			dialogWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			JPanel bottomPanel = new JPanel();
			bottomPanel.setBackground(UIParameters.Colors.PRIMARY_BACKGROUND);
			bottomPanel.setBorder(UIParameters.Borders.BORDER_AROUND_BUTTONS_DIALOG);
			bottomPanel.add(new ProgramButton("Close", e -> dialogWindow.dispose()));

			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.setBackground(UIParameters.Colors.SECONDARY_BACKGROUND);
			mainPanel.setOpaque(true);
			mainPanel.setBorder(UIParameters.Borders.BORDER_AROUND_TEXT);
			for (String line : dialogText) {
				JLabel label = new JLabel(line);
				label.setFont(UIParameters.Fonts.LABEL_FONT);
				label.setForeground(UIParameters.Colors.TEXT);
				mainPanel.add(label);
			}

			dialogWindow.add(mainPanel, BorderLayout.CENTER);
			dialogWindow.add(bottomPanel, BorderLayout.SOUTH);

			dialogWindow.setAlwaysOnTop(true);
			dialogWindow.setLocationRelativeTo(null);
			dialogWindow.pack();
			dialogWindow.setResizable(false);
			dialogWindow.setVisible(true);
			dialogWindow.setVisible(true);
		}

	}

}
