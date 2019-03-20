package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;

/**
 * Main program of the application. Initilizes the paint GUI.
 * 
 * @author Lovro Marković
 *
 */
public class JVDraw extends JFrame {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Background area component.
	 */
	private JColorArea bgArea;

	/**
	 * Foreground area component;
	 */
	private JColorArea fgArea;

	/**
	 * Button group containing shape buttons.
	 */
	private ButtonGroup bGroup;

	/**
	 * Center panel along with the status text and drawing board.
	 */
	private JPanel centerPanel;

	/**
	 * Reference to the drawing model.
	 */
	private DrawingModel dModel = new DrawingModel();

	/**
	 * Drawing canvas.
	 */
	private JDrawingCanvas canvas = new JDrawingCanvas(dModel);

	/**
	 * Canvas click count.
	 */
	private int canvasClickCount = 0;

	/**
	 * File actions.
	 */
	private FileActions fa = new FileActions(dModel, this);
	
	/**
	 * Default background color.
	 */
	private static final Color BG_DEFAULT = Color.RED;

	/**
	 * Default foreground color.
	 */
	private static final Color FG_DEFAULT = Color.BLUE;

	/**
	 * Main method of the program. Executes when program starts.
	 * 
	 * @param args
	 *            Accepts no command line arguments.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}

	/**
	 * Default constructor for this class.
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(600, 500);
		setLocation(20, 20);
		setTitle("PaintApp - Untitled");

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (!dModel.isModified()) {
					dispose();
					return;
				}

				int dialogButton = JOptionPane.showConfirmDialog(JVDraw.this,
						"Do you want to save before exiting?",
						"Save ?", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (dialogButton != JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(JVDraw.this,
							"Document won't be saved.", "Info message",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
					return;
				}
				
				if (dModel.getFilePath() != null) {
					fa.writeToFile(dModel.getFilePath());
				} else {
					fa.saveDocument();
				}
				
				dModel.saveModel();
				
				dispose();
			}
		});

		initGUI();
	}

	/**
	 * This method initailizes various GUI components.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		cp.add(centerPanel, BorderLayout.CENTER);

		// Set up canvas
		canvas.setBackground(BG_DEFAULT);
		canvas.setForeground(FG_DEFAULT);

		// Add a listener to canvas which disables other buttons while drawing
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				if (canvas.getDrawingState() == null) {
					return;
				}

				canvasClickCount++;

				if (canvasClickCount == 1) {
					disableButtons();
					return;
				}

				if (canvasClickCount == 2) {
					canvasClickCount = 0;
					enableButtons();
				}
			}
		});
		centerPanel.add(canvas, BorderLayout.CENTER);

		setupScrollList();
		createMenus();
		createToolbars();
		createStatusBar();
	}

	/**
	 * Enable all the buttons.
	 */
	private void enableButtons() {
		bgArea.setEnabled(true);
		fgArea.setEnabled(true);

		Enumeration<AbstractButton> enumeration = bGroup.getElements();
		while (enumeration.hasMoreElements()) {
			enumeration.nextElement().setEnabled(true);
		}
	}

	/**
	 * Disable all the buttons.
	 */
	private void disableButtons() {
		bgArea.setEnabled(false);
		fgArea.setEnabled(false);

		Enumeration<AbstractButton> enumeration = bGroup.getElements();
		while (enumeration.hasMoreElements()) {
			enumeration.nextElement().setEnabled(false);
		}
	}

