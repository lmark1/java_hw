package hr.fer.zemris.java.hw16.jvdraw.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This class represents a circle which extends its parent class Geometrical
 * object.
 * 
 * @author Lovro Marković
 *
 */
public class Circle extends GeometricalObject {

	/**
	 * Radius of the circle.
	 */
	private int radius;

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
	 *            Circle line color.
	 */
	public Circle(int xCenter, int yCenter, int radius, Color lineColor) {
		super(xCenter, yCenter, lineColor);
		this.radius = radius;
		circleCount++;
		name = "Circle " + circleCount;
	}

	/**
	 * Constructor which is initialized by another circle.
	 * 
	 * @param c
	 *            Circle reference.
	 */
	public Circle(Circle c) {
		this(c.getxCenter(), c.getyCenter(), c.getRadius(), c.getLineColor());
	}

	/**
	 * Reset circle counters.
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
	 * @return Return circle radius.
	 */
	public int getRadius() {
		return radius;
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

	@Override
	public String asText() {
		return String.format("%s %d %d %d %d %d %d%n", getType(), getxCenter(),
				getyCenter(), radius, getLineColor().getRed(),
				getLineColor().getGreen(), getLineColor().getBlue());
	}

	@Override
	public String getType() {
		return "CIRCLE";
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void paintComponent(Graphics2D g2d) {
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
