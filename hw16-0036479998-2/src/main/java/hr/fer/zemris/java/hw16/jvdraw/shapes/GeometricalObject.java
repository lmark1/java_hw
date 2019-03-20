package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This class represents a geometrical object.
 * 
 * @author Lovro Marković
 *
 */
public class GeometricalObject {

	/**
	 * X position of the object.
	 */
	private int xCenter;

	/**
	 * Y position of the object.
	 */
	private int yCenter;

	/**
	 * Line color of the object.
	 */
	private Color lineColor;

	/**
	 * @return Return x position of the object.
	 */
	public int getxCenter() {
		return xCenter;
	}

	/**
	 * @return Return y position of the object.
	 */
	public int getyCenter() {
		return yCenter;
	}

	/**
	 * Set a new center for the object.
	 * 
	 * @param xCenter
	 *            New center - x component.
	 */
	public void setxCenter(int xCenter) {
		this.xCenter = xCenter;
	}

	/**
	 * Set a new center for the object.
	 * 
	 * @param yCenter
	 *            New center - y component.
	 */
	public void setyCenter(int yCenter) {
		this.yCenter = yCenter;
	}

	/**
	 * Default constructor for the GeometricalObject.
	 * 
	 * @param x
	 *            Center x of the object.
	 * @param y
	 *            Center y of the object.
	 * @param color
	 *            Line color of the object.
	 */
	public GeometricalObject(int x, int y, Color color) {
		xCenter = x;
		yCenter = y;
		lineColor = color;
	}

	@Override
	public String toString() {
		return new String("Unimplemented");
	}

	/**
	 * Paint the object on the component.
	 * 
	 * @param g2d
	 *            Graphical tool.
	 */
	public void paintComponent(Graphics2D g2d) {
		throw new UnsupportedOperationException(
				"This operation is not implemented.");
	}

	/**
	 * Set a new line color.
	 * 
	 * @param newColor
	 *            New color
	 * 
	 */
	public void setLineColor(Color newColor) {
		lineColor = newColor;
	}
	
	/**
	 * @return Returns current line color.
	 */
	public Color getLineColor() {
		return lineColor;
	}
	
	/**
	 * @return Returns type of the object.
	 */
	public String getType() {
		return null;
	}
	
	/**
	 * @return Return text suitable for writing in files.
	 */
	public String asText() {
		return null;
	}
	
	/**
	 * @return Return bounding box of the object.
	 */
	public Rectangle getBoundingBox() {
		return null;
	}
}
