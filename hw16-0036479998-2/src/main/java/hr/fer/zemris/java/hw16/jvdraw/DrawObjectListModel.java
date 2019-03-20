package hr.fer.zemris.java.hw16.jvdraw;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

/**
 * List model used for tracking which objects are currently on the screen.
 * 
 * @author Lovro Marković
 *
 */
public class DrawObjectListModel extends AbstractListModel<GeometricalObject> {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to the drawing model.
	 */
	private IDrawingModel dModel;

	/**
	 * Default constructor for the list model.
	 * 
	 * @param dModel
	 *            Reference to the drawing model.
	 */
	public DrawObjectListModel(IDrawingModel dModel) {
		this.dModel = dModel;

		dModel.addDrawingModelListener(new IDrawingModelListener() {

			@Override
			public void objectsRemoved(IDrawingModel source, int index0,
					int index1) {
				fireIntervalRemoved(source, index0, index1);
			}

			@Override
			public void objectsChanged(IDrawingModel source, int index0,
					int index1) {
				fireContentsChanged(source, index0, index1);
			}

			@Override
			public void objectsAdded(IDrawingModel source, int index0,
					int index1) {
				fireIntervalAdded(source, index0, index1);
			}
		});
	}

	@Override
	public int getSize() {
		return dModel.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return dModel.getObject(index);
	}

}
