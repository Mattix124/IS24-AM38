package it.polimi.ingsw.am38.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GuiModel
{

	private PropertyChangeListener listener;
	private String received = "old";

	public void changeLogin(String received)
	{
		PropertyChangeEvent evt = new PropertyChangeEvent(this, "Login", this.received, received);

		listener.propertyChange(evt);
	}

	public void changeSetUp(String property ,Object ob)
	{
		PropertyChangeEvent evt = new PropertyChangeEvent(this, property, this.received, ob);
		listener.propertyChange(evt);
	}


	public void setListener(PropertyChangeListener l)
	{
		this.listener = l;
	}
}
