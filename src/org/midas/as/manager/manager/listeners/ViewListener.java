package org.midas.as.manager.manager.listeners;

import org.midas.as.manager.manager.ManagerScreen;

public class ViewListener
{		
	public void updateTree()
	{
		ManagerScreen.userInterfaceEvent("Refresh Services");
	}
	
	public void displayDetails()
	{
		ManagerScreen.userInterfaceEvent("Refresh Details");
	}
}
