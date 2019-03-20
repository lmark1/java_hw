package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Localization provider class which extends AbstractLocalizationPrivder class.
 * It implements the getString() method which returns a value connected to the
 * given key from the language bundle.
 * 
 * @author Lovro Marković
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/**
	 * Currently selected language.
	 */
	private String language = new String("en");

	/**
	 * Currently loaded bundle.
	 */
	private ResourceBundle bundle;

	/**
	 * A single active instance of this class.
	 */
	private static final LocalizationProvider instance = new LocalizationProvider();

	/**
	 * Localization provider constructor. Initializes default locale and bundle
	 * (english).
	 */
	private LocalizationProvider() {
		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.bundles.prijevodi",
				locale);
	}

	/**
	 * @return Returns a singleton instance of this class.
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}

	/**
	 * Sets a new language and loads up a new bundle (if the language is
	 * different from the current languag).
	 * 
	 * @param language
	 *            New language request.
	 */
	public void setLanguage(String language) {
		if (language.equals(this.language)) {
			return;
		}

		this.language = language;
		Locale locale = Locale.forLanguageTag(this.language);
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.bundles.prijevodi",
				locale);
		instance.fire();
	}
	
	/**
	 * @return Currently selected language.
	 */
	public String getLanguage() {
		return language;
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
}
