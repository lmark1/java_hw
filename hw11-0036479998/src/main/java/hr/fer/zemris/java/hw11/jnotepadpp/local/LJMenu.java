package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * JMenu which has an automatic listener for any localization changes.
 * 
 * @author Lovro Marković
 *
 */
public class LJMenu extends JMenu {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Current key for language bundle.
	 */
	private String key;

	/**
	 * Constructor for this object.
	 * 
	 * @param key
	 *            Key whose corresponding value will be shown in the menu text.
	 * @param lp
	 *            Localization provider.
	 */
	public LJMenu(String key, ILocalizationProvider lp) {
		super();

		this.key = key;
		String translation = lp.getString(key);
		setText(translation);

		lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				String translation = lp.getString(LJMenu.this.key);
				LJMenu.this.setText(translation);
			}
		});
	}
}
