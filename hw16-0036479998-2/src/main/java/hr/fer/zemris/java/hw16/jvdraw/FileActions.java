package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;

/**
 * Factory class containing various 'File' actions.
 * 
 * @author Lovro Marković
 *
 */
public class FileActions {

	/**
	 * Drawing model used for file actions.
	 */
	private DrawingModel dModel;

	/**
	 * Main frame used as a reference point for dialogs.
	 */
	private JFrame mainFrame;

	/**
	 * Default constructor for this class.
	 * 
	 * @param dModel
	 *            Reference to the DrawingModel.
	 * @param mainFrame
	 *            Reference to the main frame.
	 */
	public FileActions(DrawingModel dModel, JFrame mainFrame) {
		this.dModel = dModel;
		this.mainFrame = mainFrame;

		openDocument.putValue(Action.NAME, "Open");
		openDocument.putValue(Action.SHORT_DESCRIPTION, "Opens a .jvd file");
		openDocument.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));

		saveDocument.putValue(Action.NAME, "Save");
		saveDocument.putValue(Action.SHORT_DESCRIPTION,
				"Save current document as a .jvd file");
		saveDocument.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));

		saveAsDocument.putValue(Action.NAME, "Save As");
		saveAsDocument.putValue(Action.SHORT_DESCRIPTION,
				"Save current document as a .jvd file");

		exitDocument.putValue(Action.NAME, "Exit");
		exitDocument.putValue(Action.SHORT_DESCRIPTION, "Exit the application");
		exitDocument.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));

		exportAction.putValue(Action.NAME, "Export");
		exportAction.putValue(Action.SHORT_DESCRIPTION,
				"Export current picture in a chosen file format");
	}

	/**
	 * This action is performed when user wants to save the current document.
	 */
	public final Action saveDocument = new AbstractAction() {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (!dModel.isModified()) {
				return;
			}

			if (dModel.getFilePath() != null) {
				writeToFile(dModel.getFilePath());
			} else {
				saveDocument();
			}

		}
	};

	/**
	 * This action is used when user wants to open a document.
	 */
	public final Action openDocument = new AbstractAction() {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (dModel.isModified()) {
				int dialogButton = JOptionPane.showConfirmDialog(mainFrame,
						"Do you want to save current document?",
						"Current documen unsaved.",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (dialogButton == JOptionPane.CANCEL_OPTION) {
					return;
				}

				if (dialogButton == JOptionPane.YES_OPTION) {

					if (dModel.getFilePath() != null) {
						writeToFile(dModel.getFilePath());

					} else {
						saveDocument();
					}

					dModel.saveModel();
				}
			}

			// Open .jvd document
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open .jvd file");
			fc.setFileFilter(new FileNameExtensionFilter("JVD FILES", "jvd"));

			int result = fc.showOpenDialog(mainFrame);

			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path path = fc.getSelectedFile().toPath();
			List<String> lines = null;
			try {
				lines = Files.readAllLines(path);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(mainFrame,
						"Error occured while reading file.", "Info message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			List<GeometricalObject> newObjects = new ArrayList<>();
			Line.resetCounter();
			Circle.resetCounter();
			FilledCircle.resetCounter();
			for (String line : lines) {
				GeometricalObject obj = parse(line);
				newObjects.add(obj);
			}

			if (dModel.getSize() != 0) {
				dModel.removeAll();
			}

			dModel.addAll(newObjects);
			dModel.saveModel();
			dModel.setFilePath(path);
			mainFrame.setTitle("PaintApp - " + path.getFileName());
		}
	};

	/**
	 * Perform a save as action on the document.
	 */
	public final Action saveAsDocument = new AbstractAction() {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			saveDocument();
		}
	};

	/**
	 * Perform an exit document action.
	 */
	public final Action exitDocument = new AbstractAction() {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (!dModel.isModified()) {
				mainFrame.dispose();
				return;
			}

			int dialogButton = JOptionPane.showConfirmDialog(mainFrame,
					"Do you want to save before exiting?", "Save ?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (dialogButton != JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(mainFrame,
						"Document won't be saved.", "Info message",
						JOptionPane.INFORMATION_MESSAGE);
				mainFrame.dispose();
				return;
			}

			if (dModel.getFilePath() != null) {
				writeToFile(dModel.getFilePath());
			} else {
				saveDocument();
			}

			dModel.saveModel();

			mainFrame.dispose();
		}
	};

	/**
	 * This action exports the current document as a JPG, PNG or GIF.
	 */
	public final Action exportAction = new AbstractAction() {

		/**
		 * Serial version ID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (dModel.getSize() == 0) {
				JOptionPane.showMessageDialog(mainFrame,
						"No objects to export.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save .jvd file");
			fc.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
			fc.setFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));
			fc.setFileFilter(new FileNameExtensionFilter("*.gif", "gif"));
			fc.setAcceptAllFileFilterUsed(false);
			int result = fc.showSaveDialog(mainFrame);

			if (result != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(mainFrame, "Exporting canceled.",
						"Info message", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			String extension = fc.getFileFilter().getDescription().substring(1);
			// Add extension if necessary.
			Path path = fc.getSelectedFile().toPath();
			if (!path.toString().toLowerCase().endsWith(extension)) {
				path = Paths.get(path.toString() + extension);
			}

			// Write image as it is position on the screen
			Rectangle rec = dModel.getBoundingBox();
			int bWidth = (int) (rec.getWidth() + Math.abs(rec.getMinX()));
			int bHeight = (int) (rec.getHeight() + Math.abs(rec.getMinY()));
			BufferedImage imageHelp = new BufferedImage(bWidth, bHeight,
					BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = imageHelp.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, bWidth, bHeight);
			int xOffset = rec.getMinX() < 0 ? (int) Math.abs(rec.getMinX()) : 0;
			int yOffset = rec.getMinY() < 0 ? (int) Math.abs(rec.getMinY()) : 0;
			for (int i = 0, len = dModel.getSize(); i < len; i++) {
				GeometricalObject obj = dModel.getObject(i);
				obj.setxCenter(obj.getxCenter() + xOffset);
				obj.setyCenter(obj.getyCenter() + yOffset);
				if (obj instanceof Line) {
					((Line) obj).setxEnd(((Line) obj).getxEnd() + xOffset);
					((Line) obj).setyEnd(((Line) obj).getyEnd() + yOffset);
				}

				dModel.getObject(i).paintComponent(g);
				
				obj.setxCenter(obj.getxCenter() - xOffset);
				obj.setyCenter(obj.getyCenter() - yOffset);
				if (obj instanceof Line) {
					((Line) obj).setxEnd(((Line) obj).getxEnd() - xOffset);
					((Line) obj).setyEnd(((Line) obj).getyEnd() - yOffset);
				}
			}
			g.dispose();

			// Cut the helping image into the final image
			int xCut = rec.getMinX() < 0 ? 0 : (int)rec.getMinX();
			int yCut = rec.getMinY() < 0 ? 0 : (int)rec.getMinY();
			
			BufferedImage image = new BufferedImage((int) rec.getWidth() + xOffset,
					(int) rec.getHeight() + yOffset, BufferedImage.TYPE_3BYTE_BGR);
			g = image.createGraphics();
			g.fillRect(0, 0, (int) rec.getWidth() + xOffset, (int) rec.getHeight() + yOffset);
			g.drawImage(imageHelp, 0, 0, (int) rec.getWidth() + xOffset,
					(int) rec.getHeight() + yOffset, xCut,
					yCut, (int) (rec.getWidth() + xCut),
					(int) (rec.getHeight() + yCut), null);
			g.dispose();

			File file = path.toFile();
			String format = extension.substring(1);
			try {
				ImageIO.write(image, format, file);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(mainFrame, "Exporting failed.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(mainFrame, "Exporting successful.",
					"Info", JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Save the current document.
	 */
	public void saveDocument() {

		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save .jvd file");
		fc.setFileFilter(new FileNameExtensionFilter("JVD FILES", "jvd"));

		int result = fc.showSaveDialog(mainFrame);

		if (result != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(mainFrame, "Document won't be saved.",
					"Info message", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		// Add extension if necessary.
		Path path = fc.getSelectedFile().toPath();
		if (!path.toString().toLowerCase().endsWith(".jvd")) {
			path = Paths.get(path.toString() + ".jvd");
		}

		// Check if file exists
		if (Files.exists(path)) {
			int dialogButton = JOptionPane.showConfirmDialog(mainFrame,
					"Are you sure that you want to overwrite "
							+ path.getFileName().toString(),
					"Warning message", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);

			if (dialogButton == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(mainFrame,
						"Document won't be saved.", "Info message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

		writeToFile(path);

		// Set model as saved
		dModel.saveModel();

		// Set model path
		dModel.setFilePath(path);
		mainFrame.setTitle("PaintApp - " + path.getFileName());
	}

	/**
	 * Write contents of the model to given file.
	 * 
	 * @param path
	 *            Path to the file.
	 */
	public void writeToFile(Path path) {
		// Write contents to file
		try {
			Files.write(path, "".getBytes());
			for (int i = 0, len = dModel.getSize(); i < len; i++) {
				GeometricalObject obj = dModel.getObject(i);
				Files.write(path, obj.asText().getBytes(),
						StandardOpenOption.APPEND);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(mainFrame,
					"Error occured while saving.", "Info message",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(mainFrame,
				"File is saved to " + path.toString() + ".", "Info message",
				JOptionPane.INFORMATION_MESSAGE);

		dModel.saveModel();
	}

	/**
	 * Parse a single line of the document.
	 * 
	 * @param line
	 *            Line containing object information.
	 * @return Appropriate geometrical object.
	 */
	private GeometricalObject parse(String line) {
		String[] split = line.split("\\s+");

		if (split[0].equals("LINE")) {
			return new Line(Integer.valueOf(split[1]),
					Integer.valueOf(split[2]), Integer.valueOf(split[3]),
					Integer.valueOf(split[4]),
					new Color(Integer.valueOf(split[5]),
							Integer.valueOf(split[6]), Integer
									.valueOf(split[7])));

		} else if (split[0].equals("CIRCLE")) {
			return new Circle(Integer.valueOf(split[1]),
					Integer.valueOf(split[2]), Integer.valueOf(split[3]),
					new Color(Integer.valueOf(split[4]),
							Integer.valueOf(split[5]),
							Integer.valueOf(split[6])));

		} else if (split[0].equals("FCIRCLE")) {
			return new FilledCircle(Integer.valueOf(split[1]),
					Integer.valueOf(split[2]), Integer.valueOf(split[3]),
					new Color(Integer.valueOf(split[4]),
							Integer.valueOf(split[5]),
							Integer.valueOf(split[6])),
					new Color(Integer.valueOf(split[7]),
							Integer.valueOf(split[8]),
							Integer.valueOf(split[9])));

		} else {
			throw new IllegalArgumentException(
					split[0] + " is not supported object type");
		}
	}
}
