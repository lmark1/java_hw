package hr.fer.zemris.galerija.model;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/**
 * This class represents a single picture with its description and tags.
 * 
 * @author Lovro Marković
 *
 */
public class Picture {

	/**
	 * Path to gallery.
	 */
	private static final Path GALLERY_PATH = Paths
			.get("./src/main/webapp/WEB-INF/slike");

	/**
	 * Name of the picture
	 */
	private String name;

	/**
	 * Picture description.
	 */
	private String description;

	/**
	 * Picture description tags.
	 */
	private String[] tags;

	/**
	 * Default constructor for this object. Initializes all fields.
	 * 
	 * @param name
	 *            Name.
	 * @param description
	 *            Description.
	 * @param tags
	 *            List of tags.
	 */
	public Picture(String name, String description, String[] tags) {
		super();
		this.name = name;
		this.description = description;
		this.tags = tags;
	}

	/**
	 * @return Returns picture description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Returns name of the picture.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns a list of tags.
	 */
	public String[] getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Write picture to given output stream.
	 * 
	 * @param o
	 *            Output stream.
	 * @throws IOException
	 *             Exception thrown if a file error occurs.
	 */
	public void writeToStream(OutputStream o) throws IOException {

		FileInputStream in = new FileInputStream(
				GALLERY_PATH.resolve(name).toFile());
		
		byte[] buffer = new byte[4096];
		int count = 0;
		while ( (count = in.read(buffer)) >= 0) {
			o.write(buffer, 0, count);
		}
		in.close();
	}
	
	/**
	 * @return Returns Image object or null if cannot be read.
	 */
	public Image getImage() {
		try {
			return ImageIO.read(GALLERY_PATH.resolve(name).toFile());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
