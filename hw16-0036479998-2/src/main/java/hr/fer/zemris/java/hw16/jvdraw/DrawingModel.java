package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Rectangle;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

/**
 * This class implements a drawing model for the paint app.
 * 
 * @author Lovro Marković
 *
 */
public class DrawingModel implements IDrawingModel {

	/**
	 * List of all the geometrical objects.
	 */
	private List<GeometricalObject> objectList = new ArrayList<>();

	/**
	 * List of all the model listeners.
	 */
	private List<IDrawingModelListener> listenerList = new ArrayList<>();

	/**
	 * This flag is used to check if the model was modified after last save.
	 */
	private boolean modified = false;

	/**
	 * File path of the current document.
	 */
	private Path filePath = null;

	/**
	 * @return Returns minimum bounding box that encapsulates all elements.
	 */
	public Rectangle getBoundingBox() {
		
		int xMin = -1;
		int yMin = -1;
		int xMax = 0;
		int yMax = 0;
		for (GeometricalObject obj : objectList) {
			Rectangle rec = obj.getBoundingBox();
			
			int xRecMin = (int)rec.getMinX();
			if (xMin == -1 || xMin > xRecMin) {
				xMin = xRecMin;
			} 
			
			int yRecMin = (int)rec.getMinY();
			if (yMin == -1 || yMin > yRecMin) {
				yMin = yRecMin;
			}
			
			int xRecMax = (int)rec.getMaxX();
			if (xRecMax > xMax) {
				xMax = xRecMax;
			}
			
			int yRecMax = (int)rec.getMaxY();
			if (yRecMax > yMax) {
				yMax = yRecMax;
			}
		}
		
		return new Rectangle(xMin, yMin, xMax-xMin, yMax-yMin);
	}

	/**
	 * @return Return current file path.
	 */
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * Set new file path.
	 * 
	 * @param filePath
	 *            New file path.
	 */
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return True if the model has been modified before last save, false
	 *         otherwise.
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Sets a 'modified' flag variable signaling that the model is saved.
	 */
	public void saveModel() {
		modified = false;
	}

	@Override
	public int getSize() {
		return objectList.size();
	}

	@Override
	public GeometricalObject getObject(int index) {

		if (index < 0 || index >= objectList.size()) {
			throw new IllegalArgumentException("Given index: " + index
					+ " should be between 0 and " + (objectList.size() - 1));
		}

		return objectList.get(index);
	}

	@Override
	public void add(GeometricalObject object) {

		if (object == null) {
			throw new IllegalArgumentException("Given reference is null.");
		}

		objectList.add(object);

		// Notify all the listeners
		for (IDrawingModelListener listener : listenerList) {
			listener.objectsAdded(this, objectList.size() - 1,
					objectList.size() - 1);
		}

		modified = true;
	}

	@Override
	public void addDrawingModelListener(IDrawingModelListener l) {

		if (l == null) {
			throw new IllegalArgumentException("Given reference is null.");
		}

		listenerList.add(l);
	}

	@Override
	public void removeDrawingModelListener(IDrawingModelListener l) {

		if (l == null) {
			return;
		}

		listenerList.remove(l);
	}

	/**
	 * This method is used for notifying listener when certain objects changed.
	 * 
	 * @param index
	 *            Index of the changed object.
	 */
	public void objectsChange(int index) {

		// Do nothing...

		// Notify listeners
		for (IDrawingModelListener listener : listenerList) {
			listener.objectsChanged(this, index, index);
		}

		modified = true;
	}

	/**
	 * This method is used for notifying listeners when certain objects are
	 * deleted.
	 * 
	 * @param index0
	 *            Starting index of the deleted objects.
	 * @param index1
	 *            Ending index of the deleted objects.
	 * 
	 */
	public void objectsDelete(int index0, int index1) {

		// Delete objects
		for (int i = index1; i >= index0; i--) {
			objectList.remove(i);
		}

		// Notify listeners.
		for (IDrawingModelListener listener : listenerList) {
			listener.objectsRemoved(this, index0, index1);
		}

		modified = true;
	}

	/**
	 * Remove all objects.
	 */
	public void removeAll() {
		int index1 = objectList.size() - 1;
		objectList.clear();

		for (IDrawingModelListener listener : listenerList) {
			listener.objectsRemoved(this, 0, index1);
		}

		modified = true;
	}

	/**
	 * Add all elements from the given list to the internal list.
	 * 
	 * @param list
	 *            Given list of objects.
	 */
	public void addAll(List<GeometricalObject> list) {
		int index0 = objectList.size();
		objectList.addAll(list);

		for (IDrawingModelListener listener : listenerList) {
			listener.objectsAdded(this, index0, objectList.size() - 1);
		}

		modified = true;
	}
}