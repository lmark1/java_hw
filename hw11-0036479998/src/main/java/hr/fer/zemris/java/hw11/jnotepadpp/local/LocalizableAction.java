package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * AbstractAction whose name automatically updates with localization changes.
 * 
 * @author Lovro Marković
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Suffix for updating short description in the properties files.
	 */
	private static final String SD_SUFFIX = ".sd";

	/**
	 * Current key for language bundle.
	 */
	private String key;

	/**
	 * Constructor for the Localizable action objects.
	 * 
	 * @param key
	 *            Key whose corresponding value will be shown in the Action
	 *            name.
	 * @param lp
	 *            Localization provider.
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;
		String nameTrans = lp.getString(key);
		putValue(Action.NAME, nameTrans);
		
		String sdTrans = lp.getString(key+SD_SUFFIX);
		putValue(Action.SHORT_DESCRIPTION, sdTrans);
		
		
		lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				String translation = lp.getString(LocalizableAction.this.key);
				putValue(Action.NAME, translation);
				
				String sdTrans = lp.getString(LocalizableAction.this.key+SD_SUFFIX);
				putValue(Action.SHORT_DESCRIPTION, sdTrans);
			
			}
		});
	}

}
