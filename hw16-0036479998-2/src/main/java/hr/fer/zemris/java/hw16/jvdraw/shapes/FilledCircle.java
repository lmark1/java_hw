package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This class represents a filled circle which extends its parent Geometrical
 * object class.
 * 
 * @author Lovro Marković
 *
 */
public class FilledCircle extends GeometricalObject {

	/**
	 * Circle radius.
	 */
	private int radius;

	/**
	 * Circle color.
	 */
	private Color areaColor;

	/**
	 * Number of circles.
	 */
	private static int circleCount = 0;

	/**
	 * Object name.
	 */
	private String name;

	/**
	 * Default constructor for circle objects.
	 * 
	 * @param xCenter
	 *            Center of the circle - x component.
	 * @param yCenter
	 *            Center of the circle - y component.
	 * @param radius
	 *            Circle radius.
	 * @param lineColor
	 *            Line color of the circle.
	 * @param areaColor
	 *            Area color of the circle.
	 */
	public FilledCircle(int xCenter, int yCenter, int radius, Color lineColor,
			Color areaColor) {
		super(xCenter, yCenter, lineColor);
		this.radius = radius;
		this.areaColor = areaColor;
		circleCount++;
		name = "Filled Circle " + circleCount;
	}

	/**
	 * Constructor which is initialized by another circle.
	 * 
	 * @param c
	 *            Circle reference.
	 */
	public FilledCircle(FilledCircle c) {
		this(c.getxCenter(), c.getyCenter(), c.getRadius(), c.getLineColor(),
				c.getAreaColor());
	}

	/**
	 * @return Return circle radius.
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Reset filled circle counter.
	 */
	public static void resetCounter() {
		circleCount = 0;
	}

	/**
	 * Decrease circle counter.
	 */
	public static void decreaseCounter() {
		circleCount--;
	}

	/**
	 * Set a new circle radius.
	 * 
	 * @param radius
	 *            New radius.
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * @return Returns the circle color.
	 */
	public Color getColor() {
		return areaColor;
	}

	/**
	 * @return Returns current area color.
	 */
	public Color getAreaColor() {
		return areaColor;
	}

	/**
	 * Sets a new area color.
	 * 
	 * @param areaColor
	 *            New area color.
	 */
	public void setAreaColor(Color areaColor) {
		this.areaColor = areaColor;
	}

	@Override
	public String asText() {
		return String.format("%s %d %d %d %d %d %d %d %d %d%n", getType(),
				getxCenter(), getyCenter(), radius, getLineColor().getRed(),
				getLineColor().getGreen(), getLineColor().getBlue(),
				areaColor.getRed(), areaColor.getGreen(), areaColor.getBlue());
	}

	@Override
	public String getType() {
		return "FCIRCLE";
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void paintComponent(Graphics2D g2d) {
		g2d.setPaint(getAreaColor());
		g2d.fillOval(getxCenter() - getRadius(), getyCenter() - getRadius(),
				2 * getRadius(), 2 * getRadius());
		g2d.setPaint(getLineColor());
		g2d.drawOval(getxCenter() - getRadius(), getyCenter() - getRadius(),
				2 * getRadius(), 2 * getRadius());
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(getxCenter() - radius, getyCenter() - radius,
				radius * 2, radius * 2);
	}
}
