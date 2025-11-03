import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import program.FileIoQueue;
import ui.windows.MainWindow;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
		}
		MainWindow mainWindow = new MainWindow();
		mainWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				FileIoQueue.terminateWriterThread();
				System.exit(0);
			}
		});
		mainWindow.setVisible(true);
	}
}
