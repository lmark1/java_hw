package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

/**
 * This interfines defines a color listener.
 * 
 * @author Lovro Marković
 *
 */
public interface IColorChangeListener {

	/**
	 * This method signals which color is newly selected.
	 * 
	 * @param source
	 *            Source of the new color.
	 * @param oldColor
	 *            Old color.
	 * @param newColor
	 *            New color.
	 */
	public void newColorSelected(IColorProvider source, Color oldColor,
			Color newColor);
}
