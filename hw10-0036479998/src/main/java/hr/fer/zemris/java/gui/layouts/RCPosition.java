package hr.fer.zemris.java.gui.layouts;

/**
 * This class represents constraints for the costum layout manager.
 * 
 * @author Lovro Marković
 *
 */
public class RCPosition {

	/**
	 * Row position.
	 */
	private int row;

	/**
	 * Column position.
	 */
	private int column;

	/**
	 * Constructor. Initializes row and column values.
	 * 
	 * @param row
	 *            Value of row.
	 * @param column
	 *            Value of column.
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * @return Return column value.
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return Returns row value.
	 */
	public int getRow() {
		return row;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RCPosition)) {
			return false;
		}

		RCPosition rcObj = (RCPosition) obj;
		return (rcObj.row == this.row) && (rcObj.column == this.column);
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(column + row);
	}
}
