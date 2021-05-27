package org.midas.as.manager.tasks;

import java.util.concurrent.Callable;

import org.midas.as.manager.manager.ManagerScreen;

public class StatisticsTask implements Callable<Object> 
{
	public Object call() throws Exception 
	{
		while(true)
		{
			//ManagerScreen.userInterfaceEvent("Refresh Statistics");
			Thread.sleep(250);
		}												
	}
}
