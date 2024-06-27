package it.polimi.ingsw.am38.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 */
public class GuiListenerHolder
{
	/**
	 *
	 */
	private PropertyChangeListener listener;
	/**
	 *
	 */
	private String received = "old";

	/**
	 *
	 * @param property
	 * @param ob
	 */
	public void changeProperty(String property ,Object ob)
	{
		PropertyChangeEvent evt = new PropertyChangeEvent(this, property, this.received, ob);
		listener.propertyChange(evt);
	}

	/**
	 *
	 * @param l
	 */
	public void setListener(PropertyChangeListener l)
	{
		this.listener = l;
	}
}
