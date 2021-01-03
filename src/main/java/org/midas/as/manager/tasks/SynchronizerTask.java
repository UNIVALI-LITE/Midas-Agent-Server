package org.midas.as.manager.tasks;

import java.util.concurrent.Callable;

import org.midas.as.broker.Broker;
import org.midas.as.manager.manager.Manager;

public class SynchronizerTask implements Callable<Object>
{
	private final String port;

	public SynchronizerTask(String port) {
		this.port = port;
	}

	// TODO - Modificar o codigo para tentar reconectar sempre
	public Object call() 
	{
		Broker broker = Broker.getInstance();
		
		try 
		{
			while(broker.pingServer())
			{
				Thread.sleep(2000);
			}			
			
			Manager.getInstance().disconnect(port, false);
		} 
		catch (InterruptedException e) 
		{
			if(!Manager.getInstance().isStopping())
			{
				Manager.getInstance().disconnect(port, false);
			}			
		}				
		
		return null;
	}	
}
