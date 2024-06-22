package it.polimi.ingsw.am38.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GuiModel
{

	private PropertyChangeListener listener;
	private String received;

	public void change(String received)
	{
		PropertyChangeEvent evt = new PropertyChangeEvent(this, "Mam", this.received, received);
		this.received = received;
		listener.propertyChange(evt);
	}

	public void setListener(PropertyChangeListener l)
	{
		this.listener = l;
	}
}
