package org.midas.as.manager.tasks;

import java.util.concurrent.Callable;

import org.midas.as.broker.Broker;
import org.midas.as.manager.ManagerException;
import org.midas.as.manager.manager.Manager;

public class SynchronizerTask implements Callable<Object> {
	private final String port;

	public SynchronizerTask(String port) {
		this.port = port;
	}

	// TODO - Modificar o codigo para tentar reconectar sempre
	public Object call() {
		Broker broker = Broker.getInstance();

		try {
			while (broker.pingServer()) {
				Thread.sleep(2000);
			}

			Manager.getInstance().disconnect(port, false);
		} catch (InterruptedException | ManagerException e) 
		{
			if(!Manager.getInstance().isStopping())
			{
				try {
					Manager.getInstance().disconnect(port, false);
				} catch (ManagerException e1) {
					e1.printStackTrace();
				}
			}			
		}				
		
		return null;
	}	
}
