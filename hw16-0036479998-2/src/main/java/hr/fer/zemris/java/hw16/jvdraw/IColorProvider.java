package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

/**
 * Thic interfines defines method for providing accessing colors of a component.
 * 
 * @author Lovro Marković
 *
 */
public interface IColorProvider {

	/**
	 * @return Returns currently set color.
	 */
	public Color getCurrentColor();

	/**
	 * Adds a color change listener.
	 * 
	 * @param l
	 *            New color change listener.
	 */
	public void addColorChangeListener(IColorChangeListener l);

	/**
	 * Removes color change listener.
	 * 
	 * @param l
	 *            Listener that will be removed.
	 */
	public void removeColorChangeListener(IColorChangeListener l);
}
