package hr.fer.zemris.java.gui.charts;

/**
 * This class modesl an object wiht 2 read only properties: x, y.
 * 
 * @author Lovro Marković
 *
 */
public class XYValue {
	
	/**
	 * Class variable x.
	 */
	private int x;
	
	/**
	 * Class variable y.
	 */
	private int y;
	
	/**
	 * Constructor for XYValue objects.
	 * 
	 * @param x Initial value of x.
	 * @param y Initial value of y.
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return Returns x value.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return Returns y value.
	 */
	public int getY() {
		return y;
	}
	
}
