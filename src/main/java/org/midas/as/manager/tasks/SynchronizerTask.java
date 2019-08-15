package org.midas.as.manager.tasks;

import java.util.concurrent.Callable;

import org.midas.as.broker.Broker;
import org.midas.as.manager.manager.Manager;

public class SynchronizerTask implements Callable<Object>
{	
	public Object call() 
	{
		Broker broker = Broker.getInstance();
		
		try 
		{
			while(broker.pingServer())
			{
				Thread.sleep(2000);
			}			
			
			Manager.getInstance().disconnect(false);
		} 
		catch (InterruptedException e) 
		{
			if(!Manager.getInstance().isStopping())
			{
				Manager.getInstance().disconnect(false);
			}			
		}				
		
		return null;
	}	
}
