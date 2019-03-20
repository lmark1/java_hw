package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * This class models a bar chart type object.
 * 
 * @author Lovro Marković
 *
 */
public class BarChart {

	/**
	 * Values on the graph.
	 */
	private List<XYValue> values;
	
	/**
	 * X axis description.
	 */
	private String xDescription;
	
	/**
	 * Y axis description.
	 */
	private String yDescription;
	
	/**
	 * Minimal y value.
	 */
	private int yMin;
	
	/**
	 * Maximal y value.
	 */
	private int yMax;
	
	/**
	 * Y delta value.
	 */
	private int yDelta;
	
	/**
	 * Constructor for BarChart object.
	 * 
	 * @param values Values on the graph.
	 * @param xDescription X axis description.
	 * @param yDescription Y axis description.
	 * @param yMin Minimal y.
	 * @param yMax Maximal y.
	 * @param yDelta Delta y.
	 */
	public BarChart(List<XYValue> values, String xDescription,
			String yDescription, int yMin, int yMax, int yDelta) {
		this.values = values;
		this.xDescription = xDescription;
		this.yDescription = yDescription;
		this.yMin = yMin;
		this.yMax = yMax;
		this.yDelta = yDelta;
	}
	
	/**
	 * @return Returns list of graph values.
	 */
	public List<XYValue> getValues() {
		return values;
	}
	
	/**
	 * @return Returns x axis description.
	 */
	public String getxDescription() {
		return xDescription;
	}
	
	/**
	 * @return Returns spacing between y values.
	 */
	public int getyDelta() {
		return yDelta;
	}
	
	/**
	 * @return Returns y axis description.
	 */
	public String getyDescription() {
		return yDescription;
	}
	
	/**
	 * @return Returns maximum y value.
	 */
	public int getyMax() {
		return yMax;
	}
	
	/**
	 * @return Returs minimum y value.
	 */
	public int getyMin() {
		return yMin;
	}
}
