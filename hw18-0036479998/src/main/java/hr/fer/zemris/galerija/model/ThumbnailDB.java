package hr.fer.zemris.galerija.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/**
 * This class contains some methods for operating with thumbnails.
 * 
 * @author Lovro Marković
 *
 */
public class ThumbnailDB {

	/**
	 * Path to the thumbnail gallery.
	 */
	private static final Path THUMBNAIL_PATH = Paths
			.get("./src/main/webapp/WEB-INF/thumbnails/");

	/**
	 * Thumbail prefix added to the original image name.
	 */
	private static final String THUMBNAIL_PREFIX = "tb_";

	/**
	 * Maximum thumbnail width.
	 */
	private static final int MAX_WIDTH = 150;

	/**
	 * Maximum thumbnail height.
	 */
	private static final int MAX_HEIGHT = 150;

	/**
	 * Check if thumbnail gallery contains the image name.
	 * 
	 * @param name
	 *            Image name.
	 * @return True if it contains, false otherwise.
	 * @throws IOException
	 *             Exception thrown if directory cannot be created.
	 */
	public static boolean contains(String name) throws IOException {
		if (!Files.exists(THUMBNAIL_PATH)) {
			Files.createDirectories(THUMBNAIL_PATH);
		}
		return Files.exists(THUMBNAIL_PATH.resolve(THUMBNAIL_PREFIX + name));
	}

	/**
	 * Create a picture thumbnail.
	 * 
	 * @param picture
	 *            Given picture.
	 * @throws IOException
	 *             Exception thrown if an error occurs while making thumbnail.
	 */
	public static void createThumbnail(Picture picture) throws IOException {

		if (!Files.exists(THUMBNAIL_PATH)) {
			Files.createDirectories(THUMBNAIL_PATH);
		}

		Image image = picture.getImage();

		BufferedImage bim = new BufferedImage(MAX_WIDTH, MAX_HEIGHT,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = bim.createGraphics();
		g2d.drawImage(image, 0, 0, MAX_WIDTH, MAX_HEIGHT, null);
		g2d.dispose();

		System.out.println("Thumbnail path:");
		System.out.println(
				THUMBNAIL_PATH.resolve(THUMBNAIL_PREFIX + picture.getName()));
		ImageIO.write(bim, "jpg", THUMBNAIL_PATH
				.resolve(THUMBNAIL_PREFIX + picture.getName()).toFile());
	}

	/**
	 * Write thumbnail to stream.
	 * 
	 * @param o
	 *            Output stream.
	 * @param name
	 *            Name of the picture.
	 * @throws IOException
	 *             Exception thrown if there is an error while writing.
	 */
	public static void writeToStream(OutputStream o, String name)
			throws IOException {

		if (!Files.exists(THUMBNAIL_PATH)) {
			Files.createDirectories(THUMBNAIL_PATH);
		}

		FileInputStream in = new FileInputStream(
				THUMBNAIL_PATH.resolve(THUMBNAIL_PREFIX + name).toFile());

		byte[] buffer = new byte[4096];
		int count = 0;
		while ((count = in.read(buffer)) >= 0) {
			o.write(buffer, 0, count);
		}
		in.close();
	}
}
