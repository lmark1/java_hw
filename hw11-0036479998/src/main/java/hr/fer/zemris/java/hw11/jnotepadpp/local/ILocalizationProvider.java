package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This interface defines the Localization provider objects.
 * 
 * @author Lovro Marković
 *
 */
public interface ILocalizationProvider {

	/**
	 * Adds a localization listener to this provider which listens for
	 * localization changes.
	 * 
	 * @param listener
	 *            Localization listener.
	 */
	public void addLocalizationListener(ILocalizationListener listener);

	/**
	 * Removes localization listener.
	 * 
	 * @param listener
	 *            Lovalization listener.
	 */
	public void removeLocalizationListener(ILocalizationListener listener);

	/**
	 * @param key
	 *            Key which corresponds to a value contain in the language
	 *            bundle.
	 * @return Value in the language bundle which is connected to the given key.
	 */
	public String getString(String key);
}
