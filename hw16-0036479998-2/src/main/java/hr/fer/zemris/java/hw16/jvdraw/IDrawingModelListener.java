package hr.fer.zemris.java.hw16.jvdraw;

/**
 * This class represents a listener used for drawing model objects.
 * 
 * @author Lovro Marković
 *
 */
public interface IDrawingModelListener {
	
	/**
	 * This method signals if any objects have been added.
	 * 
	 * @param source Source of the drawing model.
	 * @param index0 Start index of the added objects.
	 * @param index1 End index of the added objects.
	 */
	public void objectsAdded(IDrawingModel source, int index0, int index1);
	
	/**
	 * This method signals if any objects have been removed.
	 * 
	 * @param source Source of the drawing model.
	 * @param index0 Start index of the removed objects.
	 * @param index1 End index of the removed objects.
	 */
	public void objectsRemoved(IDrawingModel source, int index0, int index1);
	
	/**
	 * This method signals if any objects have been changed.
	 * 
	 * @param source Source of the drawing model.
	 * @param index0 Start index of the changed objects.
	 * @param index1 End index of the changed objects.
	 */
	public void objectsChanged(IDrawingModel source, int index0, int index1);
}
