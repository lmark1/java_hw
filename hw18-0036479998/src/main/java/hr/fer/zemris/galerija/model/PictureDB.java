package hr.fer.zemris.galerija.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a picture database. It contains factory methods used
 * for fetching pictures.
 * 
 * @author Lovro Marković
 *
 */
public class PictureDB {

	/**
	 * Relative path to the picture description
	 */
	private static final Path DESCRIPTION_PATH = Paths
			.get("./src/main/webapp/WEB-INF/opisnik.txt");
	
	/**
	 * Get picture by name.
	 * 
	 * @param name Name of the picture.
	 * @return Picture object.
	 */
	public static Picture getPicture(String name) {
		return getPictures((t, n) -> n.equals(name)).get(0);
	}
	
	/**
	 * @return Returns a list of all picture objects or null if unable to read.
	 */
	public static List<Picture> getAllPictures() {
		return getPictures((tags, name) -> true);
	}

	/**
	 * @param tag
	 *            Picture tag.
	 * @return Return all pictures with containing the given picture tag.
	 */
	public static List<Picture> getFilteredPictures(String tag) {
		return getPictures((tags, name) -> Arrays.asList(tags).contains(tag));
	}
	
	/**
	 * @return Returns set of all tags.
	 */
	public static Set<String> getTags() {
		List<Picture> pictures = getPictures((tags, name)->true);
		Set<String> tags = new HashSet<>();
		
		for (Picture picture : pictures) {
			tags.addAll(Arrays.asList(picture.getTags()));
		}
		
		return tags;
	}
	
	/**
	 * @param filter
	 *            Picture filter.
	 * @return Returns a list of all filtered pictures.
	 */
	private static List<Picture> getPictures(IFilter filter) {
		List<String> lines = null;

		try {
			lines = Files.readAllLines(DESCRIPTION_PATH);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		List<Picture> pictureList = new ArrayList<>();
		for (int i = 0, len = lines.size(); i < len; i += 3) {

			String name = lines.get(i);
			String description = lines.get(i + 1);
			String[] tags = lines.get(i + 2).replaceAll("\\s+", "").split(",");

			if (filter.accept(tags, name)) {
				pictureList.add(new Picture(name, description, tags));
			}
		}

		return pictureList;
	}

	/**
	 * This interface is used for filtering pictures.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private interface IFilter {

		/**
		 * @param tags
		 *            Array of tags.
		 * @param name Name of the pictures.
		 * @return Returns true if picture is accepted, false otherwise.
		 */
		boolean accept(String[] tags, String name);
	}

}
