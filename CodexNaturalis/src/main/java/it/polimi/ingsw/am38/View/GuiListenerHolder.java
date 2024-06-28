package it.polimi.ingsw.am38.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 */
public class GuiListenerHolder
{
	/**
	 * Listener for event
	 */
	private PropertyChangeListener listener;
	/**
	 * String to set the old value (not utilized)
	 */
	private String received = "old";

	/**
	 *
	 * @param property "header" to sort the various method
	 * @param ob object passed to send information
	 */
	public void changeProperty(String property ,Object ob)
	{
		PropertyChangeEvent evt = new PropertyChangeEvent(this, property, this.received, ob);
		listener.propertyChange(evt);
	}

	/**
	 * Setter for listener
	 * @param l listener
	 */
	public void setListener(PropertyChangeListener l)
	{
		this.listener = l;
	}
}
