package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;

/**
 * Component implementing a BarChart.
 * 
 * @author Lovro Marković
 *
 */
public class BarChartComponent extends JComponent {

	/**
	 * Fixed padding that is added to the left side of the y axis and to the
	 * bottom side of x axis.
	 */
	private static final int PAD = 50;

	/**
	 * Fixed label offset from the axis.
	 */
	private static final int LABEL_OFFSET = 25;

	/**
	 * Amount of space a line goes beyond the axes.
	 */
	private static final int EXTRA = 5;

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Size of the arrow on the end of axis.
	 */
	private static final int ARROW_SIZE = 5;

	/**
	 * Bar chart data.
	 */
	private BarChart chart;

	/**
	 * Constructor for bar chart components.
	 * 
	 * @param chart
	 *            Bar chart data object.
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = chart;

		setLocation(20, 30);
		setSize(100, 100);
		setBackground(Color.YELLOW);
		setForeground(Color.GRAY);
		setOpaque(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		int w = getWidth();
		int h = getHeight();

		double graphWidth = w - 2 * PAD;
		double graphHeight = h - 2 * PAD;

		// Draw both axes
		g2.setStroke(new BasicStroke(2));
		drawArrow(g, PAD, h - PAD + EXTRA, PAD, PAD - EXTRA*2);
		drawArrow(g, PAD - EXTRA, h - PAD, w - PAD + EXTRA*2, h - PAD);
		g2.setStroke(new BasicStroke());
		
		// Initialize some font data
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		float sh = lm.getAscent() + lm.getDescent();

		// Draw ordinate label - Rotated string
		String ordinateLabel = chart.getyDescription();
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		float stringWidth = (float) font.getStringBounds(ordinateLabel, frc)
				.getWidth();
		g2.setTransform(at);
		g2.drawString(ordinateLabel,
				(int) -(PAD + graphHeight / 2 + stringWidth / 2),
				PAD - LABEL_OFFSET);
		at.rotate(Math.PI / 2);
		g2.setTransform(at);

		// Draw abscissa label
		String abscissaLabel = chart.getxDescription();
		stringWidth = (float) font.getStringBounds(abscissaLabel, frc)
				.getWidth();
		float sx = (w - stringWidth) / 2;
		g2.drawString(abscissaLabel, sx,
				h - PAD + (PAD - sh) / 2 + lm.getAscent());

		int yMin = chart.getyMin();
		int yMax = chart.getyMax();
		int delta = chart.getyDelta();

		// Draw horizontal lines and numbers
		int yCount = (yMax - yMin) / delta;
		double yOffset = (double) (graphHeight) / yCount;
		
		int yNumber = yMin;
		for (int i = 0; i <= yCount; i++) {

			double x1 = PAD - EXTRA;
			double y1 = h - PAD - yOffset * i;
			double x2 = PAD + graphWidth + EXTRA;
			double y2 = y1;

			if (i != 0) {
				g2.setPaint(Color.green.darker());
				g2.draw(new Line2D.Double(x1, y1, x2, y2));
			}

			String mark = String.valueOf(yNumber);
			g2.setPaint(Color.BLACK.darker());
			stringWidth = (float) font.getStringBounds(mark, frc).getWidth();

			//TODO provjeriti zasto oznake nisu na sredini
			g2.drawString(mark, (int) Math.floor(x1 - stringWidth - EXTRA/2),
					(int) (y1 + lm.getAscent() / 2));
			yNumber += delta;
		}
		
		// Filter duplicates and sort given data
		List<XYValue> data = chart.getValues();
		Set<XYValue> dataSet = new TreeSet<>(new Comparator<XYValue>() {

			@Override
			public int compare(XYValue o1, XYValue o2) {
				int x1 = o1.getX();
				int x2 = o2.getX();
				
				if (x1 < x2) {
					return -1;
				
				} else if ( x1 > x2) {
					return 1;
				
				} else {
					return 0;
				}
			}
		});
		dataSet.addAll(data);
			
		// Draw vertical lines and numbers and rectangles
		int xCount = data.size();
		double xOffset = graphWidth / xCount;
		
		int index = 0;
		for (XYValue xyValue : dataSet) {

			double x1 = PAD + index * xOffset;
			double y1 = h - PAD + EXTRA;
			double x2 = x1;
			double y2 = PAD - EXTRA;
			
			if (index != 0) {
				g2.setPaint(Color.green.darker());
				g2.draw(new Line2D.Double(x1, y1, x2, y2));
			}
			
			g2.setPaint(Color.BLACK.darker());
			String mark = String.valueOf(xyValue.getX());
			stringWidth = (float) font.getStringBounds(mark, frc).getWidth();
			g2.drawString(mark, (int)(x1 + xOffset/2 - stringWidth/2), (int)(y1 + lm.getAscent()));
			
			g2.setPaint(Color.RED);
			
			g2.fillRect((int)(x1 + 1), (int)(h - PAD - yOffset * xyValue.getY() / delta), (int)(xOffset - 1), (int)(yOffset * xyValue.getY() / delta));
			index++;
		}
		g2.setPaint(Color.green.darker());
		g2.draw(new Line2D.Double(PAD + index * xOffset, h - PAD + EXTRA, PAD + index * xOffset, PAD - EXTRA));
	}

	/**
	 * Draws an arrow line.
	 * 
	 * @param g1
	 *            Graphics object.
	 * @param x1
	 *            Start point x.
	 * @param y1
	 *            Start point y.
	 * @param x2
	 *            End point x.
	 * @param y2
	 *            End point y.
	 */
	private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		Graphics2D g = (Graphics2D) g1.create();

		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		// Draw horizontal arrow starting in (0, 0)
		g.drawLine(0, 0, len, 0);
		g.fillPolygon(
				new int[] { len, len - ARROW_SIZE, len - ARROW_SIZE, len },
				new int[] { 0, -ARROW_SIZE, ARROW_SIZE, 0 }, 4);
	}

}
