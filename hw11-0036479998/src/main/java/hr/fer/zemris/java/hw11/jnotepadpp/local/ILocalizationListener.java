package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This interface defines the Localization listener object. The listener tracks
 * any changes made to the localization.
 * 
 * @author Lovro Markovič
 *
 */
public interface ILocalizationListener {

	/**
	 * This method describes listener behaviour when it detects a localization
	 * change.
	 */
	public void localizationChanged();
}
