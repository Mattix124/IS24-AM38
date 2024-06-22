package it.polimi.ingsw.am38.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SmallModel
{

	private PropertyChangeListener l;
	private String d;

	public void change(String d)
	{
		PropertyChangeEvent evt = new PropertyChangeEvent(this, "Mam", this.d, d);
		this.d = d;
		l.propertyChange(evt);
	}

	public void setListener(PropertyChangeListener l)
	{
		this.l = l;
	}
}
