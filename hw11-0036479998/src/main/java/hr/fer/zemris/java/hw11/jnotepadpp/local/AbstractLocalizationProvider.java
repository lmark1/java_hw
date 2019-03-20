package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements all methods from ILocalizationProvider except
 * getString(..).
 * 
 * @author Lovro Marković
 *
 */
public abstract class AbstractLocalizationProvider
		implements ILocalizationProvider {

	/**
	 * List of localization listeners.
	 */
	List<ILocalizationListener> listenerList;

	/**
	 * Default constructor for this class. Initializes the listener list.
	 */
	public AbstractLocalizationProvider() {
		listenerList = new ArrayList<>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listenerList.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listenerList.remove(listener);
	}

	/**
	 * Notifies all subscribed listeners when a localization change happens.
	 */
	public void fire() {
		for (ILocalizationListener listener : listenerList) {
			listener.localizationChanged();
		}
	}

}
