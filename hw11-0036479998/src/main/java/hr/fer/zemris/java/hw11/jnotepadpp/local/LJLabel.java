package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JLabel;

/**
 * Label which has an automatic listener for any localization changes.
 * 
 * @author Lovro Markovič
 *
 */
public class LJLabel extends JLabel {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Current key for the language bundle.
	 */
	private String key;

	/**
	 * Current length of the document.
	 */
	private int len;

	/**
	 * Reference to the lcoalization provider.
	 */
	private ILocalizationProvider lp;

	/**
	 * Constructor for this JLabel. It accepts a key and a localization provider
	 * reference.
	 * 
	 * @param key
	 *            Key whose corresponding value will be shown in the label text.
	 * @param lp
	 *            Localization provider.
	 */
	public LJLabel(String key, ILocalizationProvider lp) {
		super();

		this.key = key;
		this.lp = lp;

		lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				String translation = lp.getString(LJLabel.this.key);

				if (LJLabel.this.getText().isEmpty()) {
					return;
				}

				LJLabel.this.setText(translation + ": " + LJLabel.this.len);
			}
		});
	}

	/**
	 * Sets the label text in a format (length translation): len.
	 * 
	 * @param len
	 *            Current length of the document.
	 */
	public void setDocLength(int len) {
		this.len = len;
		String translation = lp.getString(key);

		setText(translation + ": " + this.len);
	}
}
