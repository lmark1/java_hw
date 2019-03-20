package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

/**
 * Layout for the calculator GUI.
 * 
 * @author Lovro Marković
 *
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Maximum number of rows in the layout
	 */
	private static final int MAX_ROWS = 5;

	/**
	 * Maximum number of columns in the layout.
	 */
	private static final int MAX_COLUMNS = 7;

	/**
	 * Width of the component on the position (1,1)
	 */
	private static final int BIG_WIDTH = 5;

	/**
	 * Gap between rows and columns.
	 */
	private int gap;

	/**
	 * Mapping all components to their specified positions.
	 */
	Map<Component, RCPosition> gridMap = new HashMap<>();

	/**
	 * Constructor for the layout.
	 * 
	 * @param gap
	 *            Spacing between components.
	 */
	public CalcLayout(int gap) {
		this.gap = gap;
	}

	/**
	 * Default constructor for the layout. Initializes gap to 0.
	 */
	public CalcLayout() {
		this(0);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		String[] splitString = name.split(",");

		if (splitString.length != 2) {
			throw new IllegalArgumentException(
					"Invalid position string " + name);
		}

		int row = 0;
		int column = 0;

		try {
			row = Integer.parseInt(splitString[0]);
			column = Integer.parseInt(splitString[1]);

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Invalid position string " + name);
		}

		addLayoutComponent(comp, new RCPosition(row, column));
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {

		if (constraints instanceof String) {
			addLayoutComponent((String) constraints, comp);
			return;
		}

		if (!(constraints instanceof RCPosition)) {
			throw new IllegalArgumentException(
					"Given constraint object is not instance of RCPosition.");
		}

		RCPosition rConstraint = (RCPosition) constraints;
		if (rConstraint.getRow() < 0 || rConstraint.getRow() > MAX_ROWS) {
			throw new IllegalArgumentException("Row index is out of bounds.");
		}

		if (rConstraint.getColumn() < 0
				|| rConstraint.getColumn() > MAX_COLUMNS) {
			throw new IllegalArgumentException(
					"Column index is out of bounds.");
		}

		// Invalid position because of big component (1,1)
		if (rConstraint.getRow() == 1 && rConstraint.getColumn() >= 2
				&& rConstraint.getColumn() <= 5) {
			throw new IllegalArgumentException("Illegal component position.");
		}

		if (gridMap.containsValue(rConstraint)) {
			throw new IllegalArgumentException(
					"Position (" + rConstraint.getRow() + ","
							+ rConstraint.getColumn() + ") occupied.");
		}

		gridMap.put(comp, rConstraint);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		gridMap.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int nComponents = parent.getComponentCount();
		int maxWidth = 0;
		int maxHeight = 0;

		for (int i = 0; i < nComponents; i++) {
			Component c = parent.getComponent(i);
			Dimension d = c.getPreferredSize();

			if (d == null) {
				continue;
			}
			
			if (gridMap.get(c).equals(new RCPosition(1, 1))) {
				maxWidth = Math.max(maxWidth, d.width / (BIG_WIDTH) - (BIG_WIDTH-1)*gap);
			} else {
				maxWidth = Math.max(maxWidth, d.width);
			}
			maxHeight = Math.max(maxHeight, d.height);
		}

		Insets ins = parent.getInsets();

		Dimension dim = new Dimension(
				ins.left + ins.right + MAX_COLUMNS * maxWidth
						+ (MAX_COLUMNS - 1) * gap,
				ins.top + ins.bottom + MAX_ROWS * maxHeight
						+ (MAX_ROWS - 1) * gap);
		return dim;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int nComponents = parent.getComponentCount();

		int minWidth = 0;
		int minHeight = 0;
		for (int i = 0; i < nComponents; i++) {
			Component c = parent.getComponent(i);
			Dimension d = c.getMinimumSize();

			if (d == null) {
				continue;
			}
			
			minWidth = Math.max(minWidth, d.width);
			minHeight = Math.max(minHeight, d.height);
		}

		Insets ins = parent.getInsets();
		return new Dimension(
				ins.left + ins.right + MAX_COLUMNS * minWidth
						+ (MAX_COLUMNS - 1) * gap,
				ins.top + ins.bottom + MAX_ROWS * minHeight
						+ (MAX_ROWS - 1) * gap);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		int nComponents = target.getComponentCount();
		int maxWidth = 0;
		int maxHeight = 0;

		for (int i = 0; i < nComponents; i++) {
			Component c = target.getComponent(i);
			Dimension d = c.getMaximumSize();

			if (d == null) {
				continue;
			}
			
			maxWidth = Math.max(maxWidth, d.width);
			maxHeight = Math.max(maxHeight, d.height);
		}

		Insets ins = target.getInsets();
		return new Dimension(
				ins.left + ins.right + MAX_COLUMNS * maxWidth
						+ (MAX_COLUMNS - 1) * gap,
				ins.top + ins.bottom + MAX_ROWS * maxHeight
						+ (MAX_ROWS - 1) * gap);
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();

		// Get container dimensions
		int maxWidth = parent.getWidth() - (insets.left + insets.right);
		int maxHeight = parent.getHeight() - (insets.left + insets.right);

		// Get cell dimnensions
		int cellWidth = (maxWidth - (MAX_COLUMNS - 1) * gap) / MAX_COLUMNS;
		int cellHeight = (maxHeight - (MAX_ROWS - 1) * gap) / MAX_ROWS;

		for (Component comp : parent.getComponents()) {

			// If the component is not mapped
			if (!gridMap.containsKey(comp)) {
				continue;
			}

			RCPosition position = gridMap.get(comp);
			int row = position.getRow();
			int column = position.getColumn();

			// Calculate position on the grid
			int xPos = insets.left + (column - 1) * (cellWidth + gap);
			int yPos = insets.top + (row - 1) * (cellHeight + gap);

			if (row == 1 && column == 1) {
				comp.setBounds(xPos, yPos,
						cellWidth * BIG_WIDTH + gap * (BIG_WIDTH - 1),
						cellHeight);

			} else {
				comp.setBounds(xPos, yPos, cellWidth, cellHeight);
			}
		}
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

}
