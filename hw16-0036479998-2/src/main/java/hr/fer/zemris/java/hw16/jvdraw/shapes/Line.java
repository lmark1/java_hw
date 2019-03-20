package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This class represents a line which extends a parent class Geometrical object.
 * 
 * @author Lovro Marković
 *
 */
public class Line extends GeometricalObject {

	/**
	 * End of the line - x component.
	 */
	private int xEnd;

	/**
	 * End of the line - y component.
	 */
	private int yEnd;

	/**
	 * Line counter.
	 */
	private static int lineCount = 0;

	/**
	 * Line name.
	 */
	private String name;

	/**
	 * Default constructor for the line object.
	 * 
	 * @param xStart
	 *            Start of the line - x component.
	 * @param yStart
	 *            Start of the line - y component.
	 * @param xEnd
	 *            End of the line - x component.
	 * @param yEnd
	 *            End of the line - y component.
	 * @param color
	 *            Line color.
	 */
	public Line(int xStart, int yStart, int xEnd, int yEnd, Color color) {
		super(xStart, yStart, color);

		this.xEnd = xEnd;
		this.yEnd = yEnd;
		lineCount++;
		name = "Line " + lineCount;
	}

	/**
	 * Constructor which is initialized with another line object.
	 * 
	 * @param l
	 *            Line object.
	 */
	public Line(Line l) {
		this(l.getxCenter(), l.getyCenter(), l.getxEnd(), l.getyEnd(),
				l.getLineColor());
	}

	/**
	 * Reset line counter.
	 */
	public static void resetCounter() {
		lineCount = 0;
	}

	/**
	 * Decrease line counter.
	 */
	public static void decreaseCounter() {
		lineCount--;
	}

	/**
	 * @return Returns end of the line - x component.
	 */
	public int getxEnd() {
		return xEnd;
	}

	/**
	 * Setter for new end component.
	 * 
	 * @param xEnd
	 *            New x component.
	 */
	public void setxEnd(int xEnd) {
		this.xEnd = xEnd;
	}

	/**
	 * @return Returns end of the line - y component.
	 */
	public int getyEnd() {
		return yEnd;
	}

	/**
	 * Setter for new end component.
	 * 
	 * @param yEnd
	 *            New y component.
	 */
	public void setyEnd(int yEnd) {
		this.yEnd = yEnd;
	}

	@Override
	public String asText() {
		return String.format("%s %d %d %d %d %d %d %d%n", getType(),
				getxCenter(), getyCenter(), xEnd, yEnd, getLineColor().getRed(),
				getLineColor().getGreen(), getLineColor().getBlue());
	}

	@Override
	public String getType() {
		return "LINE";
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void paintComponent(Graphics2D g2d) {
		g2d.setColor(getLineColor());
		g2d.drawLine(getxCenter(), getyCenter(), getxEnd(), getyEnd());
	}

	@Override
	public Rectangle getBoundingBox() {
		int x1 = getxCenter();
		int x2 = xEnd;

		if (x1 > x2) {
			int h = x1;
			x1 = x2;
			x2 = h;
		}

		int y1 = getyCenter();
		int y2 = yEnd;

		if (y1 > y2) {
			int h = y1;
			y1 = y2;
			y2 = h;
		}

		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}
}
