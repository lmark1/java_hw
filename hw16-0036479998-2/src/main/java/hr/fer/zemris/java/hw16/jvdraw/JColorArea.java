package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Buttons used for changing background and foreground colors in the paint
 * components.
 * 
 * @author Lovro Marković
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * Color of the component.
	 */
	private Color selectedColor;

	/**
	 * List of all listener subscribed to this component.
	 */
	private List<IColorChangeListener> listenerList = new ArrayList<>();

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Enabled state.
	 */
	private boolean enabled = true;
	
	/**
	 * Default constructor for this class.
	 * 
	 * @param selectedColor
	 *            Selected color of the component.
	 */
	public JColorArea(Color selectedColor) {
		super();
		this.selectedColor = selectedColor;

		// Initialize mouseclick listener for choosing colors
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (!enabled) {
					return;
				}
				
				Color selectedColor = JColorChooser.showDialog(getParent(),
						"Choose a new color", JColorArea.this.selectedColor);

				if (selectedColor == null) {
					return;
				}

				JColorArea.this.notifyListeners(selectedColor);
				JColorArea.this.selectedColor = selectedColor;
				repaint();
			}

		});

		initComponent();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * This method notifies all the listeners about the new color.
	 * 
	 * @param newColor
	 *            Newly selected color.
	 */
	private void notifyListeners(Color newColor) {
		for (IColorChangeListener listener : listenerList) {
			listener.newColorSelected(this, selectedColor, newColor);
		}
	}

	/**
	 * Initializes component properties.
	 */
	private void initComponent() {
		setSize(getPreferredSize());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(selectedColor);
		g.fillRect(0, 0, getWidth(), getHeight());

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(IColorChangeListener l) {

		if (l == null) {
			throw new IllegalArgumentException("Listener reference is null.");
		}

		listenerList.add(l);
	}

	@Override
	public void removeColorChangeListener(IColorChangeListener l) {

		if (l == null) {
			return;
		}

		listenerList.remove(l);
	}

}
