package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This class extends AbstractLocalizationProvider. It serves as a decorator
 * class for the Localization provider. GetString method is delegated to the
 * localization provider.
 * 
 * @author Lovro Marković
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Reference to the localization provider.
	 */
	private ILocalizationProvider lProvider;

	/**
	 * Flag signals if listener is connected to the Localization provider.
	 */
	private boolean connected;

	/**
	 * Listener for the localization provider. If there is a localization change
	 * it signals all the listeners.
	 */
	private ILocalizationListener listener = new ILocalizationListener() {

		@Override
		public void localizationChanged() {
			LocalizationProviderBridge.this.fire();
		}
	};

	/**
	 * Constructor for the localization provider bridge. Initializes
	 * localization provider reference.
	 * 
	 * @param lProvider
	 *            Reference to the localization provider.
	 */
	public LocalizationProviderBridge(ILocalizationProvider lProvider) {
		this.lProvider = lProvider;
	}

	/**
	 * Connects a listener to the localization provider. If listener is already
	 * connected it does nothing.
	 */
	public void connect() {
		if (connected) {
			return;
		}

		connected = true;
		lProvider.addLocalizationListener(listener);
	}

	/**
	 * Disconnects the listener from the localization provider. If the listener
	 * is already disconnected it does nothing.
	 */
	public void disconnect() {
		if (!connected) {
			return;
		}

		connected = false;
		lProvider.removeLocalizationListener(listener);
	}

	@Override
	public String getString(String key) {
		return lProvider.getString(key);
	}

}
