package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;

/**
 * This class implements a canvas for drawing various shapes.
 * 
 * @author Lovro Marković
 *
 */
public class JDrawingCanvas extends JComponent {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to the drawing model.s
	 */
	private IDrawingModel dModel;

	/**
	 * Drawing state.
	 */
	private String drawingState = null;

	/**
	 * Object in progress.
	 */
	private GeometricalObject objectProgress;

	/**
	 * Mouse click count.
	 */
	private int clickCount = 0;

	/**
	 * Center of the object - x component.
	 */
	private int xCenter = 0;

	/**
	 * Center of the object - y component.
	 */
	private int yCenter = 0;

	/**
	 * Default constructor for the drawing canvas.
	 * 
	 * @param dModel
	 *            Reference to the drawing model.
	 */
	public JDrawingCanvas(IDrawingModel dModel) {
		super();
		this.dModel = dModel;

		addListeners();
	}

	/**
	 * Set new drawing state: 'line', 'circle' or 'fcircle'.
	 * 
	 * @param state
	 *            New drawing state.
	 */
	public void setDrawingState(String state) {
		drawingState = state;
	}

	/**
	 * @return Return drawing state.
	 */
	public String getDrawingState() {
		return drawingState;
	}
	
	/**
	 * @return Return click count on the canvas.
	 */
	public int getClickCount() {
		return clickCount;
	}
	
	/**
	 * Add all listeners to the drawing canvas
	 */
	private void addListeners() {

		// Repaint component when it is resized
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (drawingState == null) {
					return;
				}

				if (clickCount == 1) {
					drawObjectInProgress(e);
					
				}
			}
		});

		// Add mouse listener for drawing
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (drawingState == null) {
					return;
				}

				if (clickCount == 0) {

					// Remember current cursor position
					xCenter = e.getX();
					yCenter = e.getY();
					clickCount++;
					return;
				}

				if (clickCount == 1) {
					clickCount = 0;

					addObjectToModel();
					objectProgress = null;
				}
			}

		});

		// Object listener
		dModel.addDrawingModelListener(new IDrawingModelListener() {

			@Override
			public void objectsRemoved(IDrawingModel source, int index0,
					int index1) {

				repaint();
			}

			@Override
			public void objectsChanged(IDrawingModel source, int index0,
					int index1) {

				repaint();
			}

			@Override
			public void objectsAdded(IDrawingModel source, int index0,
					int index1) {

				repaint();
			}
		});
	}

	/**
	 * Draw updated geometrical object based on the current mouse position.
	 * 
	 * @param e
	 *            Mouse event.
	 */
	private void drawObjectInProgress(MouseEvent e) {

		// Check which drawing state is currently active and draw
		// that object
		if (drawingState.equals("line")) {

			if (objectProgress == null) {
				objectProgress = new Line(xCenter, yCenter, e.getX(), e.getY(),
						getForeground());
			} else {
				((Line) objectProgress).setxEnd(e.getX());
				((Line) objectProgress).setyEnd(e.getY());
			}

		} else if (drawingState.equals("circle")) {

			int radius = (int) Math.sqrt(Math.pow(xCenter - e.getX(), 2)
					+ Math.pow(yCenter - e.getY(), 2));

			if (objectProgress == null) {
				objectProgress = new Circle(xCenter, yCenter, radius,
						getForeground());
			} else {
				((Circle) objectProgress).setRadius(radius);
			}

		} else if (drawingState.equals("fcircle")) {

			int radius = (int) Math.sqrt(Math.pow(xCenter - e.getX(), 2)
					+ Math.pow(yCenter - e.getY(), 2));

			if (objectProgress == null) {
				objectProgress = new FilledCircle(xCenter, yCenter, radius,
						getForeground(), getBackground());
			} else {
				((FilledCircle) objectProgress).setRadius(radius);
			}
		}

		repaint();
	}

	/**
	 * Add an appropriate copy of the current object to the model.
	 */
	private void addObjectToModel() {

		if (objectProgress instanceof Line) {
			Line.decreaseCounter();
			dModel.add(new Line((Line) objectProgress));

		} else if (objectProgress instanceof Circle) {
			Circle.decreaseCounter();
			dModel.add(new Circle((Circle) objectProgress));

		} else {
			FilledCircle.decreaseCounter();
			dModel.add(new FilledCircle((FilledCircle) objectProgress));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// Paint background white
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0, len = dModel.getSize(); i < len; i++) {
			dModel.getObject(i).paintComponent(g2d);
		}

		if (objectProgress != null) {
			objectProgress.paintComponent(g2d);
		}
	}
}
