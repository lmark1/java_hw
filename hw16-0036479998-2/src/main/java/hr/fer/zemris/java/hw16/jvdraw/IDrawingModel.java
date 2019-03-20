package hr.fer.zemris.java.hw16.jvdraw;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

/**
 * This interface models a drawing model containing various geometrical shapes.
 * 
 * @author Lovro Marković
 *
 */
public interface IDrawingModel {

	/**
	 * @return Return size of the model.
	 */
	public int getSize();

	/**
	 * Getter for model objects.
	 * 
	 * @param index
	 *            Index of the object.
	 * @return Return object at the i-th index.
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Add a geometrical object to the model.
	 * 
	 * @param object
	 *            New geometrical object.
	 */
	public void add(GeometricalObject object);

	/**
	 * Add new model listener.
	 * 
	 * @param l
	 *            New model listener.
	 */
	public void addDrawingModelListener(IDrawingModelListener l);

	/**
	 * Delet model listener.
	 * 
	 * @param l
	 *            Reference to the model listener.
	 */
	public void removeDrawingModelListener(IDrawingModelListener l);
}
