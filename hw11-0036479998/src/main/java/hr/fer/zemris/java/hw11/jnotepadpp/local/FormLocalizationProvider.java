package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * This class extends LocalizationProviderBridge.
 * 
 * @author Lovro Marković.
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor for this class. It adds a windows listener to the given frame
	 * which connects / disconnects Localization provider when the frame opens /
	 * closes.
	 * 
	 * @param lProvider
	 *            Localization provider.
	 * @param frame
	 *            Frame of the GUI.
	 */
	public FormLocalizationProvider(ILocalizationProvider lProvider,
			JFrame frame) {
		super(lProvider);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
		
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}

		});
	}

}
