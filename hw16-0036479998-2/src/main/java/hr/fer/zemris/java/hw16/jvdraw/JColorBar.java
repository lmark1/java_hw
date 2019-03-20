package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Status bar showing which colors (background and foreground) are currently
 * selected.
 * 
 * @author Lovro Marković
 *
 */
public class JColorBar extends JPanel {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Foreground color provider.
	 */
	private IColorProvider fgColorProvider;

	/**
	 * Background color provider.
	 */
	private IColorProvider bgColorProvider;

	/**
	 * Label which holds foreground and background color information.
	 */
	private JLabel colorLabel;

	/**
	 * Default constructor for the status bar.
	 * 
	 * @param fgColorProvider
	 *            Reference to the foreground color provider.
	 * @param bgColorProvider
	 *            Reference to the background color provider.
	 */
	public JColorBar(IColorProvider fgColorProvider,
			IColorProvider bgColorProvider) {

		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;

		initPanel();
		addColorListeners();
	}

	/**
	 * Add color listeners.
	 */
	private void addColorListeners() {
		
		fgColorProvider.addColorChangeListener(new IColorChangeListener() {
			
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor,
					Color newColor) {
				Color bgColor = bgColorProvider.getCurrentColor();
				colorLabel.setText(generateColorText(bgColor, newColor));
			}
		});		
		
		bgColorProvider.addColorChangeListener(new IColorChangeListener() {
			
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor,
					Color newColor) {
				Color fgColor = fgColorProvider.getCurrentColor();
				colorLabel.setText(generateColorText(newColor, fgColor));
			}
		});
	}

	/**
	 * Generate text written in the label based on the current colors.
	 * 
	 * @param bgColor Background color.
	 * @param fgColor Foreground color.
	 * @return Formatted string.
	 */
	private String generateColorText(Color bgColor, Color fgColor) {
		return String.format(
				"Background color: (%d, %d, %d), foreground color: (%d, %d, %d)",
				bgColor.getRed(), bgColor.getBlue(), bgColor.getGreen(),
				fgColor.getRed(), fgColor.getBlue(), fgColor.getGreen());
	}

	/**
	 * Initialized the panel.
	 */
	private void initPanel() {
		Color bgColor = bgColorProvider.getCurrentColor();
		Color fgColor = fgColorProvider.getCurrentColor();

		colorLabel = new JLabel(generateColorText(bgColor, fgColor));
		setLayout(new BorderLayout());
		add(colorLabel, BorderLayout.LINE_START);
	}

}