	/**
	 * Setup scrolling object list.
	 */
	private void setupScrollList() {

		DrawObjectListModel listModel = new DrawObjectListModel(dModel);
		JList<GeometricalObject> objectList = new JList<>(listModel);

		// Add double click listener for changing properties
		objectList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					int index = objectList.locationToIndex(e.getPoint());
					GeometricalObject obj = listModel.getElementAt(index);

					if (obj instanceof Line) {
						changeLine((Line) obj);

					} else if (obj instanceof Circle) {
						changeCircle((Circle) obj);

					} else {
						changeFCircle((FilledCircle) obj);
					}

					// Notify the model about the change
					dModel.objectsChange(index);
				}
			}
		});

		objectList.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				int index = objectList.getSelectedIndex();

				if (!objectList.isFocusOwner() || index == -1
						|| e.getKeyCode() != KeyEvent.VK_DELETE) {
					return;
				}

				// TODO Add multiple select delete
				dModel.objectsDelete(index, index);
			}
		});

		JScrollPane sp = new JScrollPane(objectList);
		sp.setPreferredSize(new Dimension(100, sp.getHeight()));
		centerPanel.add(sp, BorderLayout.EAST);
	}

	/**
	 * Generate a dialog box for changing filled circle properties.
	 * 
	 * @param fCircle
	 *            Filled circle reference.
	 */
	private void changeFCircle(FilledCircle fCircle) {

		JTextField xStart = new JTextField(
				String.valueOf(fCircle.getxCenter()));
		JTextField yStart = new JTextField(
				String.valueOf(fCircle.getyCenter()));
		JTextField radius = new JTextField(String.valueOf(fCircle.getRadius()));
		JColorArea lineColor = new JColorArea(fCircle.getLineColor());
		JColorArea areaColor = new JColorArea(fCircle.getAreaColor());

		JPanel optionPanel = new JPanel(new GridLayout(5, 2));
		optionPanel.add(new JLabel("Circle center - x: "));
		optionPanel.add(xStart);
		optionPanel.add(new JLabel("Circle center - y: "));
		optionPanel.add(yStart);
		optionPanel.add(new JLabel("Circle radius: "));
		optionPanel.add(radius);
		optionPanel.add(new JLabel("Line color: "));
		optionPanel.add(lineColor);
		optionPanel.add(new JLabel("Area color: "));
		optionPanel.add(areaColor);

		int result = JOptionPane.showConfirmDialog(this, optionPanel,
				"Modify " + fCircle, JOptionPane.OK_CANCEL_OPTION);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		// Try parsing coordinates
		int x1, y1, r = 0;
		try {
			x1 = Integer.valueOf(xStart.getText());
			y1 = Integer.valueOf(yStart.getText());
			r = Integer.valueOf(radius.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,
					"Error while parsing coordinates.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Check if coordinates are out of bounds
		if (x1 < 0 || y1 < 0) {
			JOptionPane.showMessageDialog(this,
					"Center coordinates out of bounds.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Set new properties.
		fCircle.setxCenter(x1);
		fCircle.setyCenter(y1);
		fCircle.setRadius(r);
		fCircle.setLineColor(lineColor.getCurrentColor());
		fCircle.setAreaColor(areaColor.getCurrentColor());
	}

	/**
	 * Generate a dialog box for changing circle properties.
	 * 
	 * @param circle
	 *            Circle reference.
	 */
	private void changeCircle(Circle circle) {

		JTextField xStart = new JTextField(String.valueOf(circle.getxCenter()));
		JTextField yStart = new JTextField(String.valueOf(circle.getyCenter()));
		JTextField radius = new JTextField(String.valueOf(circle.getRadius()));
		JColorArea colorChooser = new JColorArea(circle.getLineColor());

		JPanel optionPanel = new JPanel(new GridLayout(4, 2));
		optionPanel.add(new JLabel("Circle center - x: "));
		optionPanel.add(xStart);
		optionPanel.add(new JLabel("Circle center - y: "));
		optionPanel.add(yStart);
		optionPanel.add(new JLabel("Circle radius: "));
		optionPanel.add(radius);
		optionPanel.add(new JLabel("Line color: "));
		optionPanel.add(colorChooser);

		int result = JOptionPane.showConfirmDialog(this, optionPanel,
				"Modify " + circle, JOptionPane.OK_CANCEL_OPTION);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		// Try parsing coordinates
		int x1, y1, r = 0;
		try {
			x1 = Integer.valueOf(xStart.getText());
			y1 = Integer.valueOf(yStart.getText());
			r = Integer.valueOf(radius.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,
					"Error while parsing coordinates.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Check if coordinates are out of bounds
		if (x1 < 0 || y1 < 0) {
			JOptionPane.showMessageDialog(this,
					"Center coordinates out of bounds.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Set new properties.
		circle.setxCenter(x1);
		circle.setyCenter(y1);
		circle.setRadius(r);
		circle.setLineColor(colorChooser.getCurrentColor());
	}

	/**
	 * Generate a dialog box for changing line properties.
	 * 
	 * @param line
	 *            Line reference
	 */
	private void changeLine(Line line) {

		JTextField xStart = new JTextField(String.valueOf(line.getxCenter()));
		JTextField yStart = new JTextField(String.valueOf(line.getyCenter()));
		JTextField xEnd = new JTextField(String.valueOf(line.getxEnd()));
		JTextField yEnd = new JTextField(String.valueOf(line.getyEnd()));
		JColorArea colorChooser = new JColorArea(line.getLineColor());

		JPanel optionPanel = new JPanel(new GridLayout(5, 2));
		optionPanel.add(new JLabel("Line start - x: "));
		optionPanel.add(xStart);
		optionPanel.add(new JLabel("Line start - y: "));
		optionPanel.add(yStart);
		optionPanel.add(new JLabel("Line end - x: "));
		optionPanel.add(xEnd);
		optionPanel.add(new JLabel("Line end - y: "));
		optionPanel.add(yEnd);
		optionPanel.add(new JLabel("Line color: "));
		optionPanel.add(colorChooser);

		int result = JOptionPane.showConfirmDialog(this, optionPanel,
				"Modify " + line, JOptionPane.OK_CANCEL_OPTION);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		// Try parsing coordinates
		int x1, y1, x2, y2 = 0;
		try {
			x1 = Integer.valueOf(xStart.getText());
			y1 = Integer.valueOf(yStart.getText());
			x2 = Integer.valueOf(xEnd.getText());
			y2 = Integer.valueOf(yEnd.getText());

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,
					"Error while parsing coordinates.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Check if coordinates are out of bounds
		if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) {
			JOptionPane.showMessageDialog(this,
					"Given coordinates out of bounds.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Set new coordinates.
		line.setxCenter(x1);
		line.setyCenter(y1);
		line.setxEnd(x2);
		line.setyEnd(y2);
		line.setLineColor(colorChooser.getCurrentColor());
	}

	/**
	 * Create status bar.
	 */
	private void createStatusBar() {
		JColorBar sb = new JColorBar((IColorProvider) fgArea,
				(IColorProvider) bgArea);
		centerPanel.add(sb, BorderLayout.PAGE_END);
	}

	/**
	 * Create toolbar.
	 */
	private void createToolbars() {
		JToolBar toolbar = new JToolBar();

		// Add color components
		bgArea = new JColorArea(BG_DEFAULT);
		bgArea.addColorChangeListener(new IColorChangeListener() {

			@Override
			public void newColorSelected(IColorProvider source, Color oldColor,
					Color newColor) {
				canvas.setBackground(newColor);
			}
		});

		fgArea = new JColorArea(FG_DEFAULT);
		fgArea.addColorChangeListener(new IColorChangeListener() {

			@Override
			public void newColorSelected(IColorProvider source, Color oldColor,
					Color newColor) {
				canvas.setForeground(newColor);
			}
		});

		// Add shape button compoenents
		bGroup = new ButtonGroup();
		JToggleButton lineButton = new JToggleButton("Line");
		JToggleButton circleButton = new JToggleButton("Circle");
		JToggleButton fCircleButton = new JToggleButton("Filled Circle");

		lineButton.addActionListener((e) -> canvas.setDrawingState("line"));
		circleButton.addActionListener((e) -> canvas.setDrawingState("circle"));
		fCircleButton
				.addActionListener((e) -> canvas.setDrawingState("fcircle"));

		bGroup.add(lineButton);
		bGroup.add(circleButton);
		bGroup.add(fCircleButton);

		// Add all components to toolbar
		toolbar.add(bgArea);
		toolbar.add(fgArea);
		toolbar.add(lineButton);
		toolbar.add(circleButton);
		toolbar.add(fCircleButton);

		getContentPane().add(toolbar, BorderLayout.PAGE_START);
	}

	/**
	 * Create menus.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");

		file.add(fa.openDocument);
		file.add(fa.saveDocument);
		file.add(fa.saveAsDocument);
		file.addSeparator();
		file.add(fa.exportAction);
		file.addSeparator();
		file.add(fa.exitDocument);
		
		menuBar.add(file);
		setJMenuBar(menuBar);
	}
}
