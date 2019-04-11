package org.midas.as.manager.manager.listeners;

import org.midas.as.manager.manager.ManagerScreen;

public class BuildListener
{		
	public void displayDetails(Object selection)
	{
		ManagerScreen.refreshBuildDetails(selection);
	}
}
